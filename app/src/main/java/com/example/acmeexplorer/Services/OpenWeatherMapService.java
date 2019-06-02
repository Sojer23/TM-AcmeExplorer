package com.example.acmeexplorer.Services;

import com.example.acmeexplorer.Entities.CityWeather;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    //http://api.openweathermap.org/data/2.5/weather?q=madrid&APPID=5391617c203ef792feebc0037f3202ba
    @GET("data/2.5/weather")
    Call<CityWeather> cityWeatherInfo(@Query("q") String city, @Query("APPID") String appid);

    /*@GET("api/users/{userId}")
    Call<UserList> userSingle(@Path("userId") String userId);*/

}
