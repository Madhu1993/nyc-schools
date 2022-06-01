package com.madhu.codeChal.nycschools.data;


import com.madhu.codeChal.nycschools.domain.get_sat_score_interactor.SatScoreDataRepo;
import com.madhu.codeChal.nycschools.domain.get_sat_score_interactor.data.SatDataResponse;
import com.madhu.codeChal.nycschools.network.FakeApiUrlProvider;
import com.madhu.codeChal.nycschools.network.TestHttpClient;
import com.madhu.codeChal.nycschools.network.api_url_provider.ApiUrlProvider;
import com.madhu.codeChal.nycschools.network.auth_token_provider.AuthTokenProvider;
import com.madhu.codeChal.nycschools.network.http_client.HttpClient;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class SatScoreDataRepoImplTest {

    private HttpClient httpClient;
    private AuthTokenProvider authTokenProvider;
    private ApiUrlProvider apiUrlProvider;

    @Before
    public void setUp() {
        httpClient = TestHttpClient.getHttpClient();

        authTokenProvider = HashMap::new;
        apiUrlProvider = FakeApiUrlProvider.getApiUrlProvider();
    }

    @Test
    public void getSatScores() {
        SatScoreDataRepo satScoreDataRepo = new SatScoreDataRepoImpl(httpClient, authTokenProvider, apiUrlProvider);

        String dbn = "01M292";

        satScoreDataRepo.getSatScoreDataByDbn(dbn)
                .test()
                .assertValue(SatDataResponse::isSuccessful)
                .assertValue(satDataResponse -> satDataResponse.getSatScoreData().getDbn().equals(dbn))
                .assertValue(satDataResponse -> satDataResponse.getSatScoreData().isDataAvailable())
                .assertComplete();

    }

    @Test
    public void getSatScoresNoDataForSchool() {
        SatScoreDataRepo satScoreDataRepo = new SatScoreDataRepoImpl(httpClient, authTokenProvider, apiUrlProvider);

        String dbn = "03M417";

        satScoreDataRepo.getSatScoreDataByDbn(dbn)
                .test()
                .assertValue(SatDataResponse::isSuccessful)
                .assertValue(satDataResponse -> satDataResponse.getSatScoreData().getDbn().equals(dbn))
                .assertValue(satDataResponse -> !satDataResponse.getSatScoreData().isDataAvailable())
                .assertComplete();

    }

    @Test
    public void getSatScoresServerResponseWithEmptyArr() {
        SatScoreDataRepo satScoreDataRepo = new SatScoreDataRepoImpl(httpClient, authTokenProvider, apiUrlProvider);

        String dbn = "02M546";

        satScoreDataRepo.getSatScoreDataByDbn(dbn)
                .test()
                .assertValue(SatDataResponse::isSuccessful)
                .assertValue(satDataResponse -> satDataResponse.getSatScoreData().getDbn().equals(dbn))
                .assertValue(satDataResponse -> !satDataResponse.getSatScoreData().isDataAvailable())
                .assertComplete();

    }



}