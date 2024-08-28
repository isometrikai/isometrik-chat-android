package io.isometrik.ui.conversations.gallery;

import io.isometrik.chat.utils.enums.CustomMessageTypes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The helper class to decide which all media types to be shown in header on gallery items page.
 */
public class GalleryMediaItemsSettingsUtil implements Serializable {

  private final boolean includeImages, includeVideos, includeAudioRecordings, includeFiles,
      includeWhiteboards, includeStickers, includeGifs, includeLocations, includeContacts;

  /**
   * Instantiates a new Gallery media items settings util.
   *
   * @param includeImages the include images
   * @param includeVideos the include videos
   * @param includeAudioRecordings the include audio recordings
   * @param includeFiles the include files
   * @param includeWhiteboards the include whiteboards
   * @param includeStickers the include stickers
   * @param includeGifs the include gifs
   * @param includeLocations the include locations
   * @param includeContacts the include contacts
   */
  public GalleryMediaItemsSettingsUtil(boolean includeImages, boolean includeVideos,
      boolean includeAudioRecordings, boolean includeFiles, boolean includeWhiteboards,
      boolean includeStickers, boolean includeGifs, boolean includeLocations,
      boolean includeContacts) {
    this.includeImages = includeImages;
    this.includeVideos = includeVideos;
    this.includeAudioRecordings = includeAudioRecordings;
    this.includeFiles = includeFiles;
    this.includeWhiteboards = includeWhiteboards;
    this.includeStickers = includeStickers;
    this.includeGifs = includeGifs;
    this.includeLocations = includeLocations;
    this.includeContacts = includeContacts;
  }

  /**
   * Gets gallery items enabled.
   *
   * @return the gallery items enabled
   */
  public List<String> getGalleryItemsEnabled() {

    List<String> galleryItemsEnabled = new ArrayList<>();

    if (includeImages) {
      galleryItemsEnabled.add(CustomMessageTypes.Image.getValue());
    }
    if (includeVideos) {
      galleryItemsEnabled.add(CustomMessageTypes.Video.getValue());
    }
    if (includeAudioRecordings) {
      galleryItemsEnabled.add(CustomMessageTypes.Audio.getValue());
    }
    if (includeFiles) {
      galleryItemsEnabled.add(CustomMessageTypes.File.getValue());
    }
    if (includeStickers) {
      galleryItemsEnabled.add(CustomMessageTypes.Sticker.getValue());
    }
    if (includeGifs) {
      galleryItemsEnabled.add(CustomMessageTypes.Gif.getValue());
    }
    if (includeWhiteboards) {
      galleryItemsEnabled.add(CustomMessageTypes.Whiteboard.getValue());
    }
    if (includeLocations) {
      galleryItemsEnabled.add(CustomMessageTypes.Location.getValue());
    }
    if (includeContacts) {
      galleryItemsEnabled.add(CustomMessageTypes.Contact.getValue());
    }
    return galleryItemsEnabled;
  }

  /**
   * Gets gallery media types header.
   *
   * @return the gallery media types header
   */
  public ArrayList<GalleryMediaTypeHeaderModel> getGalleryMediaTypesHeader() {

    ArrayList<GalleryMediaTypeHeaderModel> galleryMediaTypeHeaderModels = new ArrayList<>();

    if (includeImages) {
      galleryMediaTypeHeaderModels.add(
          new GalleryMediaTypeHeaderModel(CustomMessageTypes.Image.getValue()));
    }
    if (includeVideos) {
      galleryMediaTypeHeaderModels.add(
          new GalleryMediaTypeHeaderModel(CustomMessageTypes.Video.getValue()));
    }
    if (includeAudioRecordings) {
      galleryMediaTypeHeaderModels.add(
          new GalleryMediaTypeHeaderModel(CustomMessageTypes.Audio.getValue()));
    }
    if (includeFiles) {
      galleryMediaTypeHeaderModels.add(
          new GalleryMediaTypeHeaderModel(CustomMessageTypes.File.getValue()));
    }
    if (includeStickers) {
      galleryMediaTypeHeaderModels.add(
          new GalleryMediaTypeHeaderModel(CustomMessageTypes.Sticker.getValue()));
    }
    if (includeGifs) {
      galleryMediaTypeHeaderModels.add(
          new GalleryMediaTypeHeaderModel(CustomMessageTypes.Gif.getValue()));
    }
    if (includeWhiteboards) {
      galleryMediaTypeHeaderModels.add(
          new GalleryMediaTypeHeaderModel(CustomMessageTypes.Whiteboard.getValue()));
    }
    if (includeLocations) {
      galleryMediaTypeHeaderModels.add(
          new GalleryMediaTypeHeaderModel(CustomMessageTypes.Location.getValue()));
    }
    if (includeContacts) {
      galleryMediaTypeHeaderModels.add(
          new GalleryMediaTypeHeaderModel(CustomMessageTypes.Contact.getValue()));
    }

    return galleryMediaTypeHeaderModels;
  }
}
