package com.jobfinder.network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

import static com.jobfinder.network.AppConfig.BASE_URL;

class ApiClient {

    //creating network calling reference
    private static Retrofit retrofit = null;
    private static ApiInterface apiInterface = null;

    private static Retrofit getClient() {
        if (retrofit == null) {
            //initializing retrofit
            retrofit = new Retrofit.Builder()
                    //setting base url
                    .baseUrl(BASE_URL)
                    //converting json to gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //setting timeout to 30s
                    .client(new OkHttpClient.Builder()
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .build())

                    .build();
        }
        return retrofit;
    }

    //getting POST/GET api interfaces
    static ApiInterface getApiInterface() {
        return apiInterface == null ? apiInterface = getClient().create(ApiInterface.class) : apiInterface;
    }
}

interface ApiInterface {

    //Creating GET request

    @GET
    Call<ResponseBody> get(@Url String endpoint);
}
