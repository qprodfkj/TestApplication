package com.example.testapplication.auth;

import android.content.Context;
import android.widget.Toast;

import com.example.testapplication.login.SecondActivity;
import com.example.testapplication.util.L;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

public class GlobalAuthHelper {
    private static final String TAG = GlobalAuthHelper.class.getSimpleName();

    public static void accountLogout(final SecondActivity activity) {
        if (Session.getCurrentSession().checkAndImplicitOpen()) {
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    L.d(TAG, "onCompleteLogout");
                    activity.directToMainActivity(true);
                }
            });
        }
    }

    public static void accountResign(final Context context, final SecondActivity activity) {
        if (Session.getCurrentSession().checkAndImplicitOpen()) {
            // 카카오 연동 해제
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
                    Toast.makeText(context, "카카오톡 연동 해제", Toast.LENGTH_SHORT).show();
                    L.d(TAG, "카카오톡 연동 해제");
                    activity.directToMainActivity(true);
                }
            });
        }
    }
}
