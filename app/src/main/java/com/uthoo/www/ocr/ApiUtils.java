package com.uthoo.www.ocr;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://34.68.192.101";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
