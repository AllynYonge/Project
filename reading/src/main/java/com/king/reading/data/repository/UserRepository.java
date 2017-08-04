package com.king.reading.data.repository;

import com.blankj.utilcode.util.EmptyUtils;
import com.google.common.collect.Lists;
import com.king.reading.common.utils.ACacheMgr;
import com.king.reading.common.utils.Check;
import com.king.reading.common.utils.TarsUtils;
import com.king.reading.data.db.AppDatabase;
import com.king.reading.data.entities.BookBaseEntity;
import com.king.reading.data.entities.BookBaseEntity_Table;
import com.king.reading.data.entities.BookEntity_Table;
import com.king.reading.data.entities.CityEntity;
import com.king.reading.data.entities.CourseEntity;
import com.king.reading.data.entities.DbMappers;
import com.king.reading.data.entities.DistrictEntity;
import com.king.reading.data.entities.NoticeEntity;
import com.king.reading.data.entities.PlayerEntities;
import com.king.reading.data.entities.ProvinceEntity;
import com.king.reading.data.entities.PushCountEntity;
import com.king.reading.data.entities.PushCountEntity_Table;
import com.king.reading.data.entities.ReadAfterMeEntity;
import com.king.reading.data.entities.ReadAfterMeEntity_Table;
import com.king.reading.data.entities.SchoolEntity;
import com.king.reading.data.entities.SchoolEntity_Table;
import com.king.reading.data.entities.UserEntity;
import com.king.reading.data.entities.UserEntity_Table;
import com.king.reading.ddb.GetAchievementResponse;
import com.king.reading.ddb.GetAfterSchoolCourseResponse;
import com.king.reading.ddb.GetAllAfterSchoolCourseResponse;
import com.king.reading.ddb.GetAreaCodeResponse;
import com.king.reading.ddb.GetNotificationsResponse;
import com.king.reading.ddb.GetPushCountResponse;
import com.king.reading.ddb.GetReadAfterMeGameBoardResponse;
import com.king.reading.ddb.GetReadAfterMeInfoResponse;
import com.king.reading.ddb.GetSchoolsResponse;
import com.king.reading.ddb.GetVerifyCodeResponse;
import com.king.reading.ddb.LoginResponse;
import com.king.reading.ddb.ProfileResponse;
import com.king.reading.ddb.RegisterResponse;
import com.king.reading.ddb.Score;
import com.king.reading.exception.UserNotFoundException;
import com.king.reading.mod.Header;
import com.king.reading.mod.Response;
import com.king.reading.net.Api;
import com.king.reading.net.request.ChangePasswordReq;
import com.king.reading.net.request.CleanPushCountReq;
import com.king.reading.net.request.GetAchievementReq;
import com.king.reading.net.request.GetAfterSchoolCourseReq;
import com.king.reading.net.request.GetAllAfterSchoolCourseReq;
import com.king.reading.net.request.GetAreaCodeReq;
import com.king.reading.net.request.GetNotificationsReq;
import com.king.reading.net.request.GetProfileReq;
import com.king.reading.net.request.GetPushCountReq;
import com.king.reading.net.request.GetReadAfterMeGameBoardReq;
import com.king.reading.net.request.GetReadAfterMeInfoReq;
import com.king.reading.net.request.GetSchoolReq;
import com.king.reading.net.request.GetVerifyCodeReq;
import com.king.reading.net.request.LoginReq;
import com.king.reading.net.request.PostReadAfterMeScoreReq;
import com.king.reading.net.request.ReadNotificationReq;
import com.king.reading.net.request.RegisterReq;
import com.king.reading.net.request.ResetPasswordReq;
import com.king.reading.net.request.UpdateAfterSchoolCourseReq;
import com.king.reading.net.request.UpdateUserInfoReq;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.king.reading.data.entities.CourseEntity_Table.courseId;
import static com.raizlabs.android.dbflow.sql.language.SQLite.select;


/**
 * Created by AllynYonge on 26/06/2017.
 */

@Singleton
public class UserRepository {
    private Api api;
    private Api testApi;

    @Inject
    public UserRepository(@Named("tars") Api api, @Named("gson") Api testApi) {
        this.api = api;
        this.testApi = testApi;
    }

    public UserEntity getUser() {
        return select().from(UserEntity.class).where(UserEntity_Table.token.notEq("")).querySingle();
    }

    public long getBookId(){
        return getUser().usingBook;
    }

    public int getUseResourceId() {
        BookBaseEntity useBook = SQLite.select().from(BookBaseEntity.class)
                .where(BookEntity_Table.bookId.eq(getBookId()))
                .querySingle();
        return useBook.resourceId;
    }

    public Single<UserEntity> login(final String userName, final String password) {
        LoginReq request = new LoginReq(api, userName, password);
        return request.sendRequest().map(new Function<Response, UserEntity>() {
            @Override
            public UserEntity apply(@io.reactivex.annotations.NonNull Response rsp) throws Exception {
                Header header = rsp.getHeader();
                LoginResponse logRsp = (LoginResponse) TarsUtils.getBodyRsp(LoginResponse.class, rsp.getBody());
                UserEntity user = DbMappers.transferUser(logRsp.userInfo, header, logRsp.isFirstLogin);
                if (user.save()) {
                    return user;
                } else {
                    throw new SQLException("存储用户信息失败");
                }

            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> logout() {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                UserEntity user = getUser();
                if (EmptyUtils.isEmpty(user))
                    e.onError(new UserNotFoundException("当前没有登录用户"));
                user.token = "";
                user.account = "";
                user.refreshToken = "";
                e.onSuccess(user.save());
            }
        });
    }

    public Single<UserEntity> getNewestUserInfo() {
        long userId = getUser().userId;
        GetProfileReq req = new GetProfileReq(api, userId);
        return req.sendRequest().map(new Function<Response, UserEntity>() {
            @Override
            public UserEntity apply(@NonNull Response rsp) throws Exception {
                Header header = rsp.getHeader();
                ProfileResponse profileRsp = (ProfileResponse) TarsUtils.getBodyRsp(LoginResponse.class, rsp.getBody());
                return DbMappers.transferUser(profileRsp.userInfo, header, 0);
            }
        }).doOnSuccess(new Consumer<UserEntity>() {
            @Override
            public void accept(@NonNull UserEntity userEntity) throws Exception {
                userEntity.save();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserEntity> register(String userName, String password, String verifyCode) {
        RegisterReq req = new RegisterReq(api, userName, password, verifyCode);
        return req.sendRequest().observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Response, UserEntity>() {
                    @Override
                    public UserEntity apply(@NonNull Response rsp) throws Exception {
                        Header header = rsp.getHeader();
                        RegisterResponse registerRsp = (RegisterResponse) TarsUtils.getBodyRsp(RegisterResponse.class, rsp.getBody());
                        UserEntity user = DbMappers.transferUser(registerRsp.userInfo, header, 0);
                        if (user.save()) {
                            return user;
                        } else {
                            throw new SQLException("存储用户信息失败");
                        }
                    }
                }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Single<GetVerifyCodeResponse> getVerityCode(String phone) {
        GetVerifyCodeReq req = new GetVerifyCodeReq(api, phone);
        return req.sendRequest().observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> resetPassword(String phone, String password, String verityCode) {
        ResetPasswordReq req = new ResetPasswordReq(api, phone, password, verityCode);
        return req.sendRequest().observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> changePassword(String oldPassword, String newPassword) {
        ChangePasswordReq req = new ChangePasswordReq(api, oldPassword, newPassword);
        return req.sendRequest().observeOn(AndroidSchedulers.mainThread());
    }

    /***************************更新用户信息***************************/
    public Single<Boolean> updateUserInfo(final String nickName, final String className, final long bookId, final long schoolId, final int districtCode, final String avatar) {
        UpdateUserInfoReq req = new UpdateUserInfoReq(api, nickName, className, bookId, schoolId, districtCode, avatar);
        return req.sendRequest().doOnSuccess(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                UserEntity user = getUser();
                if (Check.isNotEmpty(nickName))
                    user.nickName = nickName;
                if (Check.isNotEmpty(className))
                    user.className = className;
                if (Check.isNotEmpty(avatar))
                    user.avatar = avatar;
                if (bookId != 0)
                    user.usingBook = bookId;
                if (schoolId != 0)
                    user.schoolName = SQLite.select(SchoolEntity_Table.name).from(SchoolEntity.class).where(SchoolEntity_Table.schoolId.eq(schoolId)).querySingle().name;
                user.save();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    /***************************获取地区信息**************************/
    private List<ProvinceEntity> queryProvinceEntityForDb() {
        return SQLite.select().from(ProvinceEntity.class).queryList();
    }

    private Maybe<List<ProvinceEntity>> getAreaMaybe() {
        GetAreaCodeReq req = new GetAreaCodeReq(api);
        return req.sendRequest().map(new Function<GetAreaCodeResponse, List<ProvinceEntity>>() {
            @Override
            public List<ProvinceEntity> apply(@io.reactivex.annotations.NonNull GetAreaCodeResponse response) throws Exception {
                return DbMappers.transferCity(response.getProvinces());
            }
        }).doOnSuccess(new Consumer<List<ProvinceEntity>>() {
            @Override
            public void accept(@NonNull List<ProvinceEntity> provinceEntities) throws Exception {
                ProcessModelTransaction.Builder transaction = new ProcessModelTransaction.Builder(new ProcessModelTransaction.ProcessModel<ProvinceEntity>() {
                    @Override
                    public void processModel(ProvinceEntity entity, DatabaseWrapper wrapper) {
                        entity.save();
                        for (CityEntity cityEntity : entity.citys) {
                            FlowManager.getModelAdapter(DistrictEntity.class).saveAll(cityEntity.districts);
                        }
                    }
                }).addAll(provinceEntities);
                FlowManager.getDatabase(AppDatabase.class).executeTransaction(transaction.build());
            }
        }).toMaybe();
    }

    public Maybe<List<ProvinceEntity>> getAreaName() {
        return Maybe.concat(Maybe.create(new MaybeOnSubscribe<List<ProvinceEntity>>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<List<ProvinceEntity>> e) throws Exception {
                boolean isValid = true;
                List<ProvinceEntity> provinceList = null;
                if (isValid) {
                    provinceList = queryProvinceEntityForDb();
                }
                provinceList = Check.isEmpty(provinceList) ? new ArrayList<ProvinceEntity>() : provinceList;
                e.onSuccess(provinceList);
            }
        }).filter(new Predicate<List<ProvinceEntity>>() {
            @Override
            public boolean test(@NonNull List<ProvinceEntity> provinces) throws Exception {
                return Check.isNotEmpty(provinces);
            }
        }), getAreaMaybe().map(new Function<List<ProvinceEntity>, List<ProvinceEntity>>() {
            @Override
            public List<ProvinceEntity> apply(@NonNull List<ProvinceEntity> provinces) throws Exception {
                return queryProvinceEntityForDb();
            }
        }).observeOn(AndroidSchedulers.mainThread()))
                .firstElement();
    }

    public String getAreaNameForId(long id) {
        return SQLite.select()
                .from(BookBaseEntity.class)
                .where(BookBaseEntity_Table.bookId.eq(id))
                .querySingle().areaName;
    }

    /***************************获取地区学校列表***************************/
    public Flowable<List<SchoolEntity>> getSchools(final int districtCode) {
        GetSchoolReq req = new GetSchoolReq(api, districtCode);
        return req.sendRequest().map(new Function<GetSchoolsResponse, List<SchoolEntity>>() {
            @Override
            public List<SchoolEntity> apply(@NonNull GetSchoolsResponse getSchoolsResponse) throws Exception {
                List<SchoolEntity> entities = DbMappers.transferSchool(getSchoolsResponse.getSchools(), districtCode);
                FlowManager.getModelAdapter(SchoolEntity.class).saveAll(entities);
                return entities;
            }
        }).mergeWith(RXSQLite.rx(select()
                .from(SchoolEntity.class)
                .where(SchoolEntity_Table.districtCode.eq(districtCode)))
                .queryList()).observeOn(AndroidSchedulers.mainThread());
    }

    /***************************获取成就***************************/
    public Single<Integer> getTotalStart() {
        GetAchievementReq req = new GetAchievementReq(api);
        return req.sendRequest().map(new Function<GetAchievementResponse, Integer>() {
            @Override
            public Integer apply(@NonNull GetAchievementResponse getAchievementResponse) throws Exception {
                return getAchievementResponse.getTotalStar();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    /**********************获取所有扩展课程*************************/
    private Single<List<CourseEntity>> getExtensionCourseForNet() {
        GetAllAfterSchoolCourseReq req = new GetAllAfterSchoolCourseReq(api);
        return req.sendRequest().map(new Function<GetAllAfterSchoolCourseResponse, List<CourseEntity>>() {
            @Override
            public List<CourseEntity> apply(@NonNull GetAllAfterSchoolCourseResponse getAfterSchoolCourseResponse) throws Exception {
                return DbMappers.mapperCourse(getAfterSchoolCourseResponse.courses);
            }
        }).doOnSuccess(new Consumer<List<CourseEntity>>() {
            @Override
            public void accept(@NonNull List<CourseEntity> courseEntities) throws Exception {
                SQLite.delete(CourseEntity.class).execute();
                FlowManager.getModelAdapter(CourseEntity.class).saveAll(courseEntities);
            }
        });
    }

    private Single<List<CourseEntity>> getExtensionCourseForDB() {
        return RXSQLite.rx(SQLite.select().from(CourseEntity.class)).queryList();
    }

    public Flowable<List<CourseEntity>> getExtensionCourse() {
        return getExtensionCourseForDB().mergeWith(getExtensionCourseForNet());
    }

    /**********************获取当前用户所拥有的扩展课程*************************/
    private Single<List<CourseEntity>> getUserExtensionCourseForNet() {
        GetAfterSchoolCourseReq req = new GetAfterSchoolCourseReq(api);
        return req.sendRequest().map(new Function<GetAfterSchoolCourseResponse, List<CourseEntity>>() {
            @Override
            public List<CourseEntity> apply(@NonNull GetAfterSchoolCourseResponse getAfterSchoolCourseResponse) throws Exception {
                return DbMappers.mapperCourse(getAfterSchoolCourseResponse.courses);
            }
        }).doOnSuccess(new Consumer<List<CourseEntity>>() {
            @Override
            public void accept(@NonNull List<CourseEntity> courseEntities) throws Exception {
                FlowManager.getModelAdapter(CourseEntity.class).saveAll(courseEntities);
                StringBuilder builder = new StringBuilder();
                for (CourseEntity courseEntity : courseEntities) {
                    builder.append(courseEntity).append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                UserEntity user = getUser();
                user.courseIds = builder.toString().trim();
                user.save();
            }
        });
    }

    private Single<List<CourseEntity>> getUserExtensionCourseForDB() {
        UserEntity user = getUser();
        String[] courseArrays = user.courseIds.split(",");
        List<Long> courseList = Lists.newArrayList();
        for (String courseId : courseArrays) {
            courseList.add(Long.parseLong(courseId));
        }
        return RXSQLite.rx(SQLite.select().from(CourseEntity.class).where(courseId.in(courseList))).queryList();
    }

    public Flowable<List<CourseEntity>> getUserExtensionCourse() {
        return getUserExtensionCourseForDB().mergeWith(getUserExtensionCourseForNet());
    }

    /**********************更新用户扩展课*************************/
    public Single<Boolean> updateExtension(int type, long courseId) {
        UpdateAfterSchoolCourseReq req = new UpdateAfterSchoolCourseReq(api, type, courseId);
        return req.sendRequest();
    }


    /**********************清除小红点*************************/
    public Single<Boolean> cleanPushCount(String pushKey) {
        CleanPushCountReq req = new CleanPushCountReq(api, pushKey);
        return req.sendRequest();
    }

    /**********************获取小红点*************************/
    private Single<PushCountEntity> getPushCountForNet() {
        GetPushCountReq req = new GetPushCountReq(api);
        return req.sendRequest().map(new Function<GetPushCountResponse, PushCountEntity>() {
            @Override
            public PushCountEntity apply(@NonNull GetPushCountResponse getPushCountResponse) throws Exception {
                long userId = getUser().userId;
                return DbMappers.mapperPushCount(userId, getPushCountResponse);
            }
        }).doOnSuccess(new Consumer<PushCountEntity>() {
            @Override
            public void accept(@NonNull PushCountEntity pushCountEntity) throws Exception {
                pushCountEntity.save();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    private Single<PushCountEntity> getPushCountForDB() {
        UserEntity user = getUser();
        return RXSQLite.rx(SQLite.select().from(PushCountEntity.class).where(PushCountEntity_Table.userId.eq(user.userId))).querySingle();
    }

    public Flowable<PushCountEntity> getPushCount() {
        return getPushCountForDB().mergeWith(getPushCountForNet());
    }

    /**********************获取跟读闯关信息*************************/
    private Single<ReadAfterMeEntity> getReadAfterMeForNet() {
        GetReadAfterMeInfoReq req = new GetReadAfterMeInfoReq(api, getBookId());
        return req.sendRequest().map(new Function<GetReadAfterMeInfoResponse, ReadAfterMeEntity>() {
            @Override
            public ReadAfterMeEntity apply(@NonNull GetReadAfterMeInfoResponse getReadAfterMeInfoResponse) throws Exception {
                UserEntity userEntity = getUser();
                return DbMappers.mapperReadAfterMe(userEntity.userId, getReadAfterMeInfoResponse.getReadAfterMe());
            }
        }).doOnSuccess(new Consumer<ReadAfterMeEntity>() {
            @Override
            public void accept(@NonNull ReadAfterMeEntity readAfterMeEntity) throws Exception {
                readAfterMeEntity.save();
            }
        });
    }

    private Single<ReadAfterMeEntity> getReadAfterMeForDB() {
        final UserEntity entity = getUser();
        return Single.create(new SingleOnSubscribe<ReadAfterMeEntity>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<ReadAfterMeEntity> e) throws Exception {
                ReadAfterMeEntity readAfterMeEntity = SQLite.select().from(ReadAfterMeEntity.class).where(ReadAfterMeEntity_Table.userId.eq(entity.userId)).querySingle();
                if (EmptyUtils.isNotEmpty(readAfterMeEntity))
                    e.onSuccess(readAfterMeEntity);
            }
        });
    }

    public Flowable<ReadAfterMeEntity> getReadAfterMe() {
        return getReadAfterMeForDB().mergeWith(getReadAfterMeForNet()).observeOn(AndroidSchedulers.mainThread());
    }

    /**********************提交跟读闯关成绩*************************/
    private Single<Boolean> postReadAfterMeScore(long bookId, Score score) {
        PostReadAfterMeScoreReq req = new PostReadAfterMeScoreReq(api, bookId, score);
        return req.sendRequest();
    }

    /**********************获取星星排行榜列表*************************/
    private Single<PlayerEntities> getReadAfterMeGameBoardForNet() {
        GetReadAfterMeGameBoardReq req = new GetReadAfterMeGameBoardReq(api);
        final UserEntity userEntity = getUser();
        return req.sendRequest().map(new Function<GetReadAfterMeGameBoardResponse, PlayerEntities>() {
            @Override
            public PlayerEntities apply(@NonNull GetReadAfterMeGameBoardResponse getReadAfterMeGameBoardResponse) throws Exception {
                return DbMappers.mapperGameBoard(userEntity.userId, getReadAfterMeGameBoardResponse);
            }
        }).doOnSuccess(new Consumer<PlayerEntities>() {
            @Override
            public void accept(@NonNull PlayerEntities playerEntities) throws Exception {
                playerEntities.save();
            }
        });
    }

    private Single<PlayerEntities> getReadAfterMeGameBoardForDB() {
        final UserEntity userEntity = getUser();
        return Single.create(new SingleOnSubscribe<PlayerEntities>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<PlayerEntities> e) throws Exception {
                PlayerEntities playerEntities = SQLite.select().from(PlayerEntities.class).where(UserEntity_Table.userId.eq(userEntity.userId)).querySingle();
                if (EmptyUtils.isNotEmpty(playerEntities))
                    e.onSuccess(playerEntities);
            }
        });
    }

    public Flowable<PlayerEntities> getReadAfterMeGameBoard() {
        return getReadAfterMeGameBoardForDB().mergeWith(getReadAfterMeGameBoardForNet()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取通知列表信息
     */
    public Flowable<List<NoticeEntity>> getNotices(final boolean isPullRefresh) {
        GetNotificationsReq req = new GetNotificationsReq(api, isPullRefresh ? null : ACacheMgr.getNoticesPageContext());
        final UserEntity userEntity = getUser();
        return Single.merge(req.sendRequest().map(new Function<GetNotificationsResponse, List<NoticeEntity>>() {
            @Override
            public List<NoticeEntity> apply(@NonNull GetNotificationsResponse getNotificationsResponse) throws Exception {
                ACacheMgr.saveNoticesPageContext(getNotificationsResponse.pageContext);
                return DbMappers.mapperNotices(userEntity.userId, getNotificationsResponse);
            }
        }).doOnSuccess(new Consumer<List<NoticeEntity>>() {
            @Override
            public void accept(@NonNull List<NoticeEntity> noticeEntities) throws Exception {
                if (isPullRefresh) {
                    SQLite.delete(NoticeEntity.class).execute();
                    FlowManager.getModelAdapter(NoticeEntity.class).saveAll(noticeEntities);
                }
            }
        }), RXSQLite.rx(SQLite.select().from(NoticeEntity.class)).queryList())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 通知标记为已读
     */
    public Single<Boolean> noticeMarkRead(int noticeId) {
        ReadNotificationReq req = new ReadNotificationReq(api, noticeId);
        return req.sendRequest().observeOn(AndroidSchedulers.mainThread());
    }
}
