package io.isometrik.ui.conversations.details.groupconversation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import io.isometrik.chat.response.conversation.utils.Config;
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil;
import io.isometrik.chat.R;
import io.isometrik.ui.camera.CameraActivity;
import io.isometrik.ui.conversations.details.participants.MembersWatchersActivity;
import io.isometrik.ui.conversations.details.participants.MembersWatchersAdapter;
import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.ui.conversations.gallery.ConversationDetailsGalleryAdapter;
import io.isometrik.ui.conversations.gallery.GalleryMediaItemsActivity;
import io.isometrik.ui.conversations.gallery.GalleryModel;
import io.isometrik.ui.conversations.participants.AddParticipantsActivity;
import io.isometrik.chat.databinding.IsmActivityConversationDetailsBinding;
import io.isometrik.ui.messages.preview.PreviewMessageUtil;
import io.isometrik.ui.messages.preview.image.PreviewImagePopup;
import io.isometrik.chat.utils.AlertProgress;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.KeyboardUtil;
import io.isometrik.chat.utils.PlaceholderUtils;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import io.isometrik.chat.utils.Utilities;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The activity to fetch details of a group conversation with option to add/remove members,
 * grant/revoke admin privileges, update title/image, fetch media items in gallery, change
 * typing/read-delivery/notifications settings, clear messages and leave conversation.
 */
public class ConversationDetailsActivity extends AppCompatActivity
        implements ConversationDetailsContract.View {

    private ConversationDetailsContract.Presenter conversationDetailsPresenter;
    private IsmActivityConversationDetailsBinding ismActivityConversationDetailsBinding;
    private String conversationId, conversationTitle, conversationImageUrl;
    private AlertProgress alertProgress;
    private AlertDialog alertDialog;

    private MembersWatchersAdapter membersAdapter;
    private ArrayList<MembersWatchersModel> members;
    private ConversationDetailsGalleryAdapter conversationDetailsGalleryAdapter;
    private ArrayList<GalleryModel> galleryItems;

    private ActivityResultLauncher<Intent> addParticipantsActivityLauncher;
    private ActivityResultLauncher<Intent> cameraActivityLauncher;
    private ActivityResultLauncher<Intent> showMoreParticipantsActivityLauncher;
    private File imageFile;
    private int membersCount;
    private boolean typingEventsEnabled, deliveryReadEventsEnabled, notificationsEnabled,
            editingTitle, editingImage;
    private boolean cleanUpRequested = false;
    private AlertDialog uploadProgressDialog;
    private CircularProgressIndicator circularProgressIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityConversationDetailsBinding =
                IsmActivityConversationDetailsBinding.inflate(getLayoutInflater());
        View view = ismActivityConversationDetailsBinding.getRoot();
        setContentView(view);
        alertProgress = new AlertProgress();
        updateShimmerVisibility(true, false);
        Bundle extras = getIntent().getExtras();
        conversationId = extras.getString("conversationId");
        conversationTitle = extras.getString("conversationTitle");
        conversationImageUrl = extras.getString("conversationImageUrl");
        if (conversationTitle != null) {
            ismActivityConversationDetailsBinding.tvConversationTitle.setText(conversationTitle);
        }

        if (conversationImageUrl != null) {
            loadConversationImage();
        }

        conversationDetailsPresenter = new ConversationDetailsPresenter(this);

        members = new ArrayList<>();
        ismActivityConversationDetailsBinding.rvConversationMembers.setLayoutManager(
                new LinearLayoutManager(this));

        membersAdapter = new MembersWatchersAdapter(this, members, null, null);
        ismActivityConversationDetailsBinding.rvConversationMembers.setAdapter(membersAdapter);

        conversationDetailsPresenter.requestConversationDetails(conversationId, true);

        ismActivityConversationDetailsBinding.rlClearConversation.setOnClickListener(
                v -> new AlertDialog.Builder(this).setTitle(getString(R.string.ism_clear_conversation))
                        .setMessage(getString(R.string.ism_clear_conversation_alert))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                            dialog.cancel();
                            showProgressDialog(getString(R.string.ism_clearing_conversation));
                            conversationDetailsPresenter.clearConversation(conversationId);
                        })
                        .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                        .create()
                        .show());

        ismActivityConversationDetailsBinding.rlDeleteConversation.setOnClickListener(
                v -> new AlertDialog.Builder(this).setTitle(getString(R.string.ism_delete_conversation))
                        .setMessage(getString(R.string.ism_delete_conversation_alert))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                            dialog.cancel();
                            showProgressDialog(getString(R.string.ism_deleting_conversation));
                            conversationDetailsPresenter.deleteConversation(conversationId);
                        })
                        .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                        .create()
                        .show());

        ismActivityConversationDetailsBinding.rlLeaveConversation.setOnClickListener(
                v -> new AlertDialog.Builder(this).setTitle(getString(R.string.ism_leave_conversation))
                        .setMessage(getString(R.string.ism_leave_conversation_alert))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                            dialog.cancel();
                            showProgressDialog(getString(R.string.ism_leaving_conversation));
                            conversationDetailsPresenter.leaveConversation(conversationId);
                        })
                        .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                        .create()
                        .show());

        ismActivityConversationDetailsBinding.incShimmer.ibBackExpanded.setOnClickListener(
                v -> onBackPressed());
        ismActivityConversationDetailsBinding.ibBackExpanded.setOnClickListener(v -> onBackPressed());

        addParticipantsActivityLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        conversationDetailsPresenter.fetchConversationMembers(conversationId);
                    } else {
                        Toast.makeText(ConversationDetailsActivity.this,
                                getString(R.string.ism_add_participants_canceled), Toast.LENGTH_SHORT).show();
                    }
                });

        showMoreParticipantsActivityLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {

                        conversationDetailsPresenter.fetchConversationMembers(conversationId);
                    }
                });

        ismActivityConversationDetailsBinding.rlAddParticipants.setOnClickListener(v -> {

            Intent intent = new Intent(ConversationDetailsActivity.this, AddParticipantsActivity.class);
            intent.putExtra("conversationTitle", conversationTitle);
            intent.putExtra("conversationId", conversationId);
            addParticipantsActivityLauncher.launch(intent);
        });

        ismActivityConversationDetailsBinding.rlSendMessage.setOnClickListener(v -> onBackPressed());

        cameraActivityLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {

                            imageFile = new File(result.getData().getStringExtra("capturedImagePath"));

                            ismActivityConversationDetailsBinding.ivConversationImage.setPadding(0, 0, 0, 0);
                            try {
                                Glide.with(this)
                                        .load(imageFile.getAbsolutePath())
                                        .transform(new CircleCrop())
                                        .into(ismActivityConversationDetailsBinding.ivConversationImage);
                            } catch (IllegalArgumentException | NullPointerException ignore) {
                            }
                            ismActivityConversationDetailsBinding.ivEditConversationImage.setImageDrawable(
                                    ContextCompat.getDrawable(this, R.drawable.ism_ic_done));
                            editingImage = true;
                            ismActivityConversationDetailsBinding.ivRemoveConversationImage.setVisibility(
                                    View.VISIBLE);
                        } else {
                            editingImage = false;
                            ismActivityConversationDetailsBinding.ivEditConversationImage.setImageDrawable(
                                    ContextCompat.getDrawable(this, R.drawable.ism_ic_edit_title));
                            ismActivityConversationDetailsBinding.ivRemoveConversationImage.setVisibility(
                                    View.GONE);

                            deleteImage();
                            Toast.makeText(this, getString(R.string.ism_image_capture_failure), Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        editingImage = false;
                        ismActivityConversationDetailsBinding.ivEditConversationImage.setImageDrawable(
                                ContextCompat.getDrawable(this, R.drawable.ism_ic_edit_title));
                        ismActivityConversationDetailsBinding.ivRemoveConversationImage.setVisibility(
                                View.GONE);

                        deleteImage();
                        Toast.makeText(this, getString(R.string.ism_image_capture_canceled), Toast.LENGTH_LONG)
                                .show();
                    }
                });

        ismActivityConversationDetailsBinding.rlSearchInConversation.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("searchMessageRequested", true);
            setResult(Activity.RESULT_OK, returnIntent);
            supportFinishAfterTransition();
        });

        ismActivityConversationDetailsBinding.ivConversationImage.setOnClickListener(v -> {
            if (PlaceholderUtils.isValidImageUrl(conversationImageUrl)) {
                new PreviewImagePopup().show(ConversationDetailsActivity.this, conversationImageUrl);
            }
        });
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
    public void onConversationLeftSuccessfully() {
        hideProgressDialog();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("conversationLeftOrDeleted", true);
        setResult(Activity.RESULT_OK, returnIntent);
        supportFinishAfterTransition();
    }

    @Override
    public void onConversationTitleUpdated(String conversationTitle) {
        hideProgressDialog();
        this.conversationTitle = conversationTitle;
        ismActivityConversationDetailsBinding.etConversationTitle.setVisibility(View.GONE);
        ismActivityConversationDetailsBinding.tvConversationTitle.setText(conversationTitle);
        ismActivityConversationDetailsBinding.tvConversationTitle.setVisibility(View.VISIBLE);
        ismActivityConversationDetailsBinding.ivEditConversationTitle.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ism_ic_edit_title));
        editingTitle = false;
    }

    @Override
    public void onConversationTitleUpdateFailed(String error) {
        editingTitle = false;
        onError(error);
    }

    @Override
    public void onConversationImageUpdated() {
        hideProgressDialog();
        ismActivityConversationDetailsBinding.ivEditConversationImage.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ism_ic_edit_title));
        ismActivityConversationDetailsBinding.ivRemoveConversationImage.setVisibility(View.GONE);

        editingImage = false;
    }

    @Override
    public void onConversationImageUpdateFailed(String error) {
        editingImage = false;
        ismActivityConversationDetailsBinding.ivEditConversationImage.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ism_ic_edit_title));
        ismActivityConversationDetailsBinding.ivRemoveConversationImage.setVisibility(View.GONE);

        onError(error);
    }

    @Override
    public void onConversationDetailsFetchedSuccessfully(ConversationDetailsUtil conversationDetails,
                                                         String conversationCreationDetails, boolean isAdmin,
                                                         ArrayList<MembersWatchersModel> conversationMembers) {

        conversationImageUrl = conversationDetails.getConversationImageUrl();

        runOnUiThread(() -> {

            if (!conversationDetails.getConversationTitle().equals(conversationTitle)) {
                conversationTitle = conversationDetails.getConversationTitle();
                ismActivityConversationDetailsBinding.tvConversationTitle.setText(conversationTitle);
            }

            if (!conversationDetails.getConversationImageUrl().equals(conversationImageUrl)) {
                conversationImageUrl = conversationDetails.getConversationImageUrl();

                loadConversationImage();
            }

            ismActivityConversationDetailsBinding.tvCreatedBy.setText(getString(R.string.ism_created_by, conversationCreationDetails));
            membersCount = conversationDetails.getMembersCount();
            ismActivityConversationDetailsBinding.tvParticipantsCount.setText(getString(R.string.ism_participants_count, membersCount));
            Config config = conversationDetails.getConfig();

            typingEventsEnabled = config.isTypingEvents();
            deliveryReadEventsEnabled = config.isReadEvents();
            notificationsEnabled = config.isPushNotifications();
            if (isAdmin) {

                //Conversation settings can be updated as given user is an admin in conversation.
                updateConversationSettingsText();

                ismActivityConversationDetailsBinding.rlTypingMessage.setOnClickListener(
                        v -> new AlertDialog.Builder(this).setTitle(
                                        getString(R.string.ism_update_conversation_settings))
                                .setMessage(ismActivityConversationDetailsBinding.tvTypingMessage.getText())
                                .setCancelable(true)
                                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                                    dialog.cancel();
                                    showProgressDialog(getString(R.string.ism_updating_typing_message_settings));

                                    conversationDetailsPresenter.updateConversationSettings(conversationId,
                                            !typingEventsEnabled, null, null);
                                })
                                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                                .create()
                                .show());

                ismActivityConversationDetailsBinding.rlDeliveryReadEvents.setOnClickListener(
                        v -> new AlertDialog.Builder(this).setTitle(
                                        getString(R.string.ism_update_conversation_settings))
                                .setMessage(ismActivityConversationDetailsBinding.tvDeliveryReadEvents.getText())
                                .setCancelable(true)
                                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                                    dialog.cancel();
                                    showProgressDialog(
                                            getString(R.string.ism_updating_message_delivery_read_settings));

                                    conversationDetailsPresenter.updateConversationSettings(conversationId, null,
                                            !deliveryReadEventsEnabled, null);
                                })
                                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                                .create()
                                .show());

                ismActivityConversationDetailsBinding.rlNotifications.setOnClickListener(
                        v -> new AlertDialog.Builder(this).setTitle(
                                        getString(R.string.ism_update_conversation_settings))
                                .setMessage(ismActivityConversationDetailsBinding.tvNotifications.getText())
                                .setCancelable(true)
                                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                                    dialog.cancel();
                                    showProgressDialog(getString(R.string.ism_updating_notification_settings));

                                    conversationDetailsPresenter.updateConversationSettings(conversationId, null,
                                            null, !notificationsEnabled);
                                })
                                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                                .create()
                                .show());

                ismActivityConversationDetailsBinding.ivEditConversationTitle.setVisibility(View.VISIBLE);
                ismActivityConversationDetailsBinding.ivEditConversationImage.setVisibility(View.VISIBLE);

                ismActivityConversationDetailsBinding.ivEditConversationTitle.setOnClickListener(v -> {

                    if (editingTitle) {
                        KeyboardUtil.hideKeyboard(this);

                        new AlertDialog.Builder(this).setTitle(
                                        getString(R.string.ism_update_conversation_title_alert_heading))
                                .setMessage(getString(R.string.ism_update_conversation_title_alert_message, ismActivityConversationDetailsBinding.etConversationTitle.getText()))
                                .setCancelable(true)
                                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                                    dialog.cancel();
                                    showProgressDialog(getString(R.string.ism_updating_conversation_title));
                                    conversationDetailsPresenter.updateConversationTitle(conversationId,
                                            ismActivityConversationDetailsBinding.etConversationTitle.getText()
                                                    .toString(), conversationTitle);
                                })
                                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                                .create()
                                .show();
                    } else {
                        editingTitle = true;
                        ismActivityConversationDetailsBinding.etConversationTitle.setText(conversationTitle);
                        ismActivityConversationDetailsBinding.etConversationTitle.requestFocus();
                        ismActivityConversationDetailsBinding.etConversationTitle.setVisibility(View.VISIBLE);
                        ismActivityConversationDetailsBinding.tvConversationTitle.setVisibility(View.INVISIBLE);
                        ismActivityConversationDetailsBinding.ivEditConversationTitle.setImageDrawable(
                                ContextCompat.getDrawable(this, R.drawable.ism_ic_done));
                        KeyboardUtil.showSoftKeyboard(this,
                                ismActivityConversationDetailsBinding.etConversationTitle);
                    }
                });

                ismActivityConversationDetailsBinding.ivEditConversationImage.setOnClickListener(v -> {

                    if (editingImage) {
                        showImageUploadConfirmationDialog();
                    } else {
                        showImageOptionsDialog();
                    }
                });
                ismActivityConversationDetailsBinding.ivRemoveConversationImage.setOnClickListener(v -> {
                    editingImage = false;
                    ismActivityConversationDetailsBinding.ivEditConversationImage.setImageDrawable(
                            ContextCompat.getDrawable(ConversationDetailsActivity.this,
                                    R.drawable.ism_ic_edit_title));
                    ismActivityConversationDetailsBinding.ivRemoveConversationImage.setVisibility(View.GONE);
                    loadConversationImage();
                });
            } else {
                //Conversation settings can't be updated as given user is not an admin in conversation.
                if (typingEventsEnabled) {
                    ismActivityConversationDetailsBinding.tvTypingMessage.setText(
                            getString(R.string.ism_typing_message_setting, getString(R.string.ism_enabled)));
                } else {
                    ismActivityConversationDetailsBinding.tvTypingMessage.setText(
                            getString(R.string.ism_typing_message_setting, getString(R.string.ism_disabled)));
                }

                if (deliveryReadEventsEnabled) {
                    ismActivityConversationDetailsBinding.tvDeliveryReadEvents.setText(
                            getString(R.string.ism_read_delivery_setting, getString(R.string.ism_enabled)));
                } else {
                    ismActivityConversationDetailsBinding.tvDeliveryReadEvents.setText(
                            getString(R.string.ism_read_delivery_setting, getString(R.string.ism_disabled)));
                }

                if (notificationsEnabled) {
                    ismActivityConversationDetailsBinding.tvNotifications.setText(
                            getString(R.string.ism_notification_setting, getString(R.string.ism_enabled)));
                } else {
                    ismActivityConversationDetailsBinding.tvNotifications.setText(
                            getString(R.string.ism_notification_setting, getString(R.string.ism_disabled)));
                }

                ismActivityConversationDetailsBinding.ivEditConversationTitle.setVisibility(View.GONE);
                ismActivityConversationDetailsBinding.ivEditConversationImage.setVisibility(View.GONE);
            }

            ismActivityConversationDetailsBinding.rlAddParticipants.setVisibility(
                    isAdmin ? View.VISIBLE : View.GONE);

            members.addAll(conversationMembers);
            membersAdapter.notifyDataSetChanged();

            if (conversationDetails.getMembersCount() > members.size()) {
                ismActivityConversationDetailsBinding.tvShowMoreParticipants.setVisibility(View.VISIBLE);
                ismActivityConversationDetailsBinding.tvShowMoreParticipants.setOnClickListener(v -> {

                    Intent intent =
                            new Intent(ConversationDetailsActivity.this, MembersWatchersActivity.class);
                    intent.putExtra("conversationId", conversationId);
                    intent.putExtra("conversationTitle", conversationTitle);
                    intent.putExtra("isUserAnAdmin", isAdmin);
                    intent.putExtra("userId", conversationDetails.getUsersOwnDetails().getMemberId());

                    showMoreParticipantsActivityLauncher.launch(intent);
                });
            } else {
                ismActivityConversationDetailsBinding.tvShowMoreParticipants.setVisibility(View.GONE);
            }
            updateShimmerVisibility(false, false);
        });
        updateShimmerVisibility(true, true);
        conversationDetailsPresenter.fetchGalleryItems(conversationId,

                conversationDetailsPresenter.getGalleryMediaItemsSettingsUtil().getGalleryItemsEnabled());
    }

    @Override
    public void onMemberRemovedSuccessfully(String memberId) {
        hideProgressDialog();
        runOnUiThread(() -> {
            for (int i = 0; i < members.size(); i++) {

                if (members.get(i).getMemberId().equals(memberId)) {

                    members.remove(i);
                    membersAdapter.notifyItemRemoved(i);
                    break;
                }
            }
            ismActivityConversationDetailsBinding.tvParticipantsCount.setText(
                    getString(R.string.ism_participants_count, membersCount - 1));
        });
    }

    @Override
    public void onMemberAdminPermissionsUpdated(String memberId, boolean admin) {
        hideProgressDialog();
        runOnUiThread(() -> {
            for (int i = 0; i < members.size(); i++) {

                if (members.get(i).getMemberId().equals(memberId)) {

                    MembersWatchersModel membersModel = members.get(i);
                    membersModel.setAdmin(admin);
                    members.set(i, membersModel);
                    membersAdapter.notifyItemChanged(i);
                    break;
                }
            }
        });
    }

    @Override
    public void onGalleryItemsFetchedSuccessfully(ArrayList<GalleryModel> galleryItems,
                                                  boolean hasMoreItems) {

        runOnUiThread(() -> {
            ismActivityConversationDetailsBinding.pbGallery.setVisibility(View.GONE);
            if (galleryItems.size() == 0) {
                ismActivityConversationDetailsBinding.rlEmptyGallery.setVisibility(View.VISIBLE);
                ismActivityConversationDetailsBinding.rvConversationGallery.setVisibility(View.GONE);
                ismActivityConversationDetailsBinding.tvShowMoreGallery.setVisibility(View.GONE);
            } else {

                this.galleryItems = new ArrayList<>();
                this.galleryItems.addAll(galleryItems);
                ismActivityConversationDetailsBinding.rvConversationGallery.setLayoutManager(
                        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

                conversationDetailsGalleryAdapter = new ConversationDetailsGalleryAdapter(this, this.galleryItems);
                ismActivityConversationDetailsBinding.rvConversationGallery.setAdapter(
                        conversationDetailsGalleryAdapter);

                ismActivityConversationDetailsBinding.rlEmptyGallery.setVisibility(View.GONE);
                ismActivityConversationDetailsBinding.rvConversationGallery.setVisibility(View.VISIBLE);

                if (hasMoreItems) {
                    ismActivityConversationDetailsBinding.tvShowMoreGallery.setVisibility(View.VISIBLE);

                    ismActivityConversationDetailsBinding.tvShowMoreGallery.setOnClickListener(v -> {
                        Intent intent =
                                new Intent(ConversationDetailsActivity.this, GalleryMediaItemsActivity.class);
                        intent.putExtra("galleryMediaItemsSettingsUtil",
                                conversationDetailsPresenter.getGalleryMediaItemsSettingsUtil());
                        intent.putExtra("conversationId", conversationId);
                        startActivity(intent);
                    });
                } else {
                    ismActivityConversationDetailsBinding.tvShowMoreGallery.setVisibility(View.GONE);
                }

                ismActivityConversationDetailsBinding.rvConversationGallery.addOnItemTouchListener(
                        new RecyclerItemClickListener(this,
                                ismActivityConversationDetailsBinding.rvConversationGallery,
                                new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (position >= 0) {

                                            GalleryModel galleryModel = galleryItems.get(position);

                                            PreviewMessageUtil.previewMessage(ConversationDetailsActivity.this,
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
    public void onBackPressed() {
        cleanupOnActivityDestroy();
        KeyboardUtil.hideKeyboard(this);
        try {
            super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        cleanupOnActivityDestroy();
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
    public void onConversationMembersFetched(ArrayList<MembersWatchersModel> conversationMembers,
                                             int membersCount, boolean isAdmin, String requestingUsersOwnUserId) {
        members.clear();
        members.addAll(conversationMembers);
        membersAdapter.notifyDataSetChanged();
        this.membersCount = membersCount;
        ismActivityConversationDetailsBinding.tvParticipantsCount.setText(
                getString(R.string.ism_participants_count, membersCount));

        if (membersCount > members.size()) {
            ismActivityConversationDetailsBinding.tvShowMoreParticipants.setVisibility(View.VISIBLE);
            ismActivityConversationDetailsBinding.tvShowMoreParticipants.setOnClickListener(v -> {

                Intent intent = new Intent(ConversationDetailsActivity.this, MembersWatchersActivity.class);
                intent.putExtra("conversationId", conversationId);
                intent.putExtra("conversationTitle", conversationTitle);
                intent.putExtra("isUserAnAdmin", isAdmin);
                intent.putExtra("userId", requestingUsersOwnUserId);

                showMoreParticipantsActivityLauncher.launch(intent);
            });
        } else {
            ismActivityConversationDetailsBinding.tvShowMoreParticipants.setVisibility(View.GONE);
        }
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

    /**
     * Kick out member.
     *
     * @param memberId   the member id
     * @param memberName the member name
     */
    public void kickOutMember(String memberId, String memberName) {

        new AlertDialog.Builder(this).setTitle(
                        getString(R.string.ism_kick_out_member_heading, memberName))
                .setMessage(getString(R.string.ism_kick_out_member_alert_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                    dialog.cancel();
                    showProgressDialog(getString(R.string.ism_kicking_out_member, memberName));
                    conversationDetailsPresenter.kickOutMembers(conversationId,
                            Collections.singletonList(memberId));
                })
                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    /**
     * Make admin.
     *
     * @param memberId   the member id
     * @param memberName the member name
     */
    public void makeAdmin(String memberId, String memberName) {

        new AlertDialog.Builder(this).setTitle(
                        getString(R.string.ism_make_admin_alert_heading, memberName))
                .setMessage(getString(R.string.ism_make_admin_alert_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                    dialog.cancel();
                    showProgressDialog(getString(R.string.ism_making_admin, memberName));
                    conversationDetailsPresenter.makeAdmin(conversationId, memberId);
                })
                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    /**
     * Remove admin.
     *
     * @param memberId   the member id
     * @param memberName the member name
     */
    public void removeAdmin(String memberId, String memberName) {

        new AlertDialog.Builder(this).setTitle(
                        getString(R.string.ism_remove_as_admin_alert_heading, memberName))
                .setMessage(getString(R.string.ism_remove_as_admin_alert_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                    dialog.cancel();
                    showProgressDialog(getString(R.string.ism_removing_admin, memberName));
                    conversationDetailsPresenter.revokeAdminPermissions(conversationId, memberId);
                })
                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                .create()
                .show();
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
            ismActivityConversationDetailsBinding.tvTypingMessage.setText(
                    getString(R.string.ism_update_typing_message_setting, getString(R.string.ism_disable)));
        } else {
            ismActivityConversationDetailsBinding.tvTypingMessage.setText(
                    getString(R.string.ism_update_typing_message_setting, getString(R.string.ism_enable)));
        }

        if (deliveryReadEventsEnabled) {
            ismActivityConversationDetailsBinding.tvDeliveryReadEvents.setText(
                    getString(R.string.ism_update_read_delivery_setting, getString(R.string.ism_disable)));
        } else {
            ismActivityConversationDetailsBinding.tvDeliveryReadEvents.setText(
                    getString(R.string.ism_update_read_delivery_setting, getString(R.string.ism_enable)));
        }

        if (notificationsEnabled) {
            ismActivityConversationDetailsBinding.tvNotifications.setText(
                    getString(R.string.ism_update_notification_setting, getString(R.string.ism_disable)));
        } else {
            ismActivityConversationDetailsBinding.tvNotifications.setText(
                    getString(R.string.ism_update_notification_setting, getString(R.string.ism_enable)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean permissionDenied = false;
        if (requestCode == 0) {

            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permissionDenied = true;
                    break;
                }
            }
            if (permissionDenied) {
                Toast.makeText(this, getString(R.string.ism_permission_image_capture_denied),
                        Toast.LENGTH_LONG).show();
            } else {
                requestImageCapture();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onImageUploadResult(String url) {

        if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
            uploadProgressDialog.dismiss();
        }
        circularProgressIndicator = null;
        conversationImageUrl = url;
        showProgressDialog(getString(R.string.ism_updating_conversation_image));
        conversationDetailsPresenter.updateConversationImage(conversationId, conversationImageUrl);
    }

    @Override
    public void onImageUploadError(String errorMessage) {
        if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
            uploadProgressDialog.dismiss();
        }
        circularProgressIndicator = null;
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void requestPermissions() {
        ArrayList<String> permissionsRequired = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(ConversationDetailsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsRequired.add(Manifest.permission.CAMERA);
        }

        if (!Utilities.checkSelfExternalStoragePermissionIsGranted(ConversationDetailsActivity.this, true)) {
            permissionsRequired.addAll(Utilities.getPermissionsListForExternalStorage(true));
        }

        ActivityCompat.requestPermissions(ConversationDetailsActivity.this, permissionsRequired.toArray(new String[permissionsRequired.size()]), 0);

    }

    private void requestImageCapture() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cameraActivityLauncher.launch(new Intent(this, CameraActivity.class));
        } else {
            Toast.makeText(ConversationDetailsActivity.this, R.string.ism_image_capture_not_supported,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkAndRequestPermissions() {
        ArrayList<String> permissionsRequired = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsRequired.add(Manifest.permission.CAMERA);
        }
        if (!Utilities.checkSelfExternalStoragePermissionIsGranted(this, true)) {
            permissionsRequired.addAll(Utilities.getPermissionsListForExternalStorage(true));
        }

        if (!permissionsRequired.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsRequired.toArray(new String[0]), 0);
            return false;
        }
        return true;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityLauncher.launch(intent);
    }
    // Add this ActivityResultLauncher for gallery
    private ActivityResultLauncher<Intent> galleryActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        String imagePath = getPathFromUri(selectedImage);
                        if (imagePath != null) {
                            imageFile = new File(imagePath);
                            handleSelectedImage();
                        } else {
                            Toast.makeText(this, R.string.ism_invalid_image, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.ism_image_selection_canceled, Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void requestImageUpload() {
        conversationDetailsPresenter.requestImageUpload(
                String.valueOf(System.currentTimeMillis()), conversationId,
                imageFile.getAbsolutePath());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ism_dialog_uploading_image, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        uploadProgressDialog = builder.create();
        circularProgressIndicator = dialogView.findViewById(R.id.pbUpload);

        AppCompatButton btCancel = dialogView.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(v1 -> {
            conversationDetailsPresenter.cancelConversationImageUpload();
            uploadProgressDialog.dismiss();
            circularProgressIndicator = null;
        });

        uploadProgressDialog.show();
    }

    private void removeConversationImage() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.ism_remove_conversation_image))
                .setMessage(getString(R.string.ism_remove_conversation_image_confirm))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {
                    dialog.cancel();
                    showProgressDialog(getString(R.string.ism_updating_conversation_image));
                    conversationDetailsPresenter.updateConversationImage(conversationId, "");
                })
                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    private void deleteImage() {

        conversationDetailsPresenter.deleteImage(imageFile);
    }

    private void cleanupOnActivityDestroy() {
        if (!cleanUpRequested) {
            cleanUpRequested = true;
            hideProgressDialog();
            deleteImage();
        }
    }

    private void loadConversationImage() {
        ismActivityConversationDetailsBinding.ivConversationImage.setPadding(0, 0, 0, 0);
        if (PlaceholderUtils.isValidImageUrl(conversationImageUrl)) {

            try {
                Glide.with(this)
                        .load(conversationImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(new CircleCrop())
                        .into(ismActivityConversationDetailsBinding.ivConversationImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
        } else {
            PlaceholderUtils.setTextRoundDrawable(this, conversationTitle,
                    ismActivityConversationDetailsBinding.ivConversationImage, 33);
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
        ismActivityConversationDetailsBinding.pbGallery.setVisibility(View.GONE);
        updateShimmerVisibility(false, true);
        onError(error);
    }

    private void updateShimmerVisibility(boolean visible, boolean gallery) {
        if (gallery) {
            if (visible) {
                ismActivityConversationDetailsBinding.shimmerFrameLayoutGallery.startShimmer();
                ismActivityConversationDetailsBinding.shimmerFrameLayoutGallery.setVisibility(View.VISIBLE);
            } else {
                if (ismActivityConversationDetailsBinding.shimmerFrameLayoutGallery.getVisibility()
                        == View.VISIBLE) {
                    ismActivityConversationDetailsBinding.shimmerFrameLayoutGallery.setVisibility(View.GONE);
                    ismActivityConversationDetailsBinding.shimmerFrameLayoutGallery.stopShimmer();
                }
            }
        } else {
            if (visible) {
                ismActivityConversationDetailsBinding.incShimmer.shimmerFrameLayout.startShimmer();
                ismActivityConversationDetailsBinding.rlShimmer.setVisibility(View.VISIBLE);
            } else {
                if (ismActivityConversationDetailsBinding.rlShimmer.getVisibility() == View.VISIBLE) {
                    ismActivityConversationDetailsBinding.rlShimmer.setVisibility(View.GONE);
                    ismActivityConversationDetailsBinding.incShimmer.shimmerFrameLayout.stopShimmer();
                }
            }
        }
    }

    @Override
    public void onUploadProgressUpdate(int progress) {

        if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
            runOnUiThread(() -> {
                if (circularProgressIndicator != null) {
                    circularProgressIndicator.setProgress(progress);
                }
            });
        }
    }

    private void showImageOptionsDialog() {
        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.ism_take_photo));
        options.add(getString(R.string.ism_choose_from_library));
        if (conversationImageUrl != null && !conversationImageUrl.isEmpty()) {
            options.add(getString(R.string.ism_remove_photo));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.ism_change_conversation_image));
        builder.setItems(options.toArray(new String[0]), (dialog, which) -> {
            switch (which) {
                case 0: // Take a photo
                    if (checkAndRequestPermissions()) {
                        requestImageCapture();
                    }
                    break;
                case 1: // Choose from library
                    if (checkAndRequestPermissions()) {
                        openGallery();
                    }
                    break;
                case 2: // Remove photo
                    if (conversationImageUrl != null && !conversationImageUrl.isEmpty()) {
                        removeConversationImage();
                    }
                    break;
            }
        });
        builder.show();
    }

    private void showImageUploadConfirmationDialog() {
        if (imageFile != null && imageFile.exists()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.ism_update_conversation_image_alert_heading))
                    .setMessage(getString(R.string.ism_update_conversation_image_alert_message))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {
                        dialog.cancel();
                        requestImageUpload();
                    })
                    .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                    .create()
                    .show();
        } else {
            Toast.makeText(this, R.string.ism_invalid_conversation_image, Toast.LENGTH_SHORT).show();
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    private void handleSelectedImage() {
        ismActivityConversationDetailsBinding.ivConversationImage.setPadding(0, 0, 0, 0);
        try {
            Glide.with(this)
                    .load(imageFile.getAbsolutePath())
                    .transform(new CircleCrop())
                    .into(ismActivityConversationDetailsBinding.ivConversationImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        ismActivityConversationDetailsBinding.ivEditConversationImage.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ism_ic_done));
        editingImage = true;
        ismActivityConversationDetailsBinding.ivRemoveConversationImage.setVisibility(View.VISIBLE);
    }
}
