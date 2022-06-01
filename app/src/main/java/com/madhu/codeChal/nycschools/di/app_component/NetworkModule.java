package com.madhu.codeChal.nycschools.di.app_component;

import com.madhu.codeChal.nycschools.network.api_url_provider.ApiUrlProvider;
import com.madhu.codeChal.nycschools.network.auth_token_provider.AuthTokenProvider;
import com.madhu.codeChal.nycschools.network.http_client.HttpClient;
import com.madhu.codeChal.nycschools.network.http_client.impl.HttpClientImpl;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class NetworkModule {

    @AppScope
    @Provides
    static OkHttpClient okHttpClient(){
       return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @AppScope
    @Provides
    static HttpClient httpClient(HttpClientImpl httpClient){
        return httpClient;
    }

    @AppScope
    @Provides
    static AuthTokenProvider authTokenProvider(){
        return HashMap::new;
    }

    @AppScope
    @Provides
    static ApiUrlProvider apiUrlProvider(){
        return new ApiUrlProvider() {
            @Override
            public String getSchoolListApi() {
                return "https://data.cityofnewyork.us/resource/s3k6-pzi2.json";
            }

            @Override
            public String getSchoolSatApi() {
                return "https://data.cityofnewyork.us/resource/f9bf-2cp4.json";
            }
        };
    }


}
