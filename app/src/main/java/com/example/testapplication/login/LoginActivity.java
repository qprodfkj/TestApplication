package com.example.testapplication.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.R;
import com.example.testapplication.auth.GoogleAuthActivity;
import com.example.testapplication.auth.KakaoAuthActivity.KakaoSessionCallback;
import com.example.testapplication.auth.NaverAuthActivity;
import com.example.testapplication.common.Constant;
import com.example.testapplication.util.L;
import com.example.testapplication.util.SSharedPrefHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.data.OAuthLoginState;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private OAuthLogin mNaverLoginModule;
    @BindView(R.id.btn_kakao_login) Button mKakaoLoginBtn;
    @BindView(R.id.btn_kakao_login_basic) LoginButton mKakaoLoginBtnBasic;
    @BindView(R.id.btn_naver_login) Button mNaverLoginBtn ;
    @BindView(R.id.btn_naver_login_basic) OAuthLoginButton mNaverLoginBtnBasic;
    @BindView(R.id.btn_google_login) Button mGoogleLoginBtn ;
    private KakaoSessionCallback sessionCallback;
    private FirebaseAuth mGoogleLoginModule;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate");
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mNaverLoginBtnBasic.setOAuthLoginHandler(mNaverLoginHandler);
        mNaverLoginModule = OAuthLogin.getInstance();
        mNaverLoginModule.init(
                this
                , Constant.NAVER_CLIENT_ID
                , Constant.NAVER_CLIENT_SECRET
                , "TestApp"
        );

        String authType = SSharedPrefHelper.getSharedData(Constant.PREF.AUTH_TYPE);
        L.d(TAG, "AUTH_TYPE : " + authType);

        mGoogleLoginModule = FirebaseAuth.getInstance();

        if (!HasKakaoSession() && !HasNaverSession() && !HasGoogleSession()) {
            sessionCallback = new KakaoSessionCallback(getApplicationContext(), LoginActivity.this);
            Session.getCurrentSession().addCallback(sessionCallback);
        } else if (HasKakaoSession()) {
            sessionCallback = new KakaoSessionCallback(getApplicationContext(), LoginActivity.this);
            Session.getCurrentSession().addCallback(sessionCallback);
            Session.getCurrentSession().checkAndImplicitOpen();
        } else if (HasNaverSession()) {
            Intent intent = new Intent(LoginActivity.this, NaverAuthActivity.class);
            startActivity(intent);
            finish();
        }else if (HasGoogleSession()) {
            Intent intent = new Intent(LoginActivity.this, GoogleAuthActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mNaverLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Intent intent = new Intent(LoginActivity.this, NaverAuthActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Naver Login Failed!", Toast.LENGTH_LONG);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @OnClick({R.id.btn_kakao_login, R.id.btn_naver_login, R.id.btn_google_login})
    public void onClickView(View view){
        switch(view.getId()){
            case R.id.btn_kakao_login:
                mKakaoLoginBtnBasic.performClick();
                break;
            case R.id.btn_naver_login:
                mNaverLoginBtnBasic.performClick();
                break;
            case R.id.btn_google_login:
                Intent intent = new Intent(LoginActivity.this, GoogleAuthActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private boolean HasKakaoSession() {
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            return false;
        }
        return true;
    }

    private boolean HasNaverSession() {
        if (OAuthLoginState.NEED_LOGIN.equals(mNaverLoginModule.getState(getApplicationContext())) ||
                OAuthLoginState.NEED_INIT.equals(mNaverLoginModule.getState(getApplicationContext()))) {
            return false;
        }
        return true;
    }

    private boolean HasGoogleSession() {
        if (mGoogleLoginModule.getCurrentUser() == null) {
            return false;
        }
        return true;
    }

    public void directToSecondActivity(Boolean result) {
        Intent intent = new Intent(LoginActivity.this, SecondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (result) {
            Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }
    }


}
