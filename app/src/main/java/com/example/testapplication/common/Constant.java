package com.example.testapplication.common;

public class Constant {

    //naver client id
    public static final String NAVER_CLIENT_ID = "iE0A_gTYRq7L4beLyd_h";
    //naver client Secret
    public static final String NAVER_CLIENT_SECRET = "jcvcxrg3hI";

    //auth type
    public static final String AUTH_TYPE_KAKAO = "K";
    public static final String AUTH_TYPE_NAVER = "N";
    public static final String AUTH_TYPE_GOOGLE = "G";

    /* ============================== */
    /*  Shared Pref key	 	          */
    /* ============================== */
    public static final class PREF {

        // 계정 연동 종류(카카오, 네이버) - K: 카카오, N : 네이버
        public static final String AUTH_TYPE = "AUTH_TYPE";
        public static final String AUTH_TOKEN = "AUTH_TOKEN";
        public static final String USER_EMAIL = "USER_EMAIL";
    }


}
