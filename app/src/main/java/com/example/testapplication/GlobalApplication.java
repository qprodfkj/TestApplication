package com.example.testapplication;

import android.app.Application;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import java.util.ArrayList;
import java.util.List;

public class GlobalApplication extends Application {
    private static GlobalApplication globalApplication = null;
    private static List<String> mGlobalUserLoginInfo = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        globalApplication = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static GlobalApplication getInstance() {
        return globalApplication;
    }

    public static List<String> getGlobalUserLoginInfo() {
        return mGlobalUserLoginInfo;
    }

    public static void setGlobalUserLoginInfo(List<String> userLoginInfo) {
        mGlobalUserLoginInfo = userLoginInfo;
    }

    public class KakaoSDKAdapter extends KakaoAdapter{

        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_ACCOUNT};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getInstance();
                }
            };
        }
    }

}
