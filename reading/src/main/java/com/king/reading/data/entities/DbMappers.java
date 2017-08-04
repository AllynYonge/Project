package com.king.reading.data.entities;

import com.google.common.collect.Lists;
import com.king.reading.ddb.Activity;
import com.king.reading.ddb.Banner;
import com.king.reading.ddb.BookBase;
import com.king.reading.ddb.BookVersion;
import com.king.reading.ddb.City;
import com.king.reading.ddb.Course;
import com.king.reading.ddb.District;
import com.king.reading.ddb.GetActivitiesResponse;
import com.king.reading.ddb.GetBookResponse;
import com.king.reading.ddb.GetBooklistResponse;
import com.king.reading.ddb.GetNotificationsResponse;
import com.king.reading.ddb.GetPageResponse;
import com.king.reading.ddb.GetPlayBookResponse;
import com.king.reading.ddb.GetProductResponse;
import com.king.reading.ddb.GetPushCountResponse;
import com.king.reading.ddb.GetReadAfterMeGameBoardResponse;
import com.king.reading.ddb.Notification;
import com.king.reading.ddb.Page;
import com.king.reading.ddb.Product;
import com.king.reading.ddb.Province;
import com.king.reading.ddb.ReadAfterMe;
import com.king.reading.ddb.School;
import com.king.reading.ddb.Unit;
import com.king.reading.ddb.User;
import com.king.reading.ddb.Word;
import com.king.reading.mod.Header;
import com.king.reading.model.WordSpellModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu.yang on 2017/5/16.
 */

public class DbMappers {
    public static UserEntity transferUser(User user, Header header, int isFirstLogin){
        UserEntity entity = new UserEntity();
        entity.token = header.token;
        entity.refreshToken = header.refreshToken;
        entity.account = header.account;
        entity.userId = user.getUserId();
        entity.firstLogin = isFirstLogin != 0 ? true : false;
        entity.usingBook = user.getUsingBook();
        entity.className = user.getNameOfClass();
        entity.avatar = user.getAvatarURL();
        entity.nickName = user.getNickname();
        entity.schoolName = user.getSchool();
        entity.vip = user.getPurchase().vip != 0 ? true : false;
        entity.vipStartTime = user.getPurchase().getStart();
        entity.vipEndTIme = user.getPurchase().getEnd();
        entity.vipDescription = user.getPurchase().getDesc();
        return entity;
    }


    public static List<ProvinceEntity> transferCity(List<Province> provinces){
        List<ProvinceEntity> localProvinces = new ArrayList<>();
        for (Province province : provinces){
            ProvinceEntity provinceEntity = new ProvinceEntity();
            provinceEntity.name = province.name;
            List<CityEntity> cities = new ArrayList<>();
            for (City city : province.getCities()){
                CityEntity cityEntity = new CityEntity();
                cityEntity.provinceName = province.name;
                cityEntity.name = city.name;
                List<DistrictEntity> districts = new ArrayList<>();
                for (District district : city.districts){
                    DistrictEntity districtEntity = new DistrictEntity();
                    districtEntity.areaCode = district.areaCode;
                    districtEntity.name = district.name;
                    districtEntity.cityName = cityEntity.name;
                    districts.add(districtEntity);
                }
                cityEntity.districts = districts;
                cities.add(cityEntity);
            }
            provinceEntity.citys = cities;
            localProvinces.add(provinceEntity);
        }
        return localProvinces.size() > 1 ? localProvinces : null;
    }

    public static List<SchoolEntity> transferSchool(List<School> schools, int districtCode) {
        List<SchoolEntity> entities = new ArrayList<>();
        for (School school : schools){
            entities.add(new SchoolEntity(school.schoolId, school.name, districtCode));
        }
        return entities;
    }

    public static List<BookBaseEntity> mapperBookList(GetBooklistResponse response){
        List<BookBaseEntity> bookBaseList = Lists.newArrayList();
        for (BookVersion bookVersion : response.areas){
            for (BookBase bookBase : bookVersion.getBooks()){
                BookBaseEntity book = new BookBaseEntity();
                book.areaName = bookVersion.name;
                book.bookId = bookBase.bookID;
                book.bookName = bookBase.name;
                book.coverURL = bookBase.coverURL;
                book.fullName = bookBase.fullName;
                bookBaseList.add(book);
            }
        }
        return bookBaseList;
    }

    public static BookEntity mapperBook(GetBookResponse response){
        BookEntity book = new BookEntity();
        book.bookId = response.book.base.bookID;
        book.startPage = response.book.pageRange.start;
        book.endPage = response.book.pageRange.end;
        book.resourceId = response.book.secKeyPair.resourceID;
        List<ModuleEntity> modules = new ArrayList<>();
        for (com.king.reading.ddb.Module module : response.book.modules){
            ModuleEntity moduleEntity = new ModuleEntity();
            moduleEntity.id = "" + module.moduleID + book.resourceId;
            moduleEntity.title = module.title;
            moduleEntity.start = module.pageRange.start;
            moduleEntity.end = module.pageRange.end;
            moduleEntity.name = module.name;
            moduleEntity.moduleId = module.moduleID;
            moduleEntity.resourceId = book.resourceId;
            modules.add(moduleEntity);
            List<UnitEntity> units = new ArrayList<>();
            for (com.king.reading.ddb.Unit unit : module.units){
                UnitEntity unitEntity = new UnitEntity();
                unitEntity.id = "" + unit.unitID + book.resourceId ;
                unitEntity.resourceId = book.resourceId;
                unitEntity.unitId = unit.unitID;
                unitEntity.moduleId = moduleEntity.id;
                unitEntity.name = unit.name;
                unitEntity.title = unit.title;
                unitEntity.coverURL = unit.coverURL;
                unitEntity.canRolePlay = unit.canRolePlay != 0 ? true : false;
                unitEntity.start = unit.pageRange.start;
                unitEntity.end = unit.pageRange.end;
                units.add(unitEntity);
            }
            moduleEntity.units = units;
        }
        book.modules = modules;
        return book;
    }

    public static List<PageEntity> mapperPage(GetPageResponse response, long resourceId){
        List<PageEntity> pages = new ArrayList<>();
        for(Page page : response.pages){
            PageEntity entity = new PageEntity();
            entity.id = Integer.parseInt(""+resourceId + page.pageNumber);
            entity.number = page.pageNumber;
            entity.page = page;
            pages.add(entity);
        }
        return pages;
    }

    public static List<WordEntity> mapperWords(long resourceId, long unitId, List<Word> words) {
        List<WordEntity> wordEntities = new ArrayList<>(words.size());
        for (Word word : words){
            WordEntity entity = new WordEntity();
            entity.id = String.valueOf(word.word+resourceId+unitId);
            entity.word = word.word;
            entity.mean = word.mean;
            entity.encryptSoundURL = word.encryptSoundURL;
            wordEntities.add(entity);
        }
        return wordEntities;
    }

    public static PlayBooksEntity mapperPlayBooks(long resourceId, long unitId, GetPlayBookResponse response) {
        PlayBooksEntity entity = new PlayBooksEntity();
        entity.id = ""+resourceId+unitId;
        entity.playBooks = response;
        return entity;
    }

    public static List<BannerEntity> mapperBanners(int bannerType, List<Banner> banners) {
        List<BannerEntity> entities = new ArrayList<>();
        for (Banner banner : banners){
            BannerEntity entity = new BannerEntity();
            entity.type = bannerType;
            entity.bannerUrl = banner.coverURL;
            entity.url = banner.url;
            entities.add(entity);
        }
        return entities;
    }

    public static List<ProductEntity> mapperProduct(GetProductResponse response) {
        List<ProductEntity> entityList = new ArrayList<>();
        for (Product product : response.products){
            ProductEntity entity = new ProductEntity();
            entity.productID = product.productID;
            entity.name = product.name;
            entity.price = product.price;
            entity.type = product.type;
            entityList.add(entity);
        }
        return entityList;
    }

    public static List<NoticeEntity> mapperNotices(long userId, GetNotificationsResponse response) {
        List<NoticeEntity> entityList = new ArrayList<>();
        for (Notification notice : response.notifications){
            NoticeEntity entity = new NoticeEntity();
            entity.id = notice.notificationId;
            entity.userId = userId;
            entity.content = notice.content;
            entity.jumpURL = notice.jumpURL;
            entity.createTime = notice.createTime;
            entity.title = notice.title;
            entityList.add(entity);
        }
        return entityList;
    }

    public static List<ActivityEntity> mapperActivities(GetActivitiesResponse response) {
        List<ActivityEntity> entityList = new ArrayList<>();
        for (Activity activity : response.activities){
            ActivityEntity entity = new ActivityEntity();
            entity.activityId = activity.activityId;
            entity.activityDataUrl = activity.activityDataUrl;
            entity.activityUrl = activity.activityUrl;
            entity.coverURL = activity.coverURL;
            entity.detail = activity.detail;
            entity.endTime = activity.endTime;
            entity.startTime= activity.startTime;
            entity.iconUrl= activity.iconUrl;
            entityList.add(entity);
        }
        return entityList;
    }

    public static List<CourseEntity> mapperCourse(List<Course> courses) {
        List<CourseEntity> entityList = Lists.newArrayList();
        for (Course course : courses) {
            CourseEntity entity = new CourseEntity();
            entity.courseId = course.courseID;
            entity.iconURL = course.iconURL;
            entity.name = course.name;
            entity.url = course.url;
            entityList.add(entity);
        }
        return entityList;
    }

    public static PushCountEntity mapperPushCount(long userId, GetPushCountResponse getPushCountResponse) {
        PushCountEntity entity = new PushCountEntity();
        entity.userId = userId;
        entity.pushCountRsp = getPushCountResponse;
        return entity;
    }

    public static ReadAfterMeEntity mapperReadAfterMe(long userId, ReadAfterMe readAfterMe) {
        ReadAfterMeEntity entity = new ReadAfterMeEntity();
        entity.userId = userId;
        entity.readAfterMe = readAfterMe;
        return entity;
    }

    public static PlayerEntities mapperGameBoard(long userId, GetReadAfterMeGameBoardResponse getReadAfterMeGameBoardResponse) {
        PlayerEntities entity = new PlayerEntities();
        entity.userId = userId;
        entity.gameBoard = getReadAfterMeGameBoardResponse;
        return entity;
    }

    public static List<WordSpellModule> mapperUnitWords(List<Unit> units) {
        List<WordSpellModule> unitEntities = Lists.newArrayList();
        for (Unit unit : units) {
            unitEntities.add(new WordSpellModule(unit.title, unit.unitID));
        }
        return unitEntities;
    }
}
