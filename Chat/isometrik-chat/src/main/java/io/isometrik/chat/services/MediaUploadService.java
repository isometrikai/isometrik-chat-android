package io.isometrik.chat.services;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Url;

/**
 * The interface media upload service containing method to upload media.
 */
public interface MediaUploadService {

  /**
   * Upload media call.
   *
   * @param url the url
   * @param contentType the content type
   * @param requestBody the request body
   * @return the call
   */
  //@Multipart
  @PUT
  Call<ResponseBody> uploadMedia(@Url String url, @Header("Content-Type") String contentType,
      @Body RequestBody requestBody);//  @Part MultipartBody.Part part);
}
