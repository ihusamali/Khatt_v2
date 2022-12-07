package com.husamali.khatt;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    Retrofit retrofit = null;

    Retrofit getClient(String url){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
