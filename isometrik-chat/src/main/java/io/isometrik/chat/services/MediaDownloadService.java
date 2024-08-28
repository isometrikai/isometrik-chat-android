package io.isometrik.chat.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * The interface media download service with a method to download media from a url.
 */
public interface MediaDownloadService {
  /**
   * Download media async call.
   *
   * @param mediaUrl the media url
   * @return the call
   */
  @Streaming
  @GET
  Call<ResponseBody> downloadMediaAsync(@Url String mediaUrl);
}
