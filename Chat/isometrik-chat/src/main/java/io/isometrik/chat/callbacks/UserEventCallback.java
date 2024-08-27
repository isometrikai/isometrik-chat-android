package io.isometrik.chat.callbacks;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.events.user.DeleteUserEvent;
import io.isometrik.chat.events.user.UpdateUserEvent;
import io.isometrik.chat.events.user.block.BlockUserEvent;
import io.isometrik.chat.events.user.block.UnblockUserEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class for the user event callback, with methods for user blocked/unblock, user
 * delete/update events.
 */
public abstract class UserEventCallback {

  /**
   * User blocked.
   *
   * @param isometrik the isometrik
   * @param blockUserEvent the block user event
   */
  public abstract void userBlocked(@NotNull Isometrik isometrik,
      @NotNull BlockUserEvent blockUserEvent);

  /**
   * User unblocked.
   *
   * @param isometrik the isometrik
   * @param unblockUserEvent the unblock user event
   */
  public abstract void userUnblocked(@NotNull Isometrik isometrik,
      @NotNull UnblockUserEvent unblockUserEvent);

  /**
   * User updated.
   *
   * @param isometrik the isometrik
   * @param updateUserEvent the update user event
   */
  public abstract void userUpdated(@NotNull Isometrik isometrik,
      @NotNull UpdateUserEvent updateUserEvent);

  /**
   * User deleted.
   *
   * @param isometrik the isometrik
   * @param deleteUserEvent the delete user event
   */
  public abstract void userDeleted(@NotNull Isometrik isometrik,
      @NotNull DeleteUserEvent deleteUserEvent);
}
