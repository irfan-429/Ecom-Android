package com.maq.ecom.networking;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.maq.ecom.helper.Utils;
import com.maq.ecom.interfaces.RetrofitRespondListener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by irfan A.
 */

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static <T> void callRetrofit(Call<T> call, final String requestName, final RetrofitRespondListener listener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                listener.onRetrofitSuccess(response, requestName);
                Log.e(requestName + "=> RESPONSE_CODE", new Gson().toJson(response.code()));
                try {
                    if (response.isSuccessful())
                        Log.i(requestName + "=> SUCCESS", new Gson().toJson(response.body()));
                    else
                        Log.e(requestName + "=> UN_SUCCESS", new Gson().toJson(response.errorBody().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable throwable) {
                listener.onRetrofitFailure(throwable.getMessage(), requestName);
                Log.e(requestName + "=> FAILED", throwable.getMessage());
            }
        });
    }
}