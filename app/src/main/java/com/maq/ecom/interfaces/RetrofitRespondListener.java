package com.maq.ecom.interfaces;


import retrofit2.Response;

/**
 * This interface contains common methods which are invoked by retrofit GET/POST json request function calls.
 */


public interface RetrofitRespondListener {

    void onRetrofitSuccess(Response<?> response, String requestName);

    void onRetrofitFailure(String responseError, String requestName);
}
