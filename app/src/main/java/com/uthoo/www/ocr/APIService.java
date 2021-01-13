package com.uthoo.www.ocr;

import android.provider.SyncStateContract;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIService {



   // @POST("/api/v1/match?data=Lines%3A%20%0AKAMAE3L1r%0ABr.%20Pujung%20Kelod%2C%0Aebatu%2C%20Tegallaiang%2C%2061anyar%2C%0ATelephone%20(%2B62)%20361%20901366%0AEtnail%20kumu.lilir%40gnail.%20COn%0A9ebsiteW.kuEulilir%2Cid%0ADate%0A2020-10-31%2014%3A59%3A57%0ANo.transaction120103i0012%0A166%20Htester%20(L)%0A2%20x%2025.000%20%3D%2050.000%0A173%20Hfrench%20fries%20kids%20(L)%0A2x%20%4010.000%2020.000%0A130%20Hbanana%20fritter%20(%20L%0A3X15.000%20%3D%2045.000%0APaynent%20Type%3A%20Cash%0AJunlah%20Iten%3A%207%20buah%0ATotal%20113.000%0ADiscount%3A%0ATax%26Service%2017.300%0AGrand%20Total%0A132.3%20300%0A135.000%0A700%0APayment%0Anange%0AItens%20that%20have%20been%20purchased%20cannot%20be%0Areturned%0AThank%20You")
    //@FormUrlEncoded
    //Call<String> getStringScalar(@Field("result")String result);
    //Call<List<String>> getStringScalar(@Field("title") List<String> titles);
 //  @GET("/api/v1/match?data=Lines%3A%20%0AKAMAE3L1r%0ABr.%20Pujung%20Kelod%2C%0Aebatu%2C%20Tegallaiang%2C%2061anyar%2C%0ATelephone%20(%2B62)%20361%20901366%0AEtnail%20kumu.lilir%40gnail.%20COn%0A9ebsiteW.kuEulilir%2Cid%0ADate%0A2020-10-31%2014%3A59%3A57%0ANo.transaction120103i0012%0A166%20Htester%20(L)%0A2%20x%2025.000%20%3D%2050.000%0A173%20Hfrench%20fries%20kids%20(L)%0A2x%20%4010.000%2020.000%0A130%20Hbanana%20fritter%20(%20L%0A3X15.000%20%3D%2045.000%0APaynent%20Type%3A%20Cash%0AJunlah%20Iten%3A%207%20buah%0ATotal%20113.000%0ADiscount%3A%0ATax%26Service%2017.300%0AGrand%20Total%0A132.3%20300%0A135.000%0A700%0APayment%0Anange%0AItens%20that%20have%20been%20purchased%20cannot%20be%0Areturned%0AThank%20You")
//    Call<String> getStringScalar( @Path("result") String result);
//    @Multipart
 //   @POST("/upload")
  //  Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);
   @GET("match")
   Call<ResponseBody> getStoryOfMe(@QueryMap HashMap<String, String> params);







}


