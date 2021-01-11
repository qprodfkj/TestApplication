package com.example.testapplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import com.example.testapplication.util.L;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.btnLoginKaKao) Button btnLoginKaKao;
    @BindView(R.id.btnLoginNaver) Button btnLoginNaver;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        //FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //getAppKeyHash();
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                L.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            L.e("name not found", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnLoginKaKao)
    public void onClickView(View view){
        switch(view.getId()){
            case R.id.btnLoginKaKao:
                L.d(TAG, "카카오 클릭");
                /*
                LoginClient.getInstance().loginWithKakaoTalk(mContext, (token, loginError) -> {
                    if(loginError != null){
                        L.e(TAG, "로그인 실패 - " + loginError);
                    }else{
                        L.d(TAG, "로그인 성공");

                        UserApiClient.getInstance().me((user, meError) ->{
                            if(meError != null){
                                L.e(TAG, "사용자 정보 요청 실패 - " + meError);
                            }else{
                                L.i(TAG, user.toString());
                            }
                            return null;
                        });
                    }
                    return null;
                });
                 */

                /*
                LoginClient.getInstance().loginWithKakaoAccount(mContext, (token, loginError) -> {
                    if(loginError != null){
                        L.e(TAG, "로그인 실패 - " + loginError);
                    }else{
                        L.d(TAG, "로그인 성공 token - " + token);

                        UserApiClient.getInstance().me((user, meError) ->{
                            if(meError != null){
                                L.e(TAG, "사용자 정보 요청 실패 - " + meError);
                            }else{
                                L.i(TAG, user.toString());
                            }
                            return null;
                        });
                    }
                    return null;
                });
                */

                break;
        }

    }

}