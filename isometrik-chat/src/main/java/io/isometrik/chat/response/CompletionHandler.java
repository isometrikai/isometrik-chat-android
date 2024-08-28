package io.isometrik.chat.response;

import io.isometrik.chat.response.error.IsometrikError;

/**
 * The interface Completion handler to handle the response of the remote query.
 *
 * @param <T> the type parameter
 */
public interface CompletionHandler<T> {

  /**
   * On complete.
   *
   * @param var1 the var 1
   * @param var2 the var 2
   */
  void onComplete(T var1, IsometrikError var2);
}


