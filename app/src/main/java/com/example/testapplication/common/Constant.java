package com.example.testapplication.common;

public class Constant {

    //auth type
    public static final String AUTH_TYPE_KAKAO = "K";
    public static final String AUTH_TYPE_NAVER = "N";

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