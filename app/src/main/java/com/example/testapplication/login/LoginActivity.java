package com.example.testapplication.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.MainActivity;
import com.example.testapplication.R;
import com.example.testapplication.auth.KakaoAuth.KakaoSessionCallback;
import com.example.testapplication.common.Constant;
import com.example.testapplication.util.L;
import com.example.testapplication.util.SSharedPrefHelper;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();


    @BindView(R.id.btn_kakao_login) Button mKakaoLoginBtn;
    @BindView(R.id.btn_kakao_login_basic) LoginButton mKakaoLoginBtnBasic;
    private KakaoSessionCallback sessionCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate");
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        String authType = SSharedPrefHelper.getSharedData(Constant.PREF.AUTH_TYPE);
        L.d(TAG, "AUTH_TYPE : " + authType);

        if(Constant.AUTH_TYPE_KAKAO.equals(authType)){
            //카카오 연동시
            if (!HasKakaoSession()) {
                sessionCallback = new KakaoSessionCallback(getApplicationContext(), LoginActivity.this);
                Session.getCurrentSession().addCallback(sessionCallback);
            } else if (HasKakaoSession()) {
                sessionCallback = new KakaoSessionCallback(getApplicationContext(), LoginActivity.this);
                Session.getCurrentSession().addCallback(sessionCallback);
                Session.getCurrentSession().checkAndImplicitOpen();
            }
        }else if(Constant.AUTH_TYPE_NAVER.equals(authType)){
            //네이버 연동시
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @OnClick(R.id.btn_kakao_login)
    public void onClickView(View view){
        switch(view.getId()){
            case R.id.btn_kakao_login:
                mKakaoLoginBtnBasic.performClick();
                break;
        }
    }

    private boolean HasKakaoSession() {
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
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
