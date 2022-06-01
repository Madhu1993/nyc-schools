package com.madhu.codeChal.nycschools.network;

import com.madhu.codeChal.nycschools.network.http_client.HttpClient;
import com.madhu.codeChal.nycschools.network.http_client.impl.HttpClientImpl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class TestHttpClient {

    public static HttpClient getHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        return new HttpClientImpl(okHttpClient);
    }

}
