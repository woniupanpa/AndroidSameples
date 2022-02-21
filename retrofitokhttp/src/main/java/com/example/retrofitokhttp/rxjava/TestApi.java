package com.example.retrofitokhttp.rxjava;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author yanjim
 * @Date 2022/1/10 08:49
 */
public interface TestApi {
    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);

    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);

    @POST("/")
    Observable<ResponseBody> transaction(@Body RequestBody body);

    @POST
    Observable<ResponseBody> postWithoutEncryption(@Url String url, @Header("Authorization") String authorization, @Body RequestBody body);
}
