package io.isometrik.chat.models.upload.utils;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import org.jetbrains.annotations.NotNull;

/**
 * The custom request body for upload progress request.
 */
public class UploadProgressRequestBody extends RequestBody {

  /**
   * The Delegate.
   */
  protected RequestBody delegate;
  /**
   * The Upload progress listener.
   */
  protected UploadProgressListener uploadProgressListener;
  /**
   * The Counting sink.
   */
  protected CountingSink countingSink;

  private final String requestId;
  private final String requestGroupId;

  /**
   * Instantiates a new Upload progress request body.
   *
   * @param delegate the delegate
   * @param uploadProgressListener the upload progress listener
   * @param requestId the request id
   * @param requestGroupId the request group id
   */
  public UploadProgressRequestBody(RequestBody delegate,
      UploadProgressListener uploadProgressListener, String requestId,String requestGroupId) {
    this.delegate = delegate;
    this.uploadProgressListener = uploadProgressListener;
    this.requestId = requestId;
    this.requestGroupId=requestGroupId;
  }

  @Override
  public MediaType contentType() {
    return delegate.contentType();
  }

  @Override
  public long contentLength() {
    try {
      return delegate.contentLength();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return -1;
  }

  @Override
  public void writeTo(@NotNull BufferedSink sink) throws IOException {

    countingSink = new CountingSink(sink);
    BufferedSink bufferedSink = Okio.buffer(countingSink);

    delegate.writeTo(bufferedSink);

    bufferedSink.flush();
  }

  /**
   * The type Counting sink.
   */
  protected final class CountingSink extends ForwardingSink {

    private long bytesWritten = 0;

    /**
     * Instantiates a new Counting sink.
     *
     * @param delegate the delegate
     */
    public CountingSink(Sink delegate) {
      super(delegate);
    }

    @Override
    public void write(@NotNull Buffer source, long byteCount) throws IOException {
      super.write(source, byteCount);

      bytesWritten += byteCount;
      uploadProgressListener.onUploadProgress(requestId, requestGroupId, bytesWritten, contentLength());
    }
  }
}