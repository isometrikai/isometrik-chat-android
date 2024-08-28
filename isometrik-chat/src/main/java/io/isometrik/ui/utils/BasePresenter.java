package io.isometrik.ui.utils;

/**
 * The interface Base presenter extended by fragments.
 *
 * @param <T> the type parameter
 */
public interface BasePresenter<T> {
  /**
   * Attach view.
   *
   * @param view the view
   */
  void attachView(T view);

  /**
   * Detach view.
   */
  void detachView();
}