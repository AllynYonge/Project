package com.king.reading.net.base;

import com.google.gson.Gson;
import com.king.reading.exception.ResponseValidateError;
import com.king.reading.mod.Header;
import com.king.reading.mod.Request;
import com.king.reading.net.Api;
import com.king.reading.net.RequestHeaderHelper;
import com.orhanobut.logger.Logger;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hu.yang on 2017/5/8.
 */

public abstract class BaseRequest<T> extends Request {

    private Api api;
    private Object[] params;
    public BaseRequest(Api api, Object... params){
        this.api = api;
        this.params = params;
    }

    public Single<T> sendRequest() {
        setBody(makeReqBinary(new TarsOutputStream(), params));
        setHeader(getHeader());
        Logger.d("header : " + new Gson().toJson(getHeader()));
        return getRequestSingle(api).subscribeOn(Schedulers.io())
                .filter(new Predicate<T>() {
                    @Override
                    public boolean test(@NonNull T t) throws Exception {
                        return validateRsp(t);
                    }
                }).switchIfEmpty(Maybe.create(new MaybeOnSubscribe<T>() {
                    @Override
                    public void subscribe(@NonNull MaybeEmitter<T> e) throws Exception {
                        e.onError(new ResponseValidateError("响应数据验证失败"));
                    }
                })).toSingle();
    }

    public Header getHeader(){
        return RequestHeaderHelper.getInstance().createHeader(getRequestName());
    }

    protected abstract byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params);

    /**
     * 对具体的数据进行验证
     */
    protected abstract boolean validateRsp(T rsp);

    protected abstract Single<T> getRequestSingle(Api api);

    protected abstract String getRequestName();

}
