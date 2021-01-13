package com.example.testapplication.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testapplication.GlobalApplication;
import com.example.testapplication.R;
import com.example.testapplication.common.Constant;
import com.example.testapplication.login.LoginActivity;
import com.example.testapplication.login.SecondActivity;
import com.example.testapplication.util.L;
import com.example.testapplication.util.SSharedPrefHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class GoogleAuthActivity extends Activity {
    private static final String TAG = GoogleAuthActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleClient;
    private FirebaseAuth mGoogleLoginModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate");
        mGoogleLoginModule = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        if (mGoogleLoginModule.getCurrentUser() == null) {
            Intent signInIntent = mGoogleClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else if (mGoogleLoginModule.getCurrentUser() != null) {
            /*
            mGoogleLoginModule.getCurrentUser().getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        L.d(TAG, "google token : " + idToken);
                    }
                }
            });
            */
            List<String> userInfo = new ArrayList<>();
            userInfo.add(String.format("%s-%s", "GOOGLE", mGoogleLoginModule.getCurrentUser().getProviderId()));
            userInfo.add(mGoogleLoginModule.getCurrentUser().getDisplayName());
            userInfo.add(mGoogleLoginModule.getCurrentUser().getEmail());
            GlobalApplication.setGlobalUserLoginInfo(userInfo);
            Intent intent = new Intent(GoogleAuthActivity.this, SecondActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(getApplicationContext(), "Google Sign In Failed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GoogleAuthActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mGoogleLoginModule.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mGoogleLoginModule.getCurrentUser() != null) {
                                List<String> userInfo = new ArrayList<>();
                                userInfo.add(mGoogleLoginModule.getCurrentUser().getProviderId());
                                userInfo.add(mGoogleLoginModule.getCurrentUser().getDisplayName());
                                userInfo.add(mGoogleLoginModule.getCurrentUser().getEmail());

                                mGoogleLoginModule.getCurrentUser().getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        if (task.isSuccessful()) {
                                            String idToken = task.getResult().getToken();
                                            SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TOKEN, idToken);
                                        }
                                    }
                                });

                                SSharedPrefHelper.setSharedData(Constant.PREF.AUTH_TYPE, Constant.AUTH_TYPE_GOOGLE);
                                SSharedPrefHelper.setSharedData(Constant.PREF.USER_EMAIL, mGoogleLoginModule.getCurrentUser().getEmail());
                                GlobalApplication.setGlobalUserLoginInfo(userInfo);
                                Intent intent = new Intent(GoogleAuthActivity.this, SecondActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Google Authentication Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
