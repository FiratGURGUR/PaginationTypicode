package com.developer.paginationtypicode;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserClient {



    @GET("?postId=")
    Call<ResponseBody> getJsonData(@Query("postId") String text);


}
