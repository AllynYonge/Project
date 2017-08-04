package com.king.reading;

import android.os.Environment;

import java.io.File;

/** 
* @ClassName: C
* @Description: TODO(配置接口地址、本地文件路径等配置信息) 
* @author LXL
* @date 2016-1-6 上午9:29:36 
*/ 
public class C {
//	public static String WebUrl="http://zuoye.kingsun.cn";//文件下载
	public static final String ROOT_DIRECTORY=Environment.getExternalStorageDirectory()+File.separator;
//	public static final String folderName="STUDY_SZ_DEMO";//文件夹名
	public static final String CATALOGUE_JSON_NAME="catalogue.json";
	public static final String FOLLOW_JSON_NAME="follow.json";
	public static final String ROLEACT_JSON_NAME="roleAct.json";
	public static final String TERTIARY_FOLLOW_JSON_NAME="tertiarys_follow.json";
	public static final String TERTIARY_ROLEACT_JSON_NAME="tertiarys_roleAct.json";
	
	public final static String folder_Root = ROOT_DIRECTORY+"kingReading"+File.separator;//本应用的根路径
	public final static String folder_Res = folder_Root+"Res"+File.separator;//本应用的资源存放路径
	public final static String folder_Temp =  folder_Root+"Temp"+File.separator;//本应用的临时资源（如下载等）存放路径
	public final static String CRASH_BUG_LOG="KingReading_Bug_Logs";//异常退出时bug日志记录
	public final static String FOLDER_RECORD="record"+File.separator;//录音文件保存路径
	/***********************************    APP_IDENTIFY    *************************************/
	public final static String KINGSUN_APP_ID = "2";

	/***********************************    INTENT_ARGUMENT    *************************************/
	public final static String INTENT_BOOKID = "bookId";
	public final static String CACHE_TOKEN_KEY = "token";
	public final static String Cache_NOTICE_KEY = "notice";
	public final static String Cache_ACTIVITY_KEY = "activity";

	/***************************************    CONFIG    *****************************************/
	//资源加密的开关
	public final static String SECRETKEY = "!sad*9sa";
	public final static String API_BASE_URL = "http://192.168.11.7:40000";

	/*************************************    PLATFORM_ID    **************************************/
	//微信APPID
	public final static String AppId_WX="wx7cf275f30805d173";


	/****************************************    ROUTER    ****************************************/
	public final static String ROUTER_LOGIN = "/user/login";
	public final static String ROUTER_FINDPWD = "/user/findPwd";
	public final static String ROUTER_REGISTER = "/user/register";
	public final static String ROUTER_COMPLETION_PROFILE = "/user/completionprofile";
	public final static String ROUTER_MAIN = "/user/main";
	public final static String ROUTER_UPLOADAVATAR = "/user/uploadAvatar";
	public final static String ROUTER_SELECTCLASS = "/user/selectClass";
	public final static String ROUTER_SELECTVERSION = "/user/selectVersion";
	public final static String ROUTER_UPDATEVERSION = "/user/updateVersion";
	public final static String ROUTER_PAYFEATURE = "/user/payFeature";
	public final static String ROUTER_NOTICE = "/user/notice";
	public final static String ROUTER_SETTING = "/user/setting";
	public final static String ROUTER_FEEDBACK = "/user/feedback";
	public final static String ROUTER_CUSTOMER = "/user/customer";
	public final static String ROUTER_SHARE = "/user/share";
	public final static String ROUTER_SETNEWPWD = "/user/setNewPwd";
	public final static String ROUTER_WEB = "/commom/web";
	public final static String ROUTER_READ_DETAIL = "/read/detail";
	public final static String ROUTER_LEARN_LISTEN = "/learn/listen";
	public final static String ROUTER_LEARN_ROLEPLAY = "/learn/rolePlay";
	public static final String ROUTER_LEARN_BREAK = "/learn/break";
	public static final String ROUTER_LEARN_BREAK_SORT = "/learn/break/sort";
	public final static String ROUTER_LEARN_ROLEPLAY_UNIT = "/learn/rolePlay/unit";
	public final static String ROUTER_LEARN_WORDLISTEN = "/learn/wordListen";
	public static final String ROUTER_LEARN_WORDLISTEN_DETAIL = "/learn/wordListen/detail";

	/****************************************    EVENTBUS   ****************************************/
	public final static Integer EADIMAGE_IS_UPDATE = 1;

	/****************************************    HANDLER   ****************************************/
	public static final int HANDLE_DIALOG_CONFIRM = 0X100001;
	public static final int HANDLE_DIALOG_CANCLE = 0X100002;
	public static final int HANDLE_AUTO_READING_ALL_PAGE = 0X100003;
	public static final int HANDLE_READING_TURN_PAGE = 0X100004;
	public static final int HANDLE_INTERRUPT_READING_ALL_PAGE = 0X100005;
	public static final int HANDLE_SELECTED_START_WORD_REPEAT_MODEL = 0X100006;
	public static final int HANDLE_REPEATE_MODE_READING = 0X100007;
	public static final int LISTEN_STOP_PLAY_SOUND = 0X100008;
	public static final int LISTEN_GET_SELECTED = 0X100009;
	public static final int LISTEN_GET_SECONDARY = 0X100010;
	public static final int LISTEN_GET_READING_CONFIG = 0X100011;
	public static final int LISTEN_PLAY_SELECTED_SOUND = 0X100012;
	public static final int LISTEN_TIMER_SENCOND = 0X100013;
	public static final int LISTEN_WORD_TIMER = 0X100014;
	public static final int LISTEN_WORD_SPEED = 0X100015;
	public static final int ROLEPLAY_SELECTEDROLE = 0X100016;
	public static final int ROLEPLAY_MIME_RECORD = 0X100017;
	public static final int ROLE_ACT_EXIT = 0X100018;
	public static final int ROLE_ACT_RESTART = 0X100019;
	public static final int ROLE_ACT_CONTINUE = 0X100020;
	public static final int ROLE_ACT_FINISH = 0X100021;
	public static final int FOLLOW_SHOW_EVALUATE_TIPS = 0X100022;
	public static final int FOLLOW_EVALUATE_ERROR = 0X100023;
	public static final int FOLLOW_CANCLE_EVALUATE_TIPS = 0X100024;
}
