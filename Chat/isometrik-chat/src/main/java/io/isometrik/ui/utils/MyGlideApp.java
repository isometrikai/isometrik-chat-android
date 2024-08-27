package io.isometrik.ui.utils;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * The helper class for Glide module.
 */
@GlideModule
public class MyGlideApp extends AppGlideModule {
  @Override
  public boolean isManifestParsingEnabled() {
    return false;
  }
}
