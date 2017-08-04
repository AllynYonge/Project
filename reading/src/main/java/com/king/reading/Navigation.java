package com.king.reading;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by AllynYonge on 12/07/2017.
 */

public class Navigation {

    public void routerRegisterAct(){
        ARouter.getInstance().build(C.ROUTER_REGISTER).navigation();
    }

    public void routerFindPwdAct(){
        ARouter.getInstance().build(C.ROUTER_FINDPWD).navigation();
    }

    public void routerResetPwdAct(String phoneNumber, String verityCode){
        ARouter.getInstance().build(C.ROUTER_SETNEWPWD)
                .withString("verityCode", verityCode)
                .withString("phoneNumber", phoneNumber)
                .navigation();
    }

    public void routerMainAct(long bookId){
        ARouter.getInstance().build(C.ROUTER_MAIN).withLong("bookId",bookId).navigation();
    }

    public void routerCompletionProfileAct(){
        ARouter.getInstance().build(C.ROUTER_COMPLETION_PROFILE).navigation();
    }

    public void routerLoginAct() {
        ARouter.getInstance().build(C.ROUTER_LOGIN).navigation();
    }
    public void routerUploadAvatarAct() {
        ARouter.getInstance().build(C.ROUTER_UPLOADAVATAR).navigation();
    }

    public void routerSelectVersionAct() {
        ARouter.getInstance().build(C.ROUTER_SELECTVERSION).navigation();
    }

    public void routerSelectBookAct(String areaName) {
        ARouter.getInstance().build(C.ROUTER_SELECTCLASS).withString("areaName", areaName).navigation();
    }

    public void routerUpdateVersionAct(long bookId){
        ARouter.getInstance().build(C.ROUTER_UPDATEVERSION).withLong("bookId",bookId).navigation();
    }

    public void routerWebAct(String url){
        ARouter.getInstance().build(C.ROUTER_WEB).withString("url",url).navigation();
    }
    public void routerWebAct(String url, String title){
        ARouter.getInstance().build(C.ROUTER_WEB)
                .withString("url",url)
                .withString("title", title)
                .navigation();
    }

    public void routerPayFeatureAct(){
        ARouter.getInstance().build(C.ROUTER_PAYFEATURE).navigation();
    }

    public void routerNoticeAct(){
        ARouter.getInstance().build(C.ROUTER_NOTICE).navigation();
    }

    public void routerSettingAct(){
        ARouter.getInstance().build(C.ROUTER_SETTING).navigation();
    }

    public void routerFeedBackAct(){
        ARouter.getInstance().build(C.ROUTER_FEEDBACK).navigation();
    }

    public void routerCustomerAct(){
        ARouter.getInstance().build(C.ROUTER_CUSTOMER).navigation();
    }

    public void routerReadDetailAct(String start, String end,String current){
        ARouter.getInstance().build(C.ROUTER_READ_DETAIL)
                .withString("start", start)
                .withString("current",current)
                .withString("end", end)
                .navigation();
    }

    public void routerListenAct() {
        ARouter.getInstance().build(C.ROUTER_LEARN_LISTEN).navigation();
    }

    public void routerRolePlayUnit(long unitId) {
        ARouter.getInstance().build(C.ROUTER_LEARN_ROLEPLAY_UNIT)
                .withLong("unitId", unitId)
                .navigation();
    }

    public void routerRolePlayAct() {
        ARouter.getInstance().build(C.ROUTER_LEARN_ROLEPLAY).navigation();
    }

    public void routerWordListenAct() {
        ARouter.getInstance().build(C.ROUTER_LEARN_WORDLISTEN).navigation();
    }

    public void routerWordListenDetailAct(long unitId) {
        ARouter.getInstance().build(C.ROUTER_LEARN_WORDLISTEN_DETAIL)
                .withLong("unitId", unitId).
                navigation();
    }

    public void routerBreakThrough(){
        ARouter.getInstance().build(C.ROUTER_LEARN_BREAK).navigation();
    }

    public void routerBreakSort() {
        ARouter.getInstance().build(C.ROUTER_LEARN_BREAK_SORT).navigation();
    }
}
