package com.example.testapplication.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.GlobalApplication;
import com.example.testapplication.MainActivity;
import com.example.testapplication.R;
import com.example.testapplication.auth.GlobalAuthHelper;
import com.example.testapplication.util.L;
import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = SecondActivity.class.getSimpleName();

    @BindView(R.id.tv_second_userid) TextView tvSecondUserID;
    @BindView(R.id.tv_second_nickname) TextView tvSecondNickname;
    @BindView(R.id.btn_second_logout) Button btnSecondLogout;
    @BindView(R.id.btn_second_resign) Button btnSecondResign;

    private List userInfo = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

        initView();

        btnSecondLogout.setOnClickListener(mLogoutListener);
        btnSecondResign.setOnClickListener(mResignListener);
    }

    Button.OnClickListener mLogoutListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            GlobalAuthHelper.accountLogout(SecondActivity.this);
        }
    };

    Button.OnClickListener mResignListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            GlobalAuthHelper.accountResign(getApplicationContext(), SecondActivity.this);
        }
    };

    private void initView() {
        GlobalApplication mGlobalHelper = new GlobalApplication();
        userInfo = mGlobalHelper.getGlobalUserLoginInfo();
        tvSecondUserID.setText("UserId : " + userInfo.get(0));
        if(userInfo.size()>2){
            tvSecondNickname.setText("Nickname : " + userInfo.get(1) + "\n email : " + userInfo.get(2));
        }else{
            tvSecondNickname.setText("Nickname : " + userInfo.get(1));
        }
    }

    public void directToMainActivity(Boolean result) {
        L.d(TAG, "directToMainActivity result : " + result);
        Intent intent = new Intent(SecondActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
