package com.madhu.codeChal.nycschools.data.base;

import com.madhu.codeChal.nycschools.network.api_url_provider.ApiUrlProvider;
import com.madhu.codeChal.nycschools.network.auth_token_provider.AuthTokenProvider;
import com.madhu.codeChal.nycschools.network.http_client.HttpClient;

public abstract class AbstractWebRepo {

    protected final HttpClient httpClient;
    protected final AuthTokenProvider authTokenProvider;
    protected final ApiUrlProvider apiUrlProvider;

    public AbstractWebRepo(HttpClient httpClient, AuthTokenProvider authTokenProvider, ApiUrlProvider apiUrlProvider) {
        this.httpClient = httpClient;
        this.authTokenProvider = authTokenProvider;
        this.apiUrlProvider = apiUrlProvider;
    }

}
