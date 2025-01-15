package io.isometrik.ui.messages.media;

import io.isometrik.chat.utils.enums.MessageTypeUi;

/**
 * The interface Media type to be shared callback to messages screen based on message type selected
 * to be shared in ShareMediaFragment.
 */
public interface MediaTypeToBeSharedCallback {

  /**
   * On media type to be shared selected.
   *
   * @param mediaTypeSelected the media type selected
   */
  void onMediaTypeToBeSharedSelected(MessageTypeUi mediaTypeSelected);
}
