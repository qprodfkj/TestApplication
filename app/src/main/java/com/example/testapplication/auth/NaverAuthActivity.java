package com.example.testapplication.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.testapplication.GlobalApplication;
import com.example.testapplication.common.Constant;
import com.example.testapplication.login.SecondActivity;
import com.example.testapplication.util.L;
import com.example.testapplication.util.SSharedPrefHelper;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.data.OAuthLoginState;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NaverAuthActivity extends Activity {
    private static final String TAG = NaverAuthActivity.class.getSimpleName();
    final String NAVER_RESPONSE_CODE = "00"; // 정상 반환 시 코드
    final String[] NAVER_JSON_KEY = {"id", "email"};


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OAuthLogin mNaverLoginModule = OAuthLogin.getInstance();

        String accessToken = mNaverLoginModule.getAccessToken(getApplicationContext());
        SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, mNaverLoginModule.getRefreshToken(getApplicationContext()));
        if (accessToken != null && OAuthLoginState.OK.equals(mNaverLoginModule.getState(getApplicationContext()))) {
            ReqNHNUserInfo reqNaverUserInfo = new ReqNHNUserInfo();
            reqNaverUserInfo.execute(accessToken);
        } else {
            RefreshNHNToken tokenRefresh = new RefreshNHNToken();
            try {
                tokenRefresh.execute().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ReqNHNUserInfo reqNaverUserInfo = new ReqNHNUserInfo();
            reqNaverUserInfo.execute(mNaverLoginModule.getAccessToken(getApplicationContext()));
        }
    }

    class RefreshNHNToken extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                OAuthLogin mNaverLoginModule = OAuthLogin.getInstance();
                mNaverLoginModule.refreshAccessToken(getApplicationContext());
                L.d(TAG, "RefreshNHNToken refreshToken : " + mNaverLoginModule.getRefreshToken(getApplicationContext()));
            } catch (Exception e) {
                L.e("Error RefreshNHNToken", e.toString());
            }
            return true;
        }
    }

    class ReqNHNUserInfo extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... strings) {
            String token = strings[0];// 네이버 로그인 접근 토큰;
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                result = response.toString();
                br.close();
                L.d("ReqNHNUserInfo Response", result);
            } catch (Exception e) {
                L.e("Error ReqNHNUserInfo", e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(result);
                L.d(TAG, object.toString());
                if (object.getString("resultcode").equals(NAVER_RESPONSE_CODE)) {
                    List<String> userInfo = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(object.getString("response"));
                    userInfo.add(String.format("%s-%s", "NAVER", jsonObject.getString(NAVER_JSON_KEY[0])));
                    userInfo.add(jsonObject.getString(NAVER_JSON_KEY[1]));
                    GlobalApplication mGlobal = new GlobalApplication();
                    mGlobal.setGlobalUserLoginInfo(userInfo);

                    SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TYPE, Constant.AUTH_TYPE_NAVER);
                    //SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, accessToken.getRefreshToken());
                    SSharedPrefHelper.setSharedData(Constant.PREF.USER_EMAIL, NAVER_JSON_KEY[1]);

                    Intent intent = new Intent(NaverAuthActivity.this, SecondActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class DeleteTokenTask extends AsyncTask<Context, Void, Boolean> {
        Context context;
        SecondActivity secondActivity;

        public DeleteTokenTask(Context mContext, SecondActivity mActivity) {
            this.context = mContext;
            this.secondActivity = mActivity;
        }
        @Override
        protected Boolean doInBackground(Context... contexts) {
            return OAuthLogin.getInstance().logoutAndDeleteToken(contexts[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            secondActivity.directToMainActivity(result);
        }
    }

}
