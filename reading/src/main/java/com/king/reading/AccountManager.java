package com.king.reading;

import android.accounts.Account;
import android.content.Context;

import io.reactivex.Single;

/**
 * Created by hu.yang on 2017/5/15.
 */

public class AccountManager {
    //private final Api api;
    private static AccountManager mInstance = null;
    private static Single<Account> mAccountEntity;
    private Context mContext;

    public static AccountManager getInstance() {
        if (mInstance == null) {
            synchronized (AccountManager.class) {
                if (mInstance == null) {
                    mInstance = new AccountManager();
                }
            }
        }
        return mInstance;
    }

    /*private AccountManager() {
        this.api = NetManager.getInstance().getRetrofit().create(Api.class);
        mContext = AppUtils.getApplicationUsingReflection();
    }

    public Single<Account> getAccount(String userName, String password) {
        LoginReq request = new LoginReq(api, userName, password);
        return request.sendRequest()
                .map(new Function<LoginResponse, Account>() {
                    @Override
                    public Account apply(@NonNull LoginResponse loginRsp) throws Exception {
                        return *//*AccountMapper.transferSchool(loginRsp)*//*null;
                    }
                })
                .doOnSuccess(new Consumer<Account>() {
                    @Override
                    public void accept(@NonNull Account accountEntity) throws Exception {
                        AccountSQLiteHelper.insertAccount(mContext, accountEntity);
                        mAccountEntity = Single.just(accountEntity);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> logout() {
        final ContentValues values = new ContentValues();
        values.put("isLogin", 0);
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                e.onSuccess(AccountSQLiteHelper.updateAccountInfo(mContext, values, "isLogin", 1+""));
            }
        }).subscribeOn(Schedulers.io())
        .doOnSuccess(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean isLogout) throws Exception {
                if (isLogout) mInstance = null;
            }
        });
    }

    public Single<Account> getLoginAccount() {
        if (mAccountEntity == null) {
            mAccountEntity = Single.create(new SingleOnSubscribe<Account>() {
                @Override
                public void subscribe(@NonNull SingleEmitter<Account> e) throws Exception {
                    e.onSuccess(AccountSQLiteHelper.queryForCondition(mContext, "isLogin", 1 + ""));
                }
            })
            .subscribeOn(Schedulers.io())
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    Logger.e(throwable, "query db account info error");
                }
            });
        }
        return mAccountEntity;
    }*/



    /*public Single<Account> getLoginAccount() {
        return RXSQLite.rx(SQLite.select()
                .from(Account.class)
                .where(Account_Table.isLogin.is(true)))
                .querySingle()
                .subscribeOn(Schedulers.io());
    }

    public Single<Account> getAccountForId(long accountId) {
        return RXSQLite.rx(SQLite.select()
                .from(Account.class)
                .where(Account_Table.accountId.is(accountId)))
                .querySingle()
                .subscribeOn(Schedulers.io());
    }*/

}
