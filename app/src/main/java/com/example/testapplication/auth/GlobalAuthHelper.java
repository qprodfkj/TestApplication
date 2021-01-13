package com.example.testapplication.auth;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.testapplication.GlobalApplication;
import com.example.testapplication.common.Constant;
import com.example.testapplication.login.SecondActivity;
import com.example.testapplication.util.L;
import com.example.testapplication.util.SSharedPrefHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.data.OAuthLoginState;

public class GlobalAuthHelper {
    private static final String TAG = GlobalAuthHelper.class.getSimpleName();

    public static void accountLogout(final SecondActivity activity) {
        String authType = SSharedPrefHelper.getSharedData(Constant.PREF.AUTH_TYPE);
        if(Constant.AUTH_TYPE_KAKAO.equals(authType)){
            //카카오 로그아웃
            L.d(TAG, "카카오 로그아웃");
            if (Session.getCurrentSession().checkAndImplicitOpen()) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TYPE, "");
                        SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, "");
                        activity.directToMainActivity(true);
                    }
                });
            }
        }else if(Constant.AUTH_TYPE_NAVER.equals(authType)){
            //네이버 로그아웃
            Context context = GlobalApplication.getInstance().getApplicationContext();
            if (OAuthLoginState.OK.equals(OAuthLogin.getInstance().getState(context))) {
                L.d(TAG, "네이버 로그아웃");
                SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TYPE, "");
                SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, "");
                OAuthLogin.getInstance().logout(context);
                activity.directToMainActivity(true);
            }
        } else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            L.d(TAG, "구글 로그아웃");
            SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TYPE, "");
            SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, "");
            Context context = GlobalApplication.getInstance().getApplicationContext();
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
            activity.directToMainActivity(true);
        }


    }

    public static void accountResign(final Context context, final SecondActivity activity) {
        String authType = SSharedPrefHelper.getSharedData(Constant.PREF.AUTH_TYPE);
        if(Constant.AUTH_TYPE_KAKAO.equals(authType)){
            // 카카오 연동 해제
            if (Session.getCurrentSession().checkAndImplicitOpen()) {
                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        if (errorResult.getErrorCode() == ApiErrorCode.CLIENT_ERROR_CODE) {
                            Toast.makeText(context, "네트워크가 불안정합니다.", Toast.LENGTH_SHORT).show();
                            L.d(TAG, "네트워크가 불안정합니다");
                            activity.directToMainActivity(false);
                        }
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Toast.makeText(context, "세션이 닫혀있습니다.", Toast.LENGTH_SHORT).show();
                        L.d(TAG, "세션이 닫혀있습니다");
                        activity.directToMainActivity(false);
                    }

                    @Override
                    public void onSuccess(Long result) {
                        SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TYPE, "");
                        SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, "");
                        Toast.makeText(context, "카카오톡 연동 해제", Toast.LENGTH_SHORT).show();
                        L.d(TAG, "카카오톡 연동 해제");
                        activity.directToMainActivity(true);
                    }
                });
            }
        }else if(Constant.AUTH_TYPE_NAVER.equals(authType)){
            //네이버 연동해제
            if (OAuthLoginState.OK.equals(OAuthLogin.getInstance().getState(context))) {
                // 네이버 연동 해제
                try {
                    NaverAuthActivity.DeleteTokenTask deleteTokenTask = new NaverAuthActivity.DeleteTokenTask(context, activity);
                    deleteTokenTask.execute(context).get();
                    SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TYPE, "");
                    SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, "");
                } catch (Exception e) {
                    e.printStackTrace();
                    activity.directToMainActivity(false);
                }

            }
        }else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // 구글 연동 해제
            try {
                FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            activity.directToMainActivity(true);
                        }
                        else {
                            activity.directToMainActivity(false);
                        }
                    }
                }); // Firebase 인증 해제
                GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).revokeAccess(); // Google 계정 해제
                SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TYPE, "");
                SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, "");
            } catch (Exception e) {
                activity.directToMainActivity(false);
            }
        }

    }
}
