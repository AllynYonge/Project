package com.king.reading.net;

import com.king.reading.ddb.GetAchievementResponse;
import com.king.reading.ddb.GetActivitiesResponse;
import com.king.reading.ddb.GetAfterSchoolCourseResponse;
import com.king.reading.ddb.GetAllAfterSchoolCourseResponse;
import com.king.reading.ddb.GetAreaCodeResponse;
import com.king.reading.ddb.GetBannersRequest;
import com.king.reading.ddb.GetBannersResponse;
import com.king.reading.ddb.GetBookResponse;
import com.king.reading.ddb.GetBooklistResponse;
import com.king.reading.ddb.GetNotificationsResponse;
import com.king.reading.ddb.GetPageResponse;
import com.king.reading.ddb.GetPlayBookResponse;
import com.king.reading.ddb.GetProductResponse;
import com.king.reading.ddb.GetPushCountResponse;
import com.king.reading.ddb.GetQAResponse;
import com.king.reading.ddb.GetReadAfterMeGameBoardResponse;
import com.king.reading.ddb.GetReadAfterMeInfoResponse;
import com.king.reading.ddb.GetSchoolsResponse;
import com.king.reading.ddb.GetSecKeyResponse;
import com.king.reading.ddb.GetUnitWordsRequest;
import com.king.reading.ddb.GetUnitWordsResponse;
import com.king.reading.ddb.GetVerifyCodeResponse;
import com.king.reading.ddb.GetWordsUnitListResponse;
import com.king.reading.ddb.PlaceOrderResponse;
import com.king.reading.mod.Response;
import com.king.reading.net.request.ChangePasswordReq;
import com.king.reading.net.request.CleanPushCountReq;
import com.king.reading.net.request.GetAchievementReq;
import com.king.reading.net.request.GetActivitiesReq;
import com.king.reading.net.request.GetAfterSchoolCourseReq;
import com.king.reading.net.request.GetAllAfterSchoolCourseReq;
import com.king.reading.net.request.GetAreaCodeReq;
import com.king.reading.net.request.GetBannersReq;
import com.king.reading.net.request.GetBookListReq;
import com.king.reading.net.request.GetBookReq;
import com.king.reading.net.request.GetNotificationsReq;
import com.king.reading.net.request.GetPageReq;
import com.king.reading.net.request.GetPlayBookReq;
import com.king.reading.net.request.GetProductReq;
import com.king.reading.net.request.GetProfileReq;
import com.king.reading.net.request.GetPushCountReq;
import com.king.reading.net.request.GetQAReq;
import com.king.reading.net.request.GetReadAfterMeGameBoardReq;
import com.king.reading.net.request.GetReadAfterMeInfoReq;
import com.king.reading.net.request.GetSchoolReq;
import com.king.reading.net.request.GetSecKeyReq;
import com.king.reading.net.request.GetUnitWordsReq;
import com.king.reading.net.request.GetVerifyCodeReq;
import com.king.reading.net.request.GetWordsUnitListReq;
import com.king.reading.net.request.LoginReq;
import com.king.reading.net.request.PlaceOrderReq;
import com.king.reading.net.request.PostReadAfterMeScoreReq;
import com.king.reading.net.request.ReadNotificationReq;
import com.king.reading.net.request.RegisterReq;
import com.king.reading.net.request.ResetPasswordReq;
import com.king.reading.net.request.SendFeedbackReq;
import com.king.reading.net.request.UpdateAfterSchoolCourseReq;
import com.king.reading.net.request.UpdateUserInfoReq;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hu.yang on 2017/5/15.
 */

public interface Api {
    @POST("/")
    Single<Response> LoginReq (@Body LoginReq request);

    @POST("/")
    Single<GetPageResponse> getPages(@Body GetPageReq request);

    @POST("/")
    Single<Boolean> updateUserInfo(@Body UpdateUserInfoReq request);

    @POST("/")
    Single<GetBooklistResponse> getBooks(@Body GetBookListReq request);

    @POST("/")
    Single<GetBookResponse> getBookDetail(@Body GetBookReq request);

    @POST("/")
    Single<GetAreaCodeResponse> getArea(@Body GetAreaCodeReq request);

    @POST("/")
    Single<GetVerifyCodeResponse> getVerifyCode(@Body GetVerifyCodeReq request);

    @POST("/")
    Single<Response> register(@Body RegisterReq request);

    @POST("/")
    Single<GetSchoolsResponse> getSchoolList(@Body GetSchoolReq request);

    @POST("/")
    Single<GetSecKeyResponse> getSecKey(@Body GetSecKeyReq request);

    @POST("/")
    Single<Boolean> resetPassword(@Body ResetPasswordReq request);
    @POST("/")
    Single<Boolean> changePassword(@Body ChangePasswordReq request);
    @POST("/")
    Single<GetUnitWordsResponse> getUnitWords(@Body GetUnitWordsReq request);
    @POST("/")
    Single<GetWordsUnitListResponse> WordUnitList(@Body GetWordsUnitListReq request);

    @POST("/")
    Single<GetPlayBookResponse> getPlayBook(@Body GetPlayBookReq request);

    @POST("/")
    Single<GetBannersResponse> getBanners(@Body GetBannersReq req);

    /******************支付相关******************/
    /**
     * 获取产品列表
     */
    @POST("/")
    Single<GetProductResponse> getProduct(@Body GetProductReq request);

    /**
     * 获取支付相关信息
     */
    @POST("/")
    Single<PlaceOrderResponse> getPayInfo(@Body PlaceOrderReq request);

    @POST("/")
    Single<GetActivitiesResponse> getActivities(@Body GetActivitiesReq request);

    @POST("/")
    Single<GetNotificationsResponse> getNotifications(@Body GetNotificationsReq request);

    @POST("/")
    Single<Response> getProfile(@Body GetProfileReq request);

    /**
     * 拉取已获得成就
     */
    @POST("/")
    Single<GetAchievementResponse> getAchievement(@Body GetAchievementReq request);

    /**
     * 获取扩展课程
     */
    @POST("/")

    Single<GetAllAfterSchoolCourseResponse> getAllExtensionCourse(@Body GetAllAfterSchoolCourseReq request);
    /**
     * 获取用户扩展课程
     */
    @POST("/")
    Single<GetAfterSchoolCourseResponse> getUserExtensionCourse(@Body GetAfterSchoolCourseReq request);

    /**
     * 修改用户扩展课
     */
    @POST("/")
    Single<Boolean> updateUserSchoolCourse(@Body UpdateAfterSchoolCourseReq request);

    /**
     * 标记通知为已读
     */
    @POST("/")
    Single<Boolean> noticeMarkRead(@Body ReadNotificationReq request);

    /**
     * 过去帮助列表
     */
    @POST("/")
    Single<GetQAResponse> getQA(@Body GetQAReq request);

    @POST("/")
    Single<Boolean> sendFeedBack(@Body SendFeedbackReq request);

    /**
     * 跟读闯关
     */
    @POST("/")
    Single<GetReadAfterMeInfoResponse> getReadAfterMeInfo(@Body GetReadAfterMeInfoReq request);

    /**
     * 提交跟读闯关分数
     */
    @POST("/")
    Single<Boolean> postReadAfterMeScore(@Body PostReadAfterMeScoreReq request);

    /**
     * 获取排行榜
     */
    @POST("/")
    Single<GetReadAfterMeGameBoardResponse> getReadAfterMeGameBoard(@Body GetReadAfterMeGameBoardReq request);

    /**
     * 获取小红点信息
     */
    @POST("/")
    Single<GetPushCountResponse> getPushCount(@Body GetPushCountReq request);

    /**
     * 清除小红点
     */
    @POST("/")
    Single<Boolean> cleanPushCount(@Body CleanPushCountReq request);

}
