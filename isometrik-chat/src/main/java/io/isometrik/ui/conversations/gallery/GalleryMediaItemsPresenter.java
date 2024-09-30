package io.isometrik.ui.conversations.gallery;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.message.FetchMessagesQuery;
import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The presenter to fetch list of media items in gallery with paging, search and pull to refresh
 * option.
 */
public class GalleryMediaItemsPresenter implements GalleryMediaItemsContract.Presenter {

  /**
   * Instantiates a new Gallery media items presenter.
   *
   * @param galleryMediaItemsView the gallery media items view
   */
  GalleryMediaItemsPresenter(GalleryMediaItemsContract.View galleryMediaItemsView) {
    this.galleryMediaItemsView = galleryMediaItemsView;
  }

  private final GalleryMediaItemsContract.View galleryMediaItemsView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();
  private String conversationId;
  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void initialize(String conversationId) {
    this.conversationId = conversationId;
  }

  @Override
  public void fetchGalleryMediaItems(String customType, int skip, boolean onScroll,
      boolean isSearchRequest, String searchTag) {
    isLoading = true;

    if (skip == 0) {
      isLastPage = false;
      offset = 0;
    }

    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchMessagesQuery.Builder fetchMessagesQuery =
        new FetchMessagesQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setLimit(Constants.GALLERY_ITEMS_MEDIA_PAGE_SIZE)
            .setSkip(skip)
            .setCustomTypes(Collections.singletonList(customType))
            .setSort(Constants.SORT_ORDER_DSC);

    if (isSearchRequest && searchTag != null) {
      fetchMessagesQuery.setSearchTag(searchTag);
    }

    isometrik.getRemoteUseCases()
        .getMessageUseCases()
        .fetchMessages(fetchMessagesQuery.build(), (var1, var2) -> {
          isLoading = false;
          if (var1 != null) {

            ArrayList<Message> messages = var1.getMessages();

            ArrayList<GalleryModel> galleryModels = new ArrayList<>();
            int size = messages.size();

            for (int i = 0; i < size; i++) {

              galleryModels.add(new GalleryModel(messages.get(i)));
            }
            if (size < Constants.GALLERY_ITEMS_MEDIA_PAGE_SIZE) {

              isLastPage = true;
            }

        galleryMediaItemsView.onGalleryMediaItemsFetchedSuccessfully(galleryModels, onScroll);
      } else {
        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
          if (!onScroll) {
            //No attachments found
            galleryMediaItemsView.onGalleryMediaItemsFetchedSuccessfully(new ArrayList<>(), false);
          } else {
            isLastPage = true;
          }
        } else {
          galleryMediaItemsView.onError(var2.getErrorMessage());
        }
      }
    });
  }

  @Override
  public void fetchGalleryMediaItemsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount, String customType) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= Constants.GALLERY_ITEMS_MEDIA_PAGE_SIZE) {

        offset++;
        fetchGalleryMediaItems(customType, offset * Constants.GALLERY_ITEMS_MEDIA_PAGE_SIZE, true,
            isSearchRequest, searchTag);
      }
    }
  }
}