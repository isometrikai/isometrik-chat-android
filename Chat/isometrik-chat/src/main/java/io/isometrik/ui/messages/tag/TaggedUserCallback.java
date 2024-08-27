package io.isometrik.ui.messages.tag;

/**
 * The interface tagged user callback on click.
 */
public interface TaggedUserCallback {

  /**
   * On tagged user clicked.
   *
   * @param memberId the member id
   */
  void onTaggedUserClicked(String memberId);
}
