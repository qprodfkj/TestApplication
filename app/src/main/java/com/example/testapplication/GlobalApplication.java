package com.example.testapplication;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;

    public static GlobalApplication getGlobalApplicationContext() {
        if (instance == null) {
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, getString(R.string.kakao_app_key));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
