package com.example.testapplication.auth;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.testapplication.GlobalApplication;
import com.example.testapplication.common.Constant;
import com.example.testapplication.login.LoginActivity;
import com.example.testapplication.util.L;
import com.example.testapplication.util.SSharedPrefHelper;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;
import java.util.List;

public class KakaoAuthActivity extends Activity {
    private static final String TAG = KakaoAuthActivity.class.getSimpleName();

    public static class KakaoSessionCallback implements ISessionCallback {
        private Context mContext;
        private LoginActivity loginActivity;

        public KakaoSessionCallback(Context context, LoginActivity activity) {
            this.mContext = context;
            this.loginActivity = activity;
        }

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(mContext, "KaKao 로그인 오류가 발생했습니다. " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        protected void requestMe() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    loginActivity.directToSecondActivity(false);
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    List userInfo = new ArrayList<>();
                    userInfo.add(String.valueOf(result.getId()));
                    userInfo.add(result.getKakaoAccount().getProfile().getNickname());
                    userInfo.add(result.getKakaoAccount().getEmail());
                    GlobalApplication mGlobalHelper = new GlobalApplication();
                    mGlobalHelper.setGlobalUserLoginInfo(userInfo);

                    AccessToken accessToken = Session.getCurrentSession().getTokenInfo();

                    L.d(TAG, "accessToken : " + accessToken.getAccessToken());
                    L.d(TAG, "refreshToken : " + accessToken.getRefreshToken());

                    SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TYPE, Constant.AUTH_TYPE_KAKAO);
                    SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, accessToken.getAccessToken());
                    SSharedPrefHelper.setSharedData(Constant.PREF.USER_EMAIL, result.getKakaoAccount().getEmail());

                    loginActivity.directToSecondActivity(true);
                }
            });
        }
    }
}
