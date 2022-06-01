package com.madhu.codeChal.nycschools.data;

import com.madhu.codeChal.nycschools.data.base.AbstractWebRepo;
import com.madhu.codeChal.nycschools.domain.get_school_list_interactor.SchoolListRepo;
import com.madhu.codeChal.nycschools.domain.get_school_list_interactor.data.SchoolListResponse;
import com.madhu.codeChal.nycschools.entities.Borough;
import com.madhu.codeChal.nycschools.entities.School;
import com.madhu.codeChal.nycschools.network.api_url_provider.ApiUrlProvider;
import com.madhu.codeChal.nycschools.network.auth_token_provider.AuthTokenProvider;
import com.madhu.codeChal.nycschools.network.http_client.HttpClient;
import com.madhu.codeChal.nycschools.network.http_client.NetworkRequest;
import com.madhu.codeChal.nycschools.network.http_client.NetworkResponse;
import com.madhu.codeChal.nycschools.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;

public class SchoolListWebRepoImpl extends AbstractWebRepo implements SchoolListRepo {

    @Inject
    SchoolListWebRepoImpl(HttpClient httpClient, AuthTokenProvider authTokenProvider, ApiUrlProvider apiUrlProvider) {
        super(httpClient, authTokenProvider, apiUrlProvider);
    }

    @Override
    public Single<SchoolListResponse> getSchoolsByBorough(Borough borough) {
        String schoolListApi = apiUrlProvider.getSchoolListApi();
        schoolListApi += "?boro=" + borough.code;

        Map<String, String> authTokenHeaders = authTokenProvider.getAuthTokenHeaders();

        NetworkRequest networkRequest = NetworkRequest.createNetworkRequest(schoolListApi, authTokenHeaders);

        return httpClient.getJsonArray(networkRequest)
                .map(networkResponse -> parseSchoolListResponse(networkResponse, borough));
    }

    private static SchoolListResponse parseSchoolListResponse(NetworkResponse<JSONArray> networkResponse, Borough borough){
        JSONArray data = networkResponse.getData();

        if(!networkResponse.isSuccessful() || data == null){
            return SchoolListResponse.failure();
        }

        List<School> schoolList = new ArrayList<>();

        for(int i = 0; i < data.length(); i++){
            JSONObject jsonObject = data.optJSONObject(i);
            if(jsonObject == null){
                continue;
            }

            String dbn = jsonObject.optString("dbn");
            String schoolName = jsonObject.optString("school_name");
            String BoroughCode = jsonObject.optString("boro");

            if(!StringUtil.areStringsValid(dbn, schoolName, BoroughCode)){
                continue;
            }

            Borough boroughFromJson = Borough.fromCode(BoroughCode);

            if(boroughFromJson != borough){
                continue;
            }

            String webPageLink = jsonObject.optString("website");

            School school = School.newBuilder()
                    .borough(borough)
                    .dbn(dbn)
                    .name(schoolName)
                    .webpageLink(webPageLink)
                    .build();

            schoolList.add(school);
        }

        Collections.sort(schoolList, (sch1, sch2) -> sch1.getName().compareTo(sch2.getName()));

        return SchoolListResponse.success(schoolList, borough);
    }

}
