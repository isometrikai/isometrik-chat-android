package io.isometrik.ui.conversations.details.userconversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import io.isometrik.chat.enums.ConversationType;
import io.isometrik.chat.response.conversation.utils.Config;
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil;
import io.isometrik.chat.R;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.ui.conversations.gallery.ConversationDetailsGalleryAdapter;
import io.isometrik.ui.conversations.gallery.GalleryMediaItemsActivity;
import io.isometrik.ui.conversations.gallery.GalleryModel;
import io.isometrik.ui.conversations.newconversation.group.NewGroupConversationActivity;
import io.isometrik.chat.databinding.IsmActivityUserConversationDetailsBinding;
import io.isometrik.ui.messages.chat.common.ChatConfig;
import io.isometrik.ui.messages.preview.PreviewMessageUtil;
import io.isometrik.ui.messages.preview.image.PreviewImagePopup;
import io.isometrik.chat.utils.AlertProgress;

import com.bumptech.glide.Glide;

import io.isometrik.chat.utils.PlaceholderUtils;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import io.isometrik.chat.utils.TimeUtil;

import java.util.ArrayList;

/**
 * The activity to fetch details of a 1-1 conversation with option to block user, create new group conversation with opponent, fetch media items in gallery, change
 * typing/read-delivery/notifications settings and clear messages.
 */
public class UserConversationDetailsActivity extends AppCompatActivity
        implements UserConversationDetailsContract.View {

    private UserConversationDetailsContract.Presenter userConversationDetailsPresenter;
    private IsmActivityUserConversationDetailsBinding ismActivityUserConversationDetailsBinding;

    private AlertProgress alertProgress;
    private AlertDialog alertDialog;

    private boolean unregisteredListeners, typingEventsEnabled, deliveryReadEventsEnabled,
            notificationsEnabled;

    private String conversationId, userName, userImageUrl, userId;
    private long lastSeen;
    private boolean isOnline;
    private ConversationDetailsGalleryAdapter conversationDetailsGalleryAdapter;
    private ArrayList<GalleryModel> galleryItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityUserConversationDetailsBinding =
                IsmActivityUserConversationDetailsBinding.inflate(getLayoutInflater());
        View view = ismActivityUserConversationDetailsBinding.getRoot();
        setContentView(view);
        alertProgress = new AlertProgress();
        updateShimmerVisibility(true, false);
        Bundle extras = getIntent().getExtras();
        conversationId = extras.getString("conversationId");
        userName = extras.getString("userName");
        userImageUrl = extras.getString("userImageUrl");
        isOnline = extras.getBoolean("isOnline", false);
        userId = extras.getString("userId");
        if (userId != null && !userId.isEmpty() && !ChatConfig.INSTANCE.getHideViewSocialProfileOption()) {
            ismActivityUserConversationDetailsBinding.rlViewSocialProfile.setVisibility(View.VISIBLE);
        } else {
            ismActivityUserConversationDetailsBinding.rlViewSocialProfile.setVisibility(View.GONE);
        }
        if (userName != null) {
            ismActivityUserConversationDetailsBinding.tvUserName.setText(userName);

            ismActivityUserConversationDetailsBinding.tvNewPrivateGroupConversation.setText(
                    getString(R.string.ism_user_new_private_group_conversation, userName));
            ismActivityUserConversationDetailsBinding.tvNewPublicConversation.setText(
                    getString(R.string.ism_user_new_public_group_conversation, userName));
            ismActivityUserConversationDetailsBinding.tvNewOpenConversation.setText(
                    getString(R.string.ism_user_new_open_conversation, userName));

            ismActivityUserConversationDetailsBinding.tvBlockUser.setText(
                    getString(R.string.ism_block_user, userName));
        }
        if (isOnline) {
            ismActivityUserConversationDetailsBinding.ivOnlineStatus.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ism_user_online_status_circle));

            ismActivityUserConversationDetailsBinding.tvUserOnlineStatus.setText(
                    getString(R.string.ism_online_status));
        } else {
            ismActivityUserConversationDetailsBinding.ivOnlineStatus.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ism_user_offline_status_circle));
            lastSeen = extras.getLong("lastSeenAt", 0);
            ismActivityUserConversationDetailsBinding.tvUserOnlineStatus.setText(
                    getString(R.string.ism_last_seen_at,
                            TimeUtil.formatTimestampToBothDateAndTime(lastSeen)));
        }

        if (userImageUrl != null) {
            if (PlaceholderUtils.isValidImageUrl(userImageUrl)) {

                try {
                    Glide.with(this)
                            .load(userImageUrl)
                            .placeholder(R.drawable.ism_ic_profile)
                            .transform(new CircleCrop())
                            .into(ismActivityUserConversationDetailsBinding.ivUserImage);
                } catch (IllegalArgumentException | NullPointerException ignore) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(this, userName,
                        ismActivityUserConversationDetailsBinding.ivUserImage, 33);
            }
        }

        userConversationDetailsPresenter = new UserConversationDetailsPresenter(this, userId);
        userConversationDetailsPresenter.registerUserEventListener();

        userConversationDetailsPresenter.requestConversationDetails(conversationId, true);

        ismActivityUserConversationDetailsBinding.rlNewPrivateGroupConversation.setOnClickListener(
                v -> {
                    Intent intent =
                            new Intent(UserConversationDetailsActivity.this, NewGroupConversationActivity.class);
                    intent.putExtra("conversationType", ConversationType.PrivateConversation.getValue());
                    intent.putExtra("userId", userId);
                    intent.putExtra("userProfileImageUrl", userImageUrl);
                    intent.putExtra("userName", userName);
                    intent.putExtra("isOnline", isOnline);

                    startActivity(intent);
                    supportFinishAfterTransition();
                });
        ismActivityUserConversationDetailsBinding.rlNewPublicConversation.setOnClickListener(v -> {
            Intent intent =
                    new Intent(UserConversationDetailsActivity.this, NewGroupConversationActivity.class);
            intent.putExtra("conversationType", ConversationType.PublicConversation.getValue());
            intent.putExtra("userId", userId);
            intent.putExtra("userProfileImageUrl", userImageUrl);
            intent.putExtra("userName", userName);
            intent.putExtra("isOnline", isOnline);

            startActivity(intent);
            supportFinishAfterTransition();
        });
        ismActivityUserConversationDetailsBinding.rlNewOpenConversation.setOnClickListener(v -> {
            Intent intent =
                    new Intent(UserConversationDetailsActivity.this, NewGroupConversationActivity.class);
            intent.putExtra("conversationType", ConversationType.OpenConversation.getValue());
            intent.putExtra("userId", userId);
            intent.putExtra("userProfileImageUrl", userImageUrl);
            intent.putExtra("userName", userName);
            intent.putExtra("isOnline", isOnline);

            startActivity(intent);
            supportFinishAfterTransition();
        });

        ismActivityUserConversationDetailsBinding.rlClearConversation.setOnClickListener(
                v -> new AlertDialog.Builder(this).setTitle(getString(R.string.ism_clear_conversation))
                        .setMessage(getString(R.string.ism_clear_conversation_alert))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                            dialog.cancel();
                            showProgressDialog(getString(R.string.ism_clearing_conversation));
                            userConversationDetailsPresenter.clearConversation(conversationId);
                        })
                        .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                        .create()
                        .show());

        ismActivityUserConversationDetailsBinding.rlDeleteConversation.setOnClickListener(
                v -> new AlertDialog.Builder(this).setTitle(getString(R.string.ism_delete_conversation))
                        .setMessage(getString(R.string.ism_delete_conversation_alert))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                            dialog.cancel();
                            showProgressDialog(getString(R.string.ism_deleting_conversation));
                            userConversationDetailsPresenter.deleteConversation(conversationId);
                        })
                        .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                        .create()
                        .show());

        ismActivityUserConversationDetailsBinding.incShimmer.ibBackExpanded.setOnClickListener(
                v -> onBackPressed());
        ismActivityUserConversationDetailsBinding.ibBackExpanded.setOnClickListener(
                v -> onBackPressed());

        ismActivityUserConversationDetailsBinding.rlSendMessage.setOnClickListener(
                v -> onBackPressed());

        ismActivityUserConversationDetailsBinding.rlSearchInConversation.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("searchMessageRequested", true);
            setResult(Activity.RESULT_OK, returnIntent);
            supportFinishAfterTransition();
        });

        ismActivityUserConversationDetailsBinding.ivUserImage.setOnClickListener(v -> {
            if (PlaceholderUtils.isValidImageUrl(userImageUrl)) {
                new PreviewImagePopup().show(UserConversationDetailsActivity.this, userImageUrl);
            }
        });

        ismActivityUserConversationDetailsBinding.rlBlockUser.setOnClickListener(v -> {
            showProgressDialog(getString(R.string.ism_blocking_user, userName));
            userConversationDetailsPresenter.blockUser(userId);
        });
        ismActivityUserConversationDetailsBinding.rlViewSocialProfile.setOnClickListener(
                v -> IsometrikChatSdk.getInstance().getChatActionsClickListener().onViewSocialProfileClick(userId)
        );
    }

    @Override
    public void onConversationDeletedSuccessfully() {
        hideProgressDialog();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("conversationLeftOrDeleted", true);
        setResult(Activity.RESULT_OK, returnIntent);
        supportFinishAfterTransition();
    }

    @Override
    public void onConversationClearedSuccessfully() {
        hideProgressDialog();
    }

    @Override
    public void onBackPressed() {
        unregisterListeners();
        try {
            super.onBackPressed();
        } catch (Exception ignore) {
        }
    }

    @Override
    protected void onDestroy() {
        unregisterListeners();
        super.onDestroy();
    }

    @Override
    public void onError(String errorMessage) {
        hideProgressDialog();

        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConversationDetailsFetchedSuccessfully(ConversationDetailsUtil conversationDetails,
                                                         boolean isAdmin, MembersWatchersModel userDetails) {
        userId = userDetails.getMemberId();
        runOnUiThread(() -> {
            try {
                if (!userDetails.getMemberName().equals(userName)) {

                    userName = userDetails.getMemberName();
                    ismActivityUserConversationDetailsBinding.tvUserName.setText(userName);
                    ismActivityUserConversationDetailsBinding.tvNewPrivateGroupConversation.setText(
                            getString(R.string.ism_user_new_private_group_conversation, userName));
                    ismActivityUserConversationDetailsBinding.tvNewPublicConversation.setText(
                            getString(R.string.ism_user_new_public_group_conversation, userName));
                    ismActivityUserConversationDetailsBinding.tvNewOpenConversation.setText(
                            getString(R.string.ism_user_new_open_conversation, userName));
                    ismActivityUserConversationDetailsBinding.tvBlockUser.setText(
                            getString(R.string.ism_block_user, userName));
                }
                if (!userDetails.getMemberProfileImageUrl().equals(userImageUrl)) {
                    userImageUrl = userDetails.getMemberProfileImageUrl();
                    if (PlaceholderUtils.isValidImageUrl(userImageUrl)) {

                        try {
                            Glide.with(this)
                                    .load(userImageUrl)
                                    .placeholder(R.drawable.ism_ic_profile)
                                    .transform(new CircleCrop())
                                    .into(ismActivityUserConversationDetailsBinding.ivUserImage);
                        } catch (IllegalArgumentException | NullPointerException ignore) {
                        }
                    } else {
                        PlaceholderUtils.setTextRoundDrawable(this, userName,
                                ismActivityUserConversationDetailsBinding.ivUserImage, 33);
                    }
                }

                if (isOnline != userDetails.isOnline()) {
                    isOnline = userDetails.isOnline();
                    if (isOnline) {
                        ismActivityUserConversationDetailsBinding.ivOnlineStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ism_user_online_status_circle));

                        ismActivityUserConversationDetailsBinding.tvUserOnlineStatus.setText(getString(R.string.ism_online_status));
                    } else {
                        ismActivityUserConversationDetailsBinding.ivOnlineStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ism_user_offline_status_circle));

                        if (userDetails.getLastSeen() != lastSeen) {
                            lastSeen = userDetails.getLastSeen();
                            ismActivityUserConversationDetailsBinding.tvUserOnlineStatus.setText(
                                    getString(R.string.ism_last_seen_at, TimeUtil.formatTimestampToBothDateAndTime(lastSeen)));
                        }
                    }
                }

                Config config = conversationDetails.getConfig();

                typingEventsEnabled = config.isTypingEvents();
                deliveryReadEventsEnabled = config.isReadEvents();
                notificationsEnabled = config.isPushNotifications();
                if (isAdmin) {

                    //Conversation settings can be updated as given user is an admin in conversation.

                    updateConversationSettingsText();

                    ismActivityUserConversationDetailsBinding.rlTypingMessage.setOnClickListener(
                            v -> new AlertDialog.Builder(this).setTitle(
                                            getString(R.string.ism_update_conversation_settings))
                                    .setMessage(ismActivityUserConversationDetailsBinding.tvTypingMessage.getText())
                                    .setCancelable(true)
                                    .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                                        dialog.cancel();
                                        showProgressDialog(getString(R.string.ism_updating_typing_message_settings));

                                        userConversationDetailsPresenter.updateConversationSettings(conversationId,
                                                !typingEventsEnabled, null, null);
                                    })
                                    .setNegativeButton(getString(R.string.ism_cancel),
                                            (dialog, id) -> dialog.cancel())
                                    .create()
                                    .show());

                    ismActivityUserConversationDetailsBinding.rlDeliveryReadEvents.setOnClickListener(
                            v -> new AlertDialog.Builder(this).setTitle(
                                            getString(R.string.ism_update_conversation_settings))
                                    .setMessage(
                                            ismActivityUserConversationDetailsBinding.tvDeliveryReadEvents.getText())
                                    .setCancelable(true)
                                    .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                                        dialog.cancel();
                                        showProgressDialog(
                                                getString(R.string.ism_updating_message_delivery_read_settings));

                                        userConversationDetailsPresenter.updateConversationSettings(conversationId,
                                                null, !deliveryReadEventsEnabled, null);
                                    })
                                    .setNegativeButton(getString(R.string.ism_cancel),
                                            (dialog, id) -> dialog.cancel())
                                    .create()
                                    .show());

                    ismActivityUserConversationDetailsBinding.rlNotifications.setOnClickListener(
                            v -> new AlertDialog.Builder(this).setTitle(
                                            getString(R.string.ism_update_conversation_settings))
                                    .setMessage(ismActivityUserConversationDetailsBinding.tvNotifications.getText())
                                    .setCancelable(true)
                                    .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                                        dialog.cancel();
                                        showProgressDialog(getString(R.string.ism_updating_notification_settings));

                                        userConversationDetailsPresenter.updateConversationSettings(conversationId,
                                                null, null, !notificationsEnabled);
                                    })
                                    .setNegativeButton(getString(R.string.ism_cancel),
                                            (dialog, id) -> dialog.cancel())
                                    .create()
                                    .show());
                } else {
                    //Conversation settings can't be updated as given user is not an admin in conversation.
                    if (typingEventsEnabled) {
                        ismActivityUserConversationDetailsBinding.tvTypingMessage.setText(
                                getString(R.string.ism_typing_message_setting, getString(R.string.ism_enabled)));
                    } else {
                        ismActivityUserConversationDetailsBinding.tvTypingMessage.setText(
                                getString(R.string.ism_typing_message_setting, getString(R.string.ism_disabled)));
                    }

                    if (deliveryReadEventsEnabled) {
                        ismActivityUserConversationDetailsBinding.tvDeliveryReadEvents.setText(
                                getString(R.string.ism_read_delivery_setting, getString(R.string.ism_enabled)));
                    } else {
                        ismActivityUserConversationDetailsBinding.tvDeliveryReadEvents.setText(
                                getString(R.string.ism_read_delivery_setting, getString(R.string.ism_disabled)));
                    }

                    if (notificationsEnabled) {
                        ismActivityUserConversationDetailsBinding.tvNotifications.setText(
                                getString(R.string.ism_notification_setting, getString(R.string.ism_enabled)));
                    } else {
                        ismActivityUserConversationDetailsBinding.tvNotifications.setText(
                                getString(R.string.ism_notification_setting, getString(R.string.ism_disabled)));
                    }
                }
            } catch (NullPointerException ignore) {
                //To handle crash when user has been deleted
            }
            updateShimmerVisibility(false, false);
        });
        updateShimmerVisibility(true, true);
        userConversationDetailsPresenter.fetchGalleryItems(conversationId,
                userConversationDetailsPresenter.getGalleryMediaItemsSettingsUtil()
                        .getGalleryItemsEnabled());
    }

    @Override
    public void onGalleryItemsFetchedSuccessfully(ArrayList<GalleryModel> galleryItems,
                                                  boolean hasMoreItems) {

        runOnUiThread(() -> {
            ismActivityUserConversationDetailsBinding.pbGallery.setVisibility(View.GONE);
            if (galleryItems.size() == 0) {
                ismActivityUserConversationDetailsBinding.rlEmptyGallery.setVisibility(View.VISIBLE);
                ismActivityUserConversationDetailsBinding.rvConversationGallery.setVisibility(View.GONE);
                ismActivityUserConversationDetailsBinding.tvShowMoreGallery.setVisibility(View.GONE);
            } else {

                this.galleryItems = new ArrayList<>();
                this.galleryItems.addAll(galleryItems);
                ismActivityUserConversationDetailsBinding.rvConversationGallery.setLayoutManager(
                        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

                conversationDetailsGalleryAdapter =
                        new ConversationDetailsGalleryAdapter(this, this.galleryItems);
                ismActivityUserConversationDetailsBinding.rvConversationGallery.setAdapter(
                        conversationDetailsGalleryAdapter);

                ismActivityUserConversationDetailsBinding.rlEmptyGallery.setVisibility(View.GONE);
                ismActivityUserConversationDetailsBinding.rvConversationGallery.setVisibility(View.VISIBLE);

                if (hasMoreItems) {
                    ismActivityUserConversationDetailsBinding.tvShowMoreGallery.setVisibility(View.VISIBLE);

                    ismActivityUserConversationDetailsBinding.tvShowMoreGallery.setOnClickListener(v -> {
                        Intent intent =
                                new Intent(UserConversationDetailsActivity.this, GalleryMediaItemsActivity.class);
                        intent.putExtra("galleryMediaItemsSettingsUtil",
                                userConversationDetailsPresenter.getGalleryMediaItemsSettingsUtil());
                        intent.putExtra("conversationId", conversationId);
                        startActivity(intent);
                    });
                } else {
                    ismActivityUserConversationDetailsBinding.tvShowMoreGallery.setVisibility(View.GONE);
                }
                ismActivityUserConversationDetailsBinding.rvConversationGallery.addOnItemTouchListener(
                        new RecyclerItemClickListener(this,
                                ismActivityUserConversationDetailsBinding.rvConversationGallery,
                                new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (position >= 0) {

                                            GalleryModel galleryModel = galleryItems.get(position);

                                            PreviewMessageUtil.previewMessage(UserConversationDetailsActivity.this,
                                                    galleryModel);
                                        }
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                    }
                                }));
            }
            updateShimmerVisibility(false, true);
        });
    }

    @Override
    public void onConversationSettingUpdatedSuccessfully(Boolean enableTypingMessage,
                                                         Boolean enableDeliveryReadEvents, Boolean enableNotifications) {
        hideProgressDialog();
        if (enableTypingMessage != null) {
            typingEventsEnabled = enableTypingMessage;
        }
        if (enableDeliveryReadEvents != null) {
            deliveryReadEventsEnabled = enableDeliveryReadEvents;
        }
        if (enableNotifications != null) {
            notificationsEnabled = enableNotifications;
        }
        runOnUiThread(this::updateConversationSettingsText);
    }

    private void showProgressDialog(String message) {

        alertDialog = alertProgress.getProgressDialog(this, message);
        if (!isFinishing()) alertDialog.show();
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    private void updateConversationSettingsText() {
        if (typingEventsEnabled) {
            ismActivityUserConversationDetailsBinding.tvTypingMessage.setText(
                    getString(R.string.ism_update_typing_message_setting, getString(R.string.ism_disable)));
        } else {
            ismActivityUserConversationDetailsBinding.tvTypingMessage.setText(
                    getString(R.string.ism_update_typing_message_setting, getString(R.string.ism_enable)));
        }

        if (deliveryReadEventsEnabled) {
            ismActivityUserConversationDetailsBinding.tvDeliveryReadEvents.setText(
                    getString(R.string.ism_update_read_delivery_setting, getString(R.string.ism_disable)));
        } else {
            ismActivityUserConversationDetailsBinding.tvDeliveryReadEvents.setText(
                    getString(R.string.ism_update_read_delivery_setting, getString(R.string.ism_enable)));
        }

        if (notificationsEnabled) {
            ismActivityUserConversationDetailsBinding.tvNotifications.setText(
                    getString(R.string.ism_update_notification_setting, getString(R.string.ism_disable)));
        } else {
            ismActivityUserConversationDetailsBinding.tvNotifications.setText(
                    getString(R.string.ism_update_notification_setting, getString(R.string.ism_enable)));
        }
    }

    @Override
    public void onUserBlocked() {
        hideProgressDialog();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("messagingBlocked", true);
        setResult(Activity.RESULT_OK, returnIntent);
        supportFinishAfterTransition();
    }

    /**
     * Cleanup all realtime isometrik event listeners that were added at time of exit
     */
    private void unregisterListeners() {
        if (!unregisteredListeners) {
            unregisteredListeners = true;

            userConversationDetailsPresenter.unregisterUserEventListener();
        }
    }

    @Override
    public void onFailedToFetchConversationDetails(String error) {
        onError(error);
        updateShimmerVisibility(false, false);
        onBackPressed();
    }

    @Override
    public void onFailedToFetchGalleryItems(String error) {
        ismActivityUserConversationDetailsBinding.pbGallery.setVisibility(View.GONE);
        updateShimmerVisibility(false, true);
        onError(error);
    }

    private void updateShimmerVisibility(boolean visible, boolean gallery) {
        if (gallery) {
            if (visible) {
                ismActivityUserConversationDetailsBinding.shimmerFrameLayoutGallery.startShimmer();
                ismActivityUserConversationDetailsBinding.shimmerFrameLayoutGallery.setVisibility(View.VISIBLE);
            } else {
                if (ismActivityUserConversationDetailsBinding.shimmerFrameLayoutGallery.getVisibility() == View.VISIBLE) {
                    ismActivityUserConversationDetailsBinding.shimmerFrameLayoutGallery.setVisibility(View.GONE);
                    ismActivityUserConversationDetailsBinding.shimmerFrameLayoutGallery.stopShimmer();
                }
            }
        } else {
            if (visible) {
                ismActivityUserConversationDetailsBinding.incShimmer.shimmerFrameLayout.startShimmer();
                ismActivityUserConversationDetailsBinding.rlShimmer.setVisibility(View.VISIBLE);
            } else {
                if (ismActivityUserConversationDetailsBinding.rlShimmer.getVisibility() == View.VISIBLE) {
                    ismActivityUserConversationDetailsBinding.rlShimmer.setVisibility(View.GONE);
                    ismActivityUserConversationDetailsBinding.incShimmer.shimmerFrameLayout.stopShimmer();
                }
            }
        }
    }
}
