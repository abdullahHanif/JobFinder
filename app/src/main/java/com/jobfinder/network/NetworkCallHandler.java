package com.jobfinder.network;

import android.util.Log;

import com.jobfinder.utils.Constants;
import com.jobfinder.utils.Utils;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCallHandler {

    public static void get(String endpoint, final Callback<ResponseBody> callback) {
        Log.d(Constants.TAG, "get: " + endpoint);

        Call<ResponseBody> call = ApiClient.getApiInterface().get(endpoint);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Utils.AppProgressDialog.dismiss();
                handleOnResponse(call, response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Utils.AppProgressDialog.dismiss();
                Log.d(Constants.TAG, "failResponse: " + call);
                handleOnFailResponse(call, t, callback);
            }
        });
    }

    //network call failure response
    private static void handleOnFailResponse(Call<ResponseBody> call, Throwable t, Callback<ResponseBody> callback) {
        callback.onFailure(call, t);
    }

    //network call success response
    private static void handleOnResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response, Callback callback) {
        int code = response.code();
        switch (code) {
            case 200:
                callback.onResponse(call, response);
                break;
            case 201:
                callback.onResponse(call, response);
                break;
            case 500:
                handle500Error(response);
                break;
            default:
                handleUnknownError(response);
                break;
        }
    }

    private static void handle500Error(Response<ResponseBody> response) {
        Log.d(Constants.TAG, "Server Error 500: " + response);
    }

    private static void handleUnknownError(Response<ResponseBody> response) {
        Log.d(Constants.TAG, "Unknown Error: " + response.code() + " : " + response);
    }

}
