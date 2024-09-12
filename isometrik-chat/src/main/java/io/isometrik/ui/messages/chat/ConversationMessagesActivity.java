package io.isometrik.ui.messages.chat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;

import io.isometrik.chat.enums.AttachmentMessageType;
import io.isometrik.chat.enums.PresignedUrlMediaTypes;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.utils.FileUriUtils;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.camera.CameraActivity;
import io.isometrik.ui.conversations.details.observers.ObserversActivity;
import io.isometrik.chat.databinding.IsmActivityMessagesBinding;
import io.isometrik.ui.messages.action.MessageActionCallback;
import io.isometrik.ui.messages.action.MessageActionFragment;
import io.isometrik.ui.messages.action.edit.EditMessageFragment;
import io.isometrik.ui.messages.action.replies.SendMessageReplyFragment;
import io.isometrik.ui.messages.chat.utils.attachmentutils.PrepareAttachmentHelper;
import io.isometrik.ui.messages.chat.utils.enums.MessageTypesForUI;
import io.isometrik.ui.messages.chat.utils.enums.RemoteMessageTypes;
import io.isometrik.ui.messages.chat.utils.messageutils.ContactUtil;
import io.isometrik.ui.messages.chat.utils.messageutils.MultipleMessagesUtil;
import io.isometrik.ui.messages.chat.utils.messageutils.OriginalReplyMessageUtil;
import io.isometrik.ui.messages.deliverystatus.MessageDeliveryStatusActivity;
import io.isometrik.ui.messages.forward.ForwardMessageActivity;
import io.isometrik.ui.messages.media.MediaSelectedToBeShared;
import io.isometrik.ui.messages.media.MediaTypeToBeSharedCallback;
import io.isometrik.ui.messages.media.ShareMediaFragment;
import io.isometrik.ui.messages.media.audio.record.listeners.OnRecordListener;
import io.isometrik.ui.messages.media.audio.util.AudioFileUtil;
import io.isometrik.ui.messages.media.gifs.GifsFragment;
import io.isometrik.ui.messages.media.location.LocationUtils;
import io.isometrik.ui.messages.media.location.ShareLocationActivity;
import io.isometrik.ui.messages.media.stickers.StickersFragment;
import io.isometrik.ui.messages.media.whiteboard.WhiteboardFragment;
import io.isometrik.ui.messages.preview.PreviewMessageUtil;
import io.isometrik.ui.messages.reaction.add.AddReactionFragment;
import io.isometrik.ui.messages.reaction.add.ReactionModel;
import io.isometrik.ui.messages.reaction.list.FetchReactionUsersFragment;
import io.isometrik.ui.messages.reaction.util.ReactionClickListener;
import io.isometrik.ui.messages.tag.MemberDetailsFragment;
import io.isometrik.ui.messages.tag.TagUserAdapter;
import io.isometrik.ui.messages.tag.TagUserModel;
import io.isometrik.ui.messages.tag.TaggedUserCallback;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.GalleryIntentsUtil;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.KeyboardUtil;
import io.isometrik.chat.utils.MentionedUserSpan;
import io.isometrik.chat.utils.PlaceholderUtils;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import io.isometrik.chat.utils.TagUserUtil;
import io.isometrik.chat.utils.TimeUtil;
import io.isometrik.chat.utils.Utilities;
import io.isometrik.chat.utils.enums.CustomMessageTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * The activity to send/receive messages in realtime of type- image/video/file/contact/location/whiteboard/sticker/gif/audio/text,
 * forward or add reaction to a message, search for message, capture image from camera/record
 * audio, tag a member in a text message, fetch messages with paging and pull to refresh, take
 * various action like message
 * delete/edit on
 * long press of a message cell, multiselect messages, join/leave as observer and fetch observers
 * list for open conversation,
 * fetch opponent's online or last seen status for 1-1 conversation, update UI on receiving
 * realtime
 * message or various actions in conversation, initiate/cancel of upload/download media message,
 * copy text messages, send typing message.
 */
public class ConversationMessagesActivity extends AppCompatActivity implements ConversationMessagesContract.View, ReactionClickListener, MediaTypeToBeSharedCallback, MediaSelectedToBeShared, MessageActionCallback, TaggedUserCallback {

    private ConversationMessagesContract.Presenter conversationMessagesPresenter;
    private IsmActivityMessagesBinding ismActivityMessagesBinding;
    private final ArrayList<MessagesModel> messages = new ArrayList<>();
    private ConversationMessagesAdapter conversationMessagesAdapter;
    private final ArrayList<TagUserModel> tagUserModels = new ArrayList<>();
    private TagUserAdapter tagUserAdapter;

    private AddReactionFragment addReactionFragment;
    private FetchReactionUsersFragment fetchReactionUsersFragment;
    private GifsFragment gifsFragment;
    private StickersFragment stickersFragment;
    private ShareMediaFragment shareMediaFragment;
    private WhiteboardFragment whiteboardFragment;
    private SendMessageReplyFragment sendMessageReplyFragment;
    private MessageActionFragment messageActionFragment;
    private MemberDetailsFragment memberDetailsFragment;
    private EditMessageFragment editMessageFragment;

    private boolean unregisteredListeners, scrollToMessageNeeded;
    private String conversationId;

    private static final int DOWNLOAD_MEDIA_PERMISSIONS_REQUEST_CODE = 0;
    private static final int SHARE_LOCATION_PERMISSIONS_REQUEST_CODE = 1;
    private static final int RECORD_AUDIO_PERMISSIONS_REQUEST_CODE = 2;
    private static final int CAPTURE_IMAGE_PERMISSIONS_REQUEST_CODE = 3;
    private static final int SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE = 4;
    private static final int SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE = 5;
    private static final int SHARE_FILES_PERMISSIONS_REQUEST_CODE = 6;
    private static final int SHARE_CONTACT_PERMISSIONS_REQUEST_CODE = 7;

    private ActivityResultLauncher<Intent> conversationDetailsActivityLauncher;
    private ActivityResultLauncher<Intent> userDetailsActivityLauncher;
    private ActivityResultLauncher<Intent> cameraActivityLauncher;
    private ActivityResultLauncher<Intent> multiplePhotosPicker;
    private ActivityResultLauncher<Intent> multipleVideosPicker;
    private ActivityResultLauncher<Intent> multipleFilesPicker;
    private ActivityResultLauncher<Intent> shareLocationActivityLauncher;
    private ActivityResultLauncher<Intent> contactPicker;

    private LinearLayoutManager messagesLayoutManager;
    private final Handler handler = new Handler();
    private final List<MentionedUserSpan> spansToRemove = new CopyOnWriteArrayList<>();

    private boolean messagingDisabled, joiningAsObserver;
    private boolean firstResume = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityMessagesBinding = IsmActivityMessagesBinding.inflate(getLayoutInflater());
        View view = ismActivityMessagesBinding.getRoot();
        setContentView(view);
        updateShimmerVisibility(true);
        conversationMessagesPresenter = new ConversationMessagesPresenter(this, this);

        messagingDisabled = getIntent().getExtras().containsKey("messagingDisabled");
        joiningAsObserver = getIntent().getExtras().getBoolean("joinAsObserver", false);

        messagesLayoutManager = new LinearLayoutManager(this);
        ismActivityMessagesBinding.rvMessages.setLayoutManager(messagesLayoutManager);
        conversationMessagesAdapter = new ConversationMessagesAdapter(this, messages, this, messagingDisabled, joiningAsObserver);
        ismActivityMessagesBinding.rvMessages.setAdapter(conversationMessagesAdapter);
        ismActivityMessagesBinding.rvMessages.addOnScrollListener(messagesRecyclerViewOnScrollListener);

        ismActivityMessagesBinding.vTagUsers.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        tagUserAdapter = new TagUserAdapter(this, tagUserModels);
        ismActivityMessagesBinding.vTagUsers.rvUsers.setAdapter(tagUserAdapter);

        conversationId = getIntent().getExtras().getString("conversationId");

        boolean isPrivateOneToOne = getIntent().getExtras().getBoolean("isPrivateOneToOne");

        conversationMessagesPresenter.initializeConversation(conversationId, getIntent().getExtras().getBoolean("messageDeliveryReadEventsEnabled"), getIntent().getExtras().getBoolean("typingEventsEnabled"), isPrivateOneToOne, getIntent().getExtras(), joiningAsObserver);

        if (messagingDisabled) {
            onMessagingStatusChanged(true);
        }
        updateConversationDetailsInHeader(true, isPrivateOneToOne, null, false, 0, null, 0);

        scrollToMessageNeeded = getIntent().getBooleanExtra("scrollToMessageNeeded", false);
        if (scrollToMessageNeeded) {
            ismActivityMessagesBinding.ivSearch.setVisibility(View.GONE);
            ismActivityMessagesBinding.vLoadingOverlay.getRoot().setVisibility(View.VISIBLE);
        }

        if (joiningAsObserver) {
            ismActivityMessagesBinding.ivSearch.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ismActivityMessagesBinding.ivObservers.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END);

            ismActivityMessagesBinding.ivObservers.setLayoutParams(params);
            conversationMessagesPresenter.joinAsObserver();
        } else {
            fetchMessages(false, null, false);
        }
        conversationMessagesPresenter.registerConnectionEventListener();
        conversationMessagesPresenter.registerConversationEventListener();
        conversationMessagesPresenter.registerMessageEventListener();
        conversationMessagesPresenter.registerMembershipControlEventListener();
        conversationMessagesPresenter.registerReactionEventListener();
        conversationMessagesPresenter.registerUserEventListener();

        if (!joiningAsObserver && !getIntent().getExtras().getBoolean("newConversation", false)) {
            conversationMessagesPresenter.markMessagesAsRead();
        }
        ismActivityMessagesBinding.refresh.setOnRefreshListener(() -> {
            if (joiningAsObserver) {
                ismActivityMessagesBinding.refresh.setRefreshing(false);
            } else {
                fetchMessages(false, null, true);
            }
        });
        addReactionFragment = new AddReactionFragment();
        fetchReactionUsersFragment = new FetchReactionUsersFragment();
        gifsFragment = new GifsFragment();
        stickersFragment = new StickersFragment();
        shareMediaFragment = new ShareMediaFragment();
        whiteboardFragment = new WhiteboardFragment();
        sendMessageReplyFragment = new SendMessageReplyFragment();
        messageActionFragment = new MessageActionFragment();
        memberDetailsFragment = new MemberDetailsFragment();
        editMessageFragment = new EditMessageFragment();

        ismActivityMessagesBinding.ibBack.setOnClickListener(v -> onBackPressed());

        ismActivityMessagesBinding.ivAddAttachment.setOnClickListener(v -> {
            if (!isFinishing() && !shareMediaFragment.isAdded()) {
                dismissAllDialogs();
                shareMediaFragment.updateParameters(this);
                shareMediaFragment.show(getSupportFragmentManager(), ShareMediaFragment.TAG);
            }
        });

        conversationDetailsActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    if (result.getData().getBooleanExtra("conversationLeftOrDeleted", false)) {
                        onBackPressed();
                    } else if (result.getData().getBooleanExtra("searchMessageRequested", false)) {
                        if (ismActivityMessagesBinding.rlSearch.getVisibility() == View.GONE) {
                            ismActivityMessagesBinding.rlSearch.setVisibility(View.VISIBLE);
                        }
                        KeyboardUtil.showSoftKeyboard(this, ismActivityMessagesBinding.etSearch);
                    }
                }
            }
        });

        userDetailsActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    if (result.getData().getBooleanExtra("conversationLeftOrDeleted", false)) {
                        onBackPressed();
                    } else if (result.getData().getBooleanExtra("messagingBlocked", false)) {
                        onMessagingStatusChanged(true);
                    } else if (result.getData().getBooleanExtra("searchMessageRequested", false)) {
                        if (ismActivityMessagesBinding.rlSearch.getVisibility() == View.GONE) {
                            ismActivityMessagesBinding.rlSearch.setVisibility(View.VISIBLE);
                        }
                        KeyboardUtil.showSoftKeyboard(this, ismActivityMessagesBinding.etSearch);
                    }
                }
            }
        });

        cameraActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {

                    conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Image.getValue(), CustomMessageTypes.Image.getValue(), false, true, true, true, null, null, null, MessageTypesForUI.PhotoSent, new ArrayList<>(Collections.singletonList(result.getData().getStringExtra("capturedImagePath"))), true, PresignedUrlMediaTypes.Photo, AttachmentMessageType.Image);
                }
            } else {
                Toast.makeText(this, R.string.ism_image_capture_canceled, Toast.LENGTH_SHORT).show();
            }
        });

        multiplePhotosPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    ArrayList<String> photoPaths = GalleryIntentsUtil.getFilePaths(result.getData(), this);

                    if (photoPaths == null) {
                        Toast.makeText(this, getString(R.string.ism_photos_selection_limit_exceeded, Constants.MAXIMUM_MEDIA_SELECTION_COUNT_AT_ONCE), Toast.LENGTH_SHORT).show();
                    } else {

                        if (photoPaths.isEmpty()) {
                            Toast.makeText(this, R.string.ism_photos_selected_deleted, Toast.LENGTH_SHORT).show();
                        } else {
                            conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Image.getValue(), CustomMessageTypes.Image.getValue(), false, true, true, true, null, null, null, MessageTypesForUI.PhotoSent, photoPaths, true, PresignedUrlMediaTypes.Photo, AttachmentMessageType.Image);
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.ism_photos_selection_failed, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.ism_photos_selection_canceled, Toast.LENGTH_SHORT).show();
            }
        });

        multipleVideosPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    ArrayList<String> videoPaths = GalleryIntentsUtil.getFilePaths(result.getData(), this);
                    if (videoPaths == null) {
                        Toast.makeText(this, getString(R.string.ism_videos_selection_limit_exceeded, Constants.MAXIMUM_MEDIA_SELECTION_COUNT_AT_ONCE), Toast.LENGTH_SHORT).show();
                    } else {
                        if (videoPaths.isEmpty()) {
                            Toast.makeText(this, R.string.ism_videos_selected_deleted, Toast.LENGTH_SHORT).show();
                        } else {
                            conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Video.getValue(), CustomMessageTypes.Video.getValue(), false, true, true, true, null, null, null, MessageTypesForUI.VideoSent, videoPaths, true, PresignedUrlMediaTypes.Video, AttachmentMessageType.Video);
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.ism_videos_selection_failed, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.ism_videos_selection_canceled, Toast.LENGTH_SHORT).show();
            }
        });

        multipleFilesPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {

                    String path =  FileUriUtils.INSTANCE.getRealPath(this,result.getData().getData());
//                    ArrayList<String> filePaths = GalleryIntentsUtil.getFilePaths(result.getData(), this);
                    ArrayList<String> filePaths = new ArrayList<>();
                    filePaths.add(path);
                    if (filePaths == null) {
                        Toast.makeText(this, getString(R.string.ism_files_selection_limit_exceeded, Constants.MAXIMUM_MEDIA_SELECTION_COUNT_AT_ONCE), Toast.LENGTH_SHORT).show();
                    } else {
                        if (filePaths.isEmpty()) {
                            Toast.makeText(this, R.string.ism_files_selected_deleted, Toast.LENGTH_SHORT).show();
                        } else {
                            conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.File.getValue(), CustomMessageTypes.File.getValue(), false, true, true, true, null, null, null, MessageTypesForUI.FileSent, filePaths, true, PresignedUrlMediaTypes.File, AttachmentMessageType.File);
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.ism_files_selection_failed, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.ism_files_selection_canceled, Toast.LENGTH_SHORT).show();
            }
        });

        shareLocationActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Intent data = result.getData();

                //Location data received
                String locationName = data.getStringExtra("locationName");
                String locationAddress = data.getStringExtra("locationAddress");
                double latitude = Double.parseDouble(data.getStringExtra("latitude"));
                double longitude = Double.parseDouble(data.getStringExtra("longitude"));

                //Log.d("log1", locationName + "*" + locationAddress + "*" + latitude + "*" + longitude);
                Attachment locationAttachment = PrepareAttachmentHelper.prepareLocationAttachment(locationName, locationAddress, latitude, longitude);

                if (locationAttachment == null) {
                    Toast.makeText(this, R.string.ism_location_share_failed, Toast.LENGTH_SHORT).show();
                } else {
                    conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Location.getValue(), CustomMessageTypes.Location.getValue(), false, true, true, true, new ArrayList<>(Collections.singletonList(locationAttachment)), null, null, MessageTypesForUI.LocationSent, null, false, null, null);
                }
            } else {
                Toast.makeText(this, R.string.ism_location_selection_canceled, Toast.LENGTH_SHORT).show();
            }
        });

        contactPicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {

                    JSONObject messageMetadata = ContactUtil.parseContactsData(result.getData(), getContentResolver());

                    if (messageMetadata == null) {
                        Toast.makeText(this, R.string.ism_contact_selection_failed, Toast.LENGTH_SHORT).show();
                    } else {
                        conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Contact.getValue(), CustomMessageTypes.Contact.getValue(), false, true, true, true, null, messageMetadata, null, MessageTypesForUI.ContactSent, null, false, null, null);
                    }
                } else {
                    Toast.makeText(this, R.string.ism_contact_selection_failed, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.ism_contact_selection_canceled, Toast.LENGTH_SHORT).show();
            }
        });

        ismActivityMessagesBinding.ivCaptureImage.setOnClickListener(v -> checkImageCapturePermissions());

        ismActivityMessagesBinding.rlConversationDetails.setOnClickListener(v -> {
            if (ismActivityMessagesBinding.vSelectMultipleMessagesHeader.getRoot().getVisibility() == View.GONE && clickActionsNotBlocked()) {
                KeyboardUtil.hideKeyboard(this);

                Intent intent = conversationMessagesPresenter.getConversationDetailsIntent(this, isPrivateOneToOne);
                if (isPrivateOneToOne) {
                    if (!messagingDisabled) userDetailsActivityLauncher.launch(intent);
                } else {
                    if (!joiningAsObserver) conversationDetailsActivityLauncher.launch(intent);
                }
            }
        });
        ismActivityMessagesBinding.etSendMessage.addTextChangedListener(sendMessageTextWatcher);

        ismActivityMessagesBinding.vTagUsers.rvUsers.addOnItemTouchListener(new RecyclerItemClickListener(this, ismActivityMessagesBinding.vTagUsers.rvUsers, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position >= 0) {

                    if (ismActivityMessagesBinding.etSendMessage.getText() != null) {
                        ismActivityMessagesBinding.etSendMessage.removeTextChangedListener(sendMessageTextWatcher);

                        ismActivityMessagesBinding.etSendMessage.setText(new SpannableString(TagUserUtil.addTaggedUsername((SpannableStringBuilder) ismActivityMessagesBinding.etSendMessage.getText(), ismActivityMessagesBinding.etSendMessage.getSelectionStart(), tagUserModels.get(position), ConversationMessagesActivity.this)));

                        ismActivityMessagesBinding.vTagUsers.getRoot().setVisibility(View.GONE);
                        ismActivityMessagesBinding.etSendMessage.addTextChangedListener(sendMessageTextWatcher);
                        ismActivityMessagesBinding.etSendMessage.setSelection(ismActivityMessagesBinding.etSendMessage.getText().length());
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

        ismActivityMessagesBinding.ivSendMessage.setOnClickListener(v -> {
            if (ismActivityMessagesBinding.etSendMessage.getText() != null && ismActivityMessagesBinding.etSendMessage.getText().length() > 0) {
                conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Text.getValue(), ismActivityMessagesBinding.etSendMessage.getText().toString(), false, true, true, true, null, null, TagUserUtil.prepareMentionedUsers(ismActivityMessagesBinding.etSendMessage.getEditableText()), MessageTypesForUI.TextSent, null, false, null, null);
                ismActivityMessagesBinding.etSendMessage.setText(null);
            } else {
                Toast.makeText(ConversationMessagesActivity.this, getString(R.string.ism_type_something), Toast.LENGTH_SHORT).show();
            }
        });

        ismActivityMessagesBinding.rvMessages.addOnItemTouchListener(new RecyclerItemClickListener(this, ismActivityMessagesBinding.rvMessages, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    if (ismActivityMessagesBinding.vSelectMultipleMessagesHeader.getRoot().getVisibility() == View.VISIBLE) {

                        if (!messages.get(position).getCustomMessageType().equals(MessageTypesForUI.ConversationActionMessage)) {
                            MessagesModel messagesModel = messages.get(position);
                            boolean selected = !messagesModel.isSelected();
                            messagesModel.setSelected(selected);
                            conversationMessagesPresenter.updateMessageSelectionStatus(messagesModel, selected);
                            messages.set(position, messagesModel);
                            conversationMessagesAdapter.notifyItemChanged(position);
                        }
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

                if (position >= 0) {
                    if (!messagingDisabled) {
                        if (!messages.get(position).getCustomMessageType().equals(MessageTypesForUI.ConversationActionMessage)) {
                            if (ismActivityMessagesBinding.vSelectMultipleMessagesHeader.getRoot().getVisibility() == View.VISIBLE) {
                                MessagesModel messagesModel = messages.get(position);
                                boolean selected = !messagesModel.isSelected();
                                messagesModel.setSelected(selected);
                                conversationMessagesPresenter.updateMessageSelectionStatus(messagesModel, selected);
                                messages.set(position, messagesModel);
                                conversationMessagesAdapter.notifyItemChanged(position);
                            } else {

                                MessagesModel messagesModel = messages.get(position);
                                if (!messagesModel.isSentMessage() || messagesModel.isMessageSentSuccessfully()) {
                                    if (!joiningAsObserver && clickActionsNotBlocked()) {
                                        if (!isFinishing() && !messageActionFragment.isAdded()) {
                                            dismissAllDialogs();
                                            messageActionFragment.updateParameters(messages.get(position), ConversationMessagesActivity.this, position);
                                            messageActionFragment.show(getSupportFragmentManager(), MessageActionFragment.TAG);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }));

        ismActivityMessagesBinding.vSelectMultipleMessagesHeader.ibClose.setOnClickListener(v -> {
            ismActivityMessagesBinding.vSelectMultipleMessagesHeader.getRoot().setVisibility(View.GONE);
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.getRoot().setVisibility(View.GONE);

            int size = messages.size();
            MessagesModel messagesModel;
            conversationMessagesPresenter.cleanupSelectedMessages();
            conversationMessagesAdapter.setMultipleMessagesSelectModeOn(false);
            for (int i = 0; i < size; i++) {

                if (messages.get(i).isSelected()) {
                    messagesModel = messages.get(i);
                    messagesModel.setSelected(false);
                    messages.set(i, messagesModel);
                }
            }
            conversationMessagesAdapter.notifyDataSetChanged();
        });

        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.rlDeleteForMe.setOnClickListener(v -> {
            if (ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivDeleteForMe.isSelected()) {
                deleteMessageForSelf(null, true);
            }
        });

        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.rlDeleteForAll.setOnClickListener(v -> {
            if (ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivDeleteForAll.isSelected()) {
                deleteMessageForEveryone(null, true);
            }
        });

        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.rlCopy.setOnClickListener(v -> {
            if (ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivCopy.isSelected()) {
                conversationMessagesPresenter.copyText();
            }
        });

        ismActivityMessagesBinding.btRecord.setRecordView(ismActivityMessagesBinding.vRecordView);
        ismActivityMessagesBinding.vRecordView.setLockEnabled(true);
        ismActivityMessagesBinding.vRecordView.setRecordLockImageView(ismActivityMessagesBinding.vRecordLock);
        ismActivityMessagesBinding.vRecordView.setTimeLimit(Constants.MAXIMUM_AUDIO_RECORDING_DURATION_MILLISECONDS);
        ismActivityMessagesBinding.vRecordView.setActivityContext(this);

        ismActivityMessagesBinding.vRecordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart(boolean hasRecordingPermission) {
                //KeyboardUtil.hideKeyboard(ConversationMessagesActivity.this);
                if (hasRecordingPermission) {
                    ismActivityMessagesBinding.rlBottomLayout.setVisibility(View.INVISIBLE);
                    conversationMessagesPresenter.startAudioRecording(ConversationMessagesActivity.this);
                } else {
                    if (ActivityCompat.checkSelfPermission(ConversationMessagesActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(ConversationMessagesActivity.this, Manifest.permission.RECORD_AUDIO)) {
                            Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), getString(R.string.ism_request_record_audio_permission), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ism_ok), view1 -> ActivityCompat.requestPermissions(ConversationMessagesActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSIONS_REQUEST_CODE));

                            snackbar.show();
                        } else {
                            ActivityCompat.requestPermissions(ConversationMessagesActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSIONS_REQUEST_CODE);
                        }
                    }
                }
            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                conversationMessagesPresenter.stopAudioRecording(true);
            }

            @Override
            public void onFinish(long recordTime, boolean limitReached) {
                //Stop Recording..
                //limitReached to determine if the Record was finished when time limit reached.
                ismActivityMessagesBinding.rlBottomLayout.setVisibility(View.VISIBLE);
                ismActivityMessagesBinding.btRecord.setVisibility(View.VISIBLE);
                conversationMessagesPresenter.stopAudioRecording(false);
            }

            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second
                ismActivityMessagesBinding.rlBottomLayout.setVisibility(View.VISIBLE);
                ismActivityMessagesBinding.btRecord.setVisibility(View.VISIBLE);
                conversationMessagesPresenter.stopAudioRecording(true);
            }

            @Override
            public void switchedToLockedMode() {
                ismActivityMessagesBinding.btRecord.setVisibility(View.GONE);
            }
        });

        ismActivityMessagesBinding.vRecordView.setOnBasketAnimationEndListener(() -> {
            ismActivityMessagesBinding.rlBottomLayout.setVisibility(View.VISIBLE);
            ismActivityMessagesBinding.btRecord.setVisibility(View.VISIBLE);
        });

        ismActivityMessagesBinding.ivSearch.setOnClickListener(v -> {
            if (ismActivityMessagesBinding.vSelectMultipleMessagesHeader.getRoot().getVisibility() == View.GONE && clickActionsNotBlocked()) {
                if (ismActivityMessagesBinding.rlSearch.getVisibility() == View.VISIBLE) {

                    KeyboardUtil.hideKeyboard(this);
                    ismActivityMessagesBinding.rlSearch.setVisibility(View.GONE);
                } else {
                    ismActivityMessagesBinding.rlSearch.setVisibility(View.VISIBLE);
                    KeyboardUtil.showSoftKeyboard(this, ismActivityMessagesBinding.etSearch);
                }
            }
        });

        ismActivityMessagesBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!joiningAsObserver) {
                    if (s.length() > 0) {
                        fetchMessages(true, s.toString(), false);
                    } else {

                        fetchMessages(false, null, false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ismActivityMessagesBinding.rlDeleteConversation.setOnClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(this).setTitle(getString(R.string.ism_delete_conversation)).setMessage(getString(R.string.ism_delete_conversation_alert)).setCancelable(true).setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                dialog.cancel();
                conversationMessagesPresenter.deleteConversation();
            }).setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel()).create().show();
        });

        if (getIntent().getBooleanExtra("fromNotification", false)) {

            try {
                IsometrikUiSdk.getInstance().getIsometrik().getExecutor().execute(() -> IsometrikUiSdk.getInstance().getIsometrik().createConnection(IsometrikUiSdk.getInstance().getUserSession().getUserId() + IsometrikUiSdk.getInstance().getUserSession().getDeviceId(), IsometrikUiSdk.getInstance().getUserSession().getUserToken()));
            } catch (Exception ignore) {

            }
        }

        ismActivityMessagesBinding.ivRefreshOnlineStatus.setOnClickListener(v -> {

            if (ismActivityMessagesBinding.vSelectMultipleMessagesHeader.getRoot().getVisibility() == View.GONE && clickActionsNotBlocked()) {
                conversationMessagesPresenter.requestConversationDetails();
            }
        });

        ismActivityMessagesBinding.ivObservers.setOnClickListener(v -> {
            Intent intent = new Intent(ConversationMessagesActivity.this, ObserversActivity.class);
            intent.putExtra("conversationId", conversationId);
            startActivity(intent);
        });
    }

    private final TextWatcher sendMessageTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (count > 0) {
                int end = start + count;
                Editable message = ismActivityMessagesBinding.etSendMessage.getEditableText();
                MentionedUserSpan[] list = message.getSpans(start, end, MentionedUserSpan.class);

                for (MentionedUserSpan span : list) {
                    int spanStart = message.getSpanStart(span);
                    int spanEnd = message.getSpanEnd(span);
                    if ((spanStart < end) && (spanEnd > start)) {

                        spansToRemove.add(span);
                    }
                }
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) {
                ismActivityMessagesBinding.rlRecordAudio.setVisibility(View.GONE);
                ismActivityMessagesBinding.ivCaptureImage.setVisibility(View.INVISIBLE);
                ismActivityMessagesBinding.ivSendMessage.setVisibility(View.VISIBLE);
            } else {
                ismActivityMessagesBinding.ivSendMessage.setVisibility(View.GONE);
                ismActivityMessagesBinding.ivCaptureImage.setVisibility(View.VISIBLE);
                ismActivityMessagesBinding.rlRecordAudio.setVisibility(View.VISIBLE);
            }
            if (!joiningAsObserver) conversationMessagesPresenter.sendTypingMessage();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!joiningAsObserver) {
                if (s.length() > 0) {
                    String searchTag = TagUserUtil.getTaggedUsernameToSearch(s.toString(), ismActivityMessagesBinding.etSendMessage.getSelectionStart());

                    if (searchTag != null) {
                        conversationMessagesPresenter.searchUserToTag(searchTag);
                    } else {
                        ismActivityMessagesBinding.vTagUsers.getRoot().setVisibility(View.GONE);
                    }
                } else {
                    ismActivityMessagesBinding.vTagUsers.getRoot().setVisibility(View.GONE);
                }

                Editable message = ismActivityMessagesBinding.etSendMessage.getEditableText();

                for (MentionedUserSpan span : spansToRemove) {

                    int start = message.getSpanStart(span);
                    int end = message.getSpanEnd(span);

                    message.removeSpan(span);
                    if (start != end) {
                        message.delete(start, end);
                    }
                }
                spansToRemove.clear();
            }
        }
    };

    @Override
    public void onMediaDownloadedComplete(boolean successfullyCompleted, String messageId, String mediaTypeDownloadedMessage, int messagePosition, String downloadedMediaPath) {
        runOnUiThread(() -> {

            if (!successfullyCompleted) {
                Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), mediaTypeDownloadedMessage, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            int position = conversationMessagesPresenter.verifyMessagePositionInList(messageId, messagePosition, messages);
            if (position != -1) {
                conversationMessagesAdapter.updateMessageStatusOnMediaDownloadFinished(position, successfullyCompleted, downloadedMediaPath);
            }
        });
    }

    @Override
    public void onMediaDownloadCanceled(boolean successfullyCanceled, String messageId, String mediaTypeDownloadCanceledMessage, int messagePosition) {
        runOnUiThread(() -> {
            if (!successfullyCanceled) {
                Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), mediaTypeDownloadCanceledMessage, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            int position = conversationMessagesPresenter.verifyMessagePositionInList(messageId, messagePosition, messages);
            if (position != -1) {
                conversationMessagesAdapter.updateMessageStatusOnDownloadingStateChanged(position, false);
            }
        });
    }

    @Override
    public void onMediaUploadCanceled(boolean successfullyCanceled, String localMessageId, String mediaTypeUploadCanceledMessage, int messagePosition) {
        runOnUiThread(() -> {
            if (!successfullyCanceled) {
                Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), mediaTypeUploadCanceledMessage, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            int position = conversationMessagesPresenter.verifyUnsentMessagePositionInList(localMessageId, messagePosition, messages);
            if (position != -1) {
                MessagesModel messagesModel = messages.get(position);
                messagesModel.setUploading(false);
                messages.set(position, messagesModel);
                conversationMessagesAdapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void onDownloadProgressUpdate(String messageId, int messagePosition, int progress) {
        runOnUiThread(() -> {

            int position = conversationMessagesPresenter.verifyMessagePositionInList(messageId, messagePosition, messages);
            if (position != -1) {
                conversationMessagesAdapter.updateProgressStatusOfMessage(true, position, ismActivityMessagesBinding.rvMessages, progress);
            }
        });
    }

    @Override
    public void onUploadProgressUpdate(String localMessageId, String mediaId, int messagePosition, int progress) {
        runOnUiThread(() -> {
            int position = conversationMessagesPresenter.verifyUnsentMessagePositionInList(localMessageId, messagePosition, messages);
            if (position != -1) {
                conversationMessagesAdapter.updateProgressStatusOfMessage(false, position, ismActivityMessagesBinding.rvMessages, progress);
            }
        });
    }

    @Override
    public void onFailedToSendMessage(String localMessageId, String error) {
        runOnUiThread(() -> {
            int position = conversationMessagesPresenter.verifyUnsentMessagePositionInList(localMessageId, messages.size() - 1, messages);
            if (position != -1) {
                MessagesModel messagesModel = messages.get(position);
                messagesModel.setSendingMessageFailed(true);
                messages.set(position, messagesModel);
                conversationMessagesAdapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void onMessageReactionClicked(String messageId, ReactionModel reactionModel) {
        if (!isFinishing() && !fetchReactionUsersFragment.isAdded()) {
            dismissAllDialogs();
            fetchReactionUsersFragment.updateParameters(conversationId, messageId, reactionModel, this, messagingDisabled);
            fetchReactionUsersFragment.show(getSupportFragmentManager(), FetchReactionUsersFragment.TAG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case CAPTURE_IMAGE_PERMISSIONS_REQUEST_CODE: {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestImageCapture();
                } else {
                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_permission_image_capture_denied, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            }

            case DOWNLOAD_MEDIA_PERMISSIONS_REQUEST_CODE: {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_storage_permission_to_download_granted, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_storage_permission_to_download_denied, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

                break;
            }
            case SHARE_LOCATION_PERMISSIONS_REQUEST_CODE: {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestLocationShare();
                } else {

                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_location_permission_denied, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            }
            case RECORD_AUDIO_PERMISSIONS_REQUEST_CODE: {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_press_hold_to_record_audio, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_record_audio_denied, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            }
            case SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE: {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestMediaShareFromStorage(SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE);
                } else {
                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_permission_photos_share_denied, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            }
            case SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE: {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestMediaShareFromStorage(SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE);
                } else {
                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_permission_videos_share_denied, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            }
            case SHARE_FILES_PERMISSIONS_REQUEST_CODE: {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestMediaShareFromStorage(SHARE_FILES_PERMISSIONS_REQUEST_CODE);
                } else {
                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_permission_files_share_denied, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            }
            case SHARE_CONTACT_PERMISSIONS_REQUEST_CODE: {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestContactShare();
                } else {
                    Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_permission_contact_share_denied, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            }
        }
    }

    @Override
    public void onMediaTypeToBeSharedSelected(MessageTypesForUI mediaTypeSelected) {

        switch (mediaTypeSelected) {

            case PhotoSent: {
                //Photos
                checkAccessStoragePermissions(SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE, getString(R.string.ism_permission_photos_share));
                break;
            }
            case VideoSent: {
                //Videos
                checkAccessStoragePermissions(SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE, getString(R.string.ism_permission_videos_share));
                break;
            }
            case FileSent: {
                //Files
                checkAccessStoragePermissions(SHARE_FILES_PERMISSIONS_REQUEST_CODE, getString(R.string.ism_permission_files_share));
                break;
            }

            case StickerSent: {
                //Sticker
                if (!isFinishing() && !stickersFragment.isAdded()) {
                    dismissAllDialogs();
                    stickersFragment.updateParameters(this);
                    stickersFragment.show(getSupportFragmentManager(), StickersFragment.TAG);
                }
                break;
            }
            case GifSent: {
                //Gif
                if (!isFinishing() && !gifsFragment.isAdded()) {
                    dismissAllDialogs();
                    gifsFragment.updateParameters(this);
                    gifsFragment.show(getSupportFragmentManager(), GifsFragment.TAG);
                }
                break;
            }
            case WhiteboardSent: {
                //Whiteboard
                if (!isFinishing() && !whiteboardFragment.isAdded()) {
                    dismissAllDialogs();
                    whiteboardFragment.updateParameters(this);
                    whiteboardFragment.show(getSupportFragmentManager(), WhiteboardFragment.TAG);
                }
                break;
            }
            case LocationSent: {
                //Location
                if (ActivityCompat.checkSelfPermission(ConversationMessagesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    requestLocationShare();
                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(ConversationMessagesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), getString(R.string.ism_request_location_permission), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ism_ok), view -> ActivityCompat.requestPermissions(ConversationMessagesActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SHARE_LOCATION_PERMISSIONS_REQUEST_CODE));

                        snackbar.show();
                    } else {
                        ActivityCompat.requestPermissions(ConversationMessagesActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, SHARE_LOCATION_PERMISSIONS_REQUEST_CODE);
                    }
                }
                break;
            }
            case ContactSent: {
                //Contact
                if (ActivityCompat.checkSelfPermission(ConversationMessagesActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    requestContactShare();
                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(ConversationMessagesActivity.this, Manifest.permission.READ_CONTACTS)) {
                        Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), getString(R.string.ism_permission_contact_share), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ism_ok), view -> ActivityCompat.requestPermissions(ConversationMessagesActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, SHARE_CONTACT_PERMISSIONS_REQUEST_CODE));

                        snackbar.show();
                    } else {
                        ActivityCompat.requestPermissions(ConversationMessagesActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, SHARE_CONTACT_PERMISSIONS_REQUEST_CODE);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void gifShareRequested(String gifName, String gifImageUrl, String gifStillUrl) {
        Attachment gifAttachment = PrepareAttachmentHelper.prepareGifAttachment(gifName, gifImageUrl, gifStillUrl, gifStillUrl);
        dismissAllDialogs();
        if (gifAttachment == null) {
            Toast.makeText(this, R.string.ism_gif_share_failed, Toast.LENGTH_SHORT).show();
        } else {
            conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Gif.getValue(), CustomMessageTypes.Gif.getValue(), false, true, true, true, new ArrayList<>(Collections.singletonList(gifAttachment)), null, null, MessageTypesForUI.GifSent, null, false, null, null);
        }
    }

    @Override
    public void stickerShareRequested(String stickerName, String stickerImageUrl) {
        Attachment stickerAttachment = PrepareAttachmentHelper.prepareStickerAttachment(stickerName, stickerImageUrl, stickerImageUrl, stickerImageUrl);

        dismissAllDialogs();
        if (stickerAttachment == null) {
            Toast.makeText(this, R.string.ism_sticker_share_failed, Toast.LENGTH_SHORT).show();
        } else {
            conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Sticker.getValue(), CustomMessageTypes.Sticker.getValue(), false, true, true, true, new ArrayList<>(Collections.singletonList(stickerAttachment)), null, null, MessageTypesForUI.StickerSent, null, false, null, null);
        }
    }

    @Override
    public void whiteboardShareRequested(String whiteboardImageUrl) {

        conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Whiteboard.getValue(), CustomMessageTypes.Whiteboard.getValue(), false, true, true, true, null, null, null, MessageTypesForUI.WhiteboardSent, new ArrayList<>(Collections.singletonList(whiteboardImageUrl)), true, PresignedUrlMediaTypes.Photo, AttachmentMessageType.Image);
    }

    public void downloadMedia(MessagesModel messagesModel, String mediaType, int messagePosition) {
        if (Utilities.checkSelfExternalStoragePermissionIsGranted(ConversationMessagesActivity.this, false)) {

            int position = conversationMessagesPresenter.verifyMessagePositionInList(messagesModel.getMessageId(), messagePosition, messages);
            if (position != -1) {
                conversationMessagesAdapter.updateMessageStatusOnDownloadingStateChanged(position, true);
            }
            conversationMessagesPresenter.downloadMedia(messagesModel, messagePosition);
        } else {
            if (Utilities.shouldShowExternalPermissionStorageRational(ConversationMessagesActivity.this, false)) {
                Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), getString(R.string.ism_request_storage_permission_to_download, mediaType), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ism_ok), view -> Utilities.requestExternalStoragePermission(ConversationMessagesActivity.this, DOWNLOAD_MEDIA_PERMISSIONS_REQUEST_CODE, false));

                snackbar.show();
            } else {
                Utilities.requestExternalStoragePermission(ConversationMessagesActivity.this, DOWNLOAD_MEDIA_PERMISSIONS_REQUEST_CODE, false);

            }
        }
    }

    /**
     * Cancel media download.
     *
     * @param messagesModel   the messages model
     * @param messagePosition the message position
     */
    public void cancelMediaDownload(MessagesModel messagesModel, int messagePosition) {
        conversationMessagesPresenter.cancelMediaDownload(messagesModel, messagePosition);
    }

    /**
     * Cancel media upload.
     *
     * @param messagesModel   the messages model
     * @param messagePosition the message position
     */
    public void cancelMediaUpload(MessagesModel messagesModel, int messagePosition) {
        conversationMessagesPresenter.cancelMediaUpload(messagesModel, messagePosition);
    }

    /**
     * Remove canceled message.
     *
     * @param localMessageId  the local message id
     * @param messagePosition the message position
     */
    public void removeCanceledMessage(String localMessageId, int messagePosition) {
        int position = conversationMessagesPresenter.verifyUnsentMessagePositionInList(localMessageId, messagePosition, messages);
        if (position != -1) {
            messages.remove(position);
            conversationMessagesAdapter.notifyItemRemoved(position);
            if (messages.size() == 0) {
                if (ismActivityMessagesBinding.tvNoMessages.getVisibility() == View.GONE) {
                    ismActivityMessagesBinding.tvNoMessages.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void addReactionForMessage(String messageId) {
        if (clickActionsNotBlocked()) {
            if (!isFinishing() && !addReactionFragment.isAdded()) {
                dismissAllDialogs();
                addReactionFragment.updateParameters(conversationId, messageId, this);
                addReactionFragment.show(getSupportFragmentManager(), AddReactionFragment.TAG);
            }
        }
    }

    @Override
    public void editMessageRequested(MessagesModel messagesModel) {

        if (!isFinishing() && !editMessageFragment.isAdded()) {
            dismissAllDialogs();
            editMessageFragment.updateParameters(messagesModel, this);
            editMessageFragment.show(getSupportFragmentManager(), EditMessageFragment.TAG);
        }
    }

    private void requestLocationShare() {

        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            if (LocationUtils.isLocationEnabled(this)) {
                shareLocationActivityLauncher.launch(new Intent(ConversationMessagesActivity.this, ShareLocationActivity.class));
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(getString(R.string.ism_location_not_enabled));
                dialog.setPositiveButton(getString(R.string.ism_location_enable), (paramDialogInterface, paramInt) -> {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                });
                dialog.show();
            }
        } else {
            Toast.makeText(this, getString(R.string.ism_location_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkImageCapturePermissions() {

        if ((ContextCompat.checkSelfPermission(ConversationMessagesActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) || !Utilities.checkSelfExternalStoragePermissionIsGranted(ConversationMessagesActivity.this, true) || (ContextCompat.checkSelfPermission(ConversationMessagesActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(ConversationMessagesActivity.this, Manifest.permission.CAMERA)) || Utilities.shouldShowExternalPermissionStorageRational(ConversationMessagesActivity.this, true) || (ActivityCompat.shouldShowRequestPermissionRationale(ConversationMessagesActivity.this, Manifest.permission.RECORD_AUDIO))) {
                Snackbar snackbar = Snackbar.make(ismActivityMessagesBinding.getRoot(), R.string.ism_permission_image_capture, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ism_ok), view -> requestCameraPermissions());

                snackbar.show();

                ((TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                requestCameraPermissions();
            }
        } else {
            requestImageCapture();
        }
    }

    private void requestCameraPermissions() {

        ArrayList<String> permissionsRequired = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(ConversationMessagesActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            permissionsRequired.add(Manifest.permission.CAMERA);
        }
        if (ActivityCompat.checkSelfPermission(ConversationMessagesActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            permissionsRequired.add(Manifest.permission.RECORD_AUDIO);
        }

        if (!Utilities.checkSelfExternalStoragePermissionIsGranted(ConversationMessagesActivity.this, true)) {
            permissionsRequired.addAll(Utilities.getPermissionsListForExternalStorage(true));
        }

        ActivityCompat.requestPermissions(ConversationMessagesActivity.this, permissionsRequired.toArray(new String[permissionsRequired.size()]), CAPTURE_IMAGE_PERMISSIONS_REQUEST_CODE);

    }

    private void checkAccessStoragePermissions(int permissionsRequestCode, String permissionRationale) {
        if (Utilities.checkSelfExternalStoragePermissionIsGranted(this, true)) {
            // Permission already granted
            requestMediaShareFromStorage(permissionsRequestCode);
        } else {
            //not granted
            if (Utilities.shouldShowExternalPermissionStorageRational(this, true)) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), permissionRationale, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ism_ok), view -> Utilities.requestExternalStoragePermission(this, permissionsRequestCode, true));
                snackbar.show();
                ((TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
            }else{
                Utilities.requestExternalStoragePermission(this, permissionsRequestCode, true);
            }
        }
    }

    private void requestImageCapture() {
        KeyboardUtil.hideKeyboard(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cameraActivityLauncher.launch(new Intent(this, CameraActivity.class));
        } else {
            Toast.makeText(ConversationMessagesActivity.this, R.string.ism_image_capture_not_supported, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestContactShare() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        contactPicker.launch(intent);
    }

    private void requestMediaShareFromStorage(int permissionsRequestCode) {

        switch (permissionsRequestCode) {
            case SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE: {

                multiplePhotosPicker.launch(GalleryIntentsUtil.getPhotosIntent(true));
                break;
            }
            case SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE: {

                multipleVideosPicker.launch(GalleryIntentsUtil.getVideosIntent(true));
                break;
            }
            case SHARE_FILES_PERMISSIONS_REQUEST_CODE: {

                multipleFilesPicker.launch(GalleryIntentsUtil.getFilesIntent(true));
                break;
            }
        }
    }

    @Override
    public void onMessageSentSuccessfully(String localMessageId, String messageId, String mediaUrl, String thumbnailUrl) {

        runOnUiThread(() -> {

            int position = conversationMessagesPresenter.verifyUnsentMessagePositionInList(localMessageId, messages.size() - 1, messages);
            if (position != -1) {

                MessagesModel messagesModel = messages.get(position);
                messagesModel.setMessageSentSuccessfully(true);
                messagesModel.setMessageId(messageId);
                messagesModel.setUploaded(true);
                messagesModel.setUploading(false);
                if (mediaUrl != null) {
                    switch (messagesModel.getCustomMessageType()) {

                        case PhotoSent: {
                            messagesModel.setPhotoMainUrl(mediaUrl);
                            messagesModel.setPhotoThumbnailUrl(thumbnailUrl);
                            break;
                        }
                        case VideoSent: {
                            messagesModel.setVideoMainUrl(mediaUrl);
                            messagesModel.setVideoThumbnailUrl(thumbnailUrl);
                            break;
                        }
                        case WhiteboardSent: {
                            messagesModel.setWhiteboardMainUrl(mediaUrl);
                            messagesModel.setWhiteboardThumbnailUrl(thumbnailUrl);
                            break;
                        }
                        case FileSent: {
                            messagesModel.setFileUrl(mediaUrl);
                            break;
                        }
                        case AudioSent: {
                            messagesModel.setAudioUrl(mediaUrl);
                            break;
                        }
                    }
                }

                messages.set(position, messagesModel);
                conversationMessagesAdapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void onError(String errorMessage) {
        if (ismActivityMessagesBinding.refresh.isRefreshing()) {
            ismActivityMessagesBinding.refresh.setRefreshing(false);
        }
        updateShimmerVisibility(false);
        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void addSentMessageInUILocally(MessagesModel messagesModel, boolean uploadRequired) {
        runOnUiThread(() -> {
            if (ismActivityMessagesBinding.tvNoMessages.getVisibility() == View.VISIBLE) {
                ismActivityMessagesBinding.tvNoMessages.setVisibility(View.GONE);
            }

            messages.add(messagesModel);
            conversationMessagesAdapter.notifyItemInserted(messages.size() - 1);
            scrollToLastMessage();
            if (uploadRequired) {
                conversationMessagesPresenter.saveUploadingMessagePosition(messagesModel.getLocalMessageId(), messages.size() - 1);
            }
        });
    }

    @Override
    public void addMessageInUI(MessagesModel messagesModel) {
        runOnUiThread(() -> {
            if (ismActivityMessagesBinding.tvNoMessages.getVisibility() == View.VISIBLE) {
                ismActivityMessagesBinding.tvNoMessages.setVisibility(View.GONE);
            }

            messages.add(messagesModel);
            conversationMessagesAdapter.notifyItemInserted(messages.size() - 1);
            scrollToLastMessage();
        });
    }

    @Override
    public void addMessagesInUI(ArrayList<MessagesModel> messagesModel, boolean refreshRequest, boolean hideSearchingMessageOverlay, boolean messageFound, boolean onReconnect) {
        runOnUiThread(() -> {

            if (onReconnect) {
                try {
                    if (messagesModel.get(messagesModel.size() - 1).getMessageId().equals(messages.get(messages.size() - 1).getMessageId())) {
                        return;
                    }
                } catch (Exception ignore) {
                }
            }

            if (refreshRequest) {
                messages.clear();
                messages.addAll(0, messagesModel);
                conversationMessagesAdapter.notifyDataSetChanged();
                ismActivityMessagesBinding.refresh.setRefreshing(false);

                if (!scrollToMessageNeeded) {
                    scrollToLastMessage();
                }
            } else {
                messages.addAll(0, messagesModel);
                conversationMessagesAdapter.notifyItemRangeInserted(0, messagesModel.size());
            }

            if (scrollToMessageNeeded) {

                if (hideSearchingMessageOverlay) {
                    scrollToMessageNeeded = false;

                    if (messageFound) {
                        String messageIdToScrollTo = getIntent().getExtras().getString("messageId");
                        for (int i = 0; i < messages.size(); i++) {
                            if (messages.get(i).getMessageId() != null && messages.get(i).getMessageId().equals(messageIdToScrollTo)) {
                                try {

                                    int finalI = i;
                                    handler.postDelayed(() -> {
                                        messagesLayoutManager.scrollToPositionWithOffset(finalI, 0);
                                        ismActivityMessagesBinding.ivSearch.setVisibility(View.VISIBLE);
                                        ismActivityMessagesBinding.vLoadingOverlay.getRoot().setVisibility(View.GONE);
                                        updateShimmerVisibility(false);
                                    }, 100);
                                } catch (IndexOutOfBoundsException | NullPointerException ignore) {
                                    ismActivityMessagesBinding.ivSearch.setVisibility(View.VISIBLE);
                                    ismActivityMessagesBinding.vLoadingOverlay.getRoot().setVisibility(View.GONE);
                                    updateShimmerVisibility(false);
                                }
                                break;
                            }
                        }
                    } else {
                        ismActivityMessagesBinding.ivSearch.setVisibility(View.VISIBLE);
                        ismActivityMessagesBinding.vLoadingOverlay.getRoot().setVisibility(View.GONE);
                        scrollToLastMessage();
                        Toast.makeText(this, getString(R.string.ism_message_not_found), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if (messages.size() == 0) {
                if (ismActivityMessagesBinding.tvNoMessages.getVisibility() == View.GONE) {
                    ismActivityMessagesBinding.tvNoMessages.setVisibility(View.VISIBLE);
                }
            } else {
                if (ismActivityMessagesBinding.tvNoMessages.getVisibility() == View.VISIBLE) {
                    ismActivityMessagesBinding.tvNoMessages.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Scrolls message list to last message on receipt of new message
     */
    private void scrollToLastMessage() {
        runOnUiThread(() -> {
            try {

                handler.postDelayed(() -> {
                    messagesLayoutManager.scrollToPositionWithOffset(conversationMessagesAdapter.getItemCount() - 1, 0);
                    updateShimmerVisibility(false);
                }, 100);
            } catch (IndexOutOfBoundsException | NullPointerException ignore) {
                updateShimmerVisibility(false);
            }
        });
    }

    private final RecyclerView.OnScrollListener messagesRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!joiningAsObserver) {
                if (dy != 0 && messagesLayoutManager.findFirstVisibleItemPosition() == 0) {
                    conversationMessagesPresenter.fetchMessagesOnScroll();
                }
            }
        }
    };

    private void fetchMessages(boolean isSearchRequest, String searchTag, boolean showProgressDialog) {
        //Have to disable refresh if downloading/uploading media

        if (ismActivityMessagesBinding.vSelectMultipleMessagesHeader.getRoot().getVisibility() == View.VISIBLE) {
            ismActivityMessagesBinding.vSelectMultipleMessagesHeader.ibClose.performClick();
        }
        //if(showProgressDialog) {
        //showProgressDialog(getString(R.string.ism_fetching_conversations));
        //}
        conversationMessagesPresenter.fetchMessages(0, true, isSearchRequest, searchTag, false);
    }

    private void dismissAllDialogs() {

        if (!isFinishing()) {
            try {
                if (addReactionFragment.getDialog() != null && addReactionFragment.getDialog().isShowing() && !addReactionFragment.isRemoving()) {
                    addReactionFragment.dismiss();
                } else if (fetchReactionUsersFragment.getDialog() != null && fetchReactionUsersFragment.getDialog().isShowing() && !fetchReactionUsersFragment.isRemoving()) {
                    fetchReactionUsersFragment.dismiss();
                } else if (gifsFragment.getDialog() != null && gifsFragment.getDialog().isShowing() && !gifsFragment.isRemoving()) {
                    gifsFragment.dismiss();
                } else if (stickersFragment.getDialog() != null && stickersFragment.getDialog().isShowing() && !stickersFragment.isRemoving()) {
                    stickersFragment.dismiss();
                } else if (shareMediaFragment.getDialog() != null && shareMediaFragment.getDialog().isShowing() && !shareMediaFragment.isRemoving()) {
                    shareMediaFragment.dismiss();
                } else if (whiteboardFragment.getDialog() != null && whiteboardFragment.getDialog().isShowing() && !whiteboardFragment.isRemoving()) {
                    whiteboardFragment.dismiss();
                } else if (sendMessageReplyFragment.getDialog() != null && sendMessageReplyFragment.getDialog().isShowing() && !sendMessageReplyFragment.isRemoving()) {
                    sendMessageReplyFragment.dismiss();
                } else if (messageActionFragment.getDialog() != null && messageActionFragment.getDialog().isShowing() && !messageActionFragment.isRemoving()) {
                    messageActionFragment.dismiss();
                } else if (memberDetailsFragment.getDialog() != null && memberDetailsFragment.getDialog().isShowing() && !memberDetailsFragment.isRemoving()) {
                    memberDetailsFragment.dismiss();
                } else if (editMessageFragment.getDialog() != null && editMessageFragment.getDialog().isShowing() && !editMessageFragment.isRemoving()) {
                    editMessageFragment.dismiss();
                }
                KeyboardUtil.hideKeyboard(this);
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void replyMessageRequested(MessagesModel messagesModel) {
        if (!isFinishing() && !sendMessageReplyFragment.isAdded()) {
            dismissAllDialogs();
            sendMessageReplyFragment.updateParameters(messagesModel, this);
            sendMessageReplyFragment.show(getSupportFragmentManager(), SendMessageReplyFragment.TAG);
        }
    }

    @Override
    public void deleteMessageForSelf(String messageId, boolean multipleMessagesSelected) {

        new androidx.appcompat.app.AlertDialog.Builder(this).setTitle(getString(R.string.ism_delete_for_me_heading)).setMessage(getString(R.string.ism_delete_for_me_alert_message)).setCancelable(true).setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

            dialog.cancel();
            if (multipleMessagesSelected) {
                conversationMessagesPresenter.deleteMessageForSelf(null, true);
            } else {
                conversationMessagesPresenter.deleteMessageForSelf(new ArrayList<>(Collections.singletonList(messageId)), false);
            }
        }).setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel()).create().show();
    }

    @Override
    public void deleteMessageForEveryone(String messageId, boolean multipleMessagesSelected) {

        new androidx.appcompat.app.AlertDialog.Builder(this).setTitle(getString(R.string.ism_delete_for_all_heading)).setMessage(getString(R.string.ism_delete_for_all_alert_message)).setCancelable(true).setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

            dialog.cancel();
            if (multipleMessagesSelected) {
                conversationMessagesPresenter.deleteMessageForEveryone(null, true);
            } else {
                conversationMessagesPresenter.deleteMessageForEveryone(new ArrayList<>(Collections.singletonList(messageId)), false);
            }
        }).setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel()).create().show();
    }

    @Override
    public void selectMultipleMessagesRequested() {

        ismActivityMessagesBinding.vSelectMultipleMessagesHeader.getRoot().setVisibility(View.VISIBLE);
        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.getRoot().setVisibility(View.VISIBLE);

        conversationMessagesAdapter.setMultipleMessagesSelectModeOn(true);
        ismActivityMessagesBinding.vSelectMultipleMessagesHeader.tvSelectedMessagesCount.setText(getString(R.string.ism_number_of_messages_selected, 0));
        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivCopy.setSelected(false);
        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.tvCopy.setSelected(false);
        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivDeleteForMe.setSelected(false);
        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivDeleteForAll.setSelected(false);
        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.tvDeleteForMe.setSelected(false);
        ismActivityMessagesBinding.vSelectMultipleMessagesFooter.tvDeleteForAll.setSelected(false);

        conversationMessagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void fetchMessagesInfoRequest(MessagesModel messagesModel) {
        if (messagesModel.getCustomMessageType() == MessageTypesForUI.TextSent || messagesModel.getCustomMessageType() == MessageTypesForUI.TextReceived) {
            //To handle  java.lang.IllegalArgumentException: class android.widget.ListView declares multiple JSON fields named mPendingCheckForTap for tagged users
            messagesModel.setTextMessage(new SpannableString(messagesModel.getTextMessage().toString()));
        }
        Intent intent = new Intent(this, MessageDeliveryStatusActivity.class);
        intent.putExtra("message", IsometrikUiSdk.getInstance().getIsometrik().getGson().toJson(messagesModel));
        intent.putExtra("conversationId", conversationId);
        intent.putExtra("messageId", messagesModel.getMessageId());
        intent.putExtra("sentAt", messagesModel.getSentAt());
        startActivity(intent);
    }

    @Override
    public void forwardMessageRequest(MessagesModel messagesModel) {

        if (clickActionsNotBlocked()) {
            if (messagesModel.getCustomMessageType() == MessageTypesForUI.TextSent || messagesModel.getCustomMessageType() == MessageTypesForUI.TextReceived) {
                //To handle  java.lang.IllegalArgumentException: class android.widget.ListView declares multiple JSON fields named mPendingCheckForTap for tagged users
                messagesModel.setTextMessage(new SpannableString(messagesModel.getTextMessage().toString()));
            }
            KeyboardUtil.hideKeyboard(this);

            Intent intent = new Intent(this, ForwardMessageActivity.class);
            intent.putExtra("message", IsometrikUiSdk.getInstance().getIsometrik().getGson().toJson(messagesModel));
            startActivity(intent);
        }
    }

    @Override
    public void onMessageSelectionStatus(MultipleMessagesUtil multipleMessagesUtil) {
        ismActivityMessagesBinding.vSelectMultipleMessagesHeader.tvSelectedMessagesCount.setText(getString(R.string.ism_number_of_messages_selected, multipleMessagesUtil.getSelectedMessagesCount()));

        if (multipleMessagesUtil.isCopyEnabled()) {
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivCopy.setSelected(true);
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.tvCopy.setSelected(true);
        } else {
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivCopy.setSelected(false);
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.tvCopy.setSelected(false);
        }

        if (multipleMessagesUtil.isDeleteEnabled()) {
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivDeleteForMe.setSelected(true);
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivDeleteForAll.setSelected(true);
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.tvDeleteForMe.setSelected(true);
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.tvDeleteForAll.setSelected(true);
        } else {
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivDeleteForMe.setSelected(false);
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.ivDeleteForAll.setSelected(false);
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.tvDeleteForMe.setSelected(false);
            ismActivityMessagesBinding.vSelectMultipleMessagesFooter.tvDeleteForAll.setSelected(false);
        }
    }

    @Override
    public void sendReplyMessage(String messageId, String replyMessage, JSONObject replyMessageDetails) {

        conversationMessagesPresenter.shareMessage(RemoteMessageTypes.ReplyMessage, messageId, new OriginalReplyMessageUtil(messageId, replyMessageDetails),

                CustomMessageTypes.Replay.getValue(), replyMessage, false, true, true, true, null, replyMessageDetails, null, MessageTypesForUI.ReplaySent, null, false, null, null);
    }

    @Override
    public void onTextCopyRequest(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.ism_text_copied), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessageDeletedSuccessfully(List<String> messageIds) {
        int size = messageIds.size();
        runOnUiThread(() -> {
            String messageId;
            for (int j = 0; j < size; j++) {

                messageId = messageIds.get(j);
                for (int i = messages.size() - 1; i >= 0; i--) {

                    if (messages.get(i).getMessageId() != null && messages.get(i).getMessageId().equals(messageId)) {

                        messages.remove(i);
                        conversationMessagesAdapter.notifyItemRemoved(i);
                        if (messages.size() == 0) {
                            if (ismActivityMessagesBinding.tvNoMessages.getVisibility() == View.GONE) {
                                ismActivityMessagesBinding.tvNoMessages.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onConversationCleared() {
        runOnUiThread(() -> {
            messages.clear();
            conversationMessagesAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onConversationTitleUpdated(String newTitle) {
        runOnUiThread(() -> {
            ismActivityMessagesBinding.tvConversationOrUserName.setText(newTitle);
            ismActivityMessagesBinding.vSelectMultipleMessagesHeader.tvConversationTitle.setText(newTitle);
        });
    }

    @Override
    public void onRemoteUserTypingEvent(String message) {
        runOnUiThread(() -> {
            ismActivityMessagesBinding.tvTyping.setText(message);
            ismActivityMessagesBinding.tvTyping.setVisibility(View.VISIBLE);
            try {
                handler.postDelayed(typingMessageRunnable, Constants.TYPING_MESSAGE_VISIBILITY_DURATION_IN_MS);
            } catch (Exception ignore) {

            }
        });
    }

    private final Runnable typingMessageRunnable = new Runnable() {
        public void run() {
            try {
                ismActivityMessagesBinding.tvTyping.setVisibility(View.GONE);
            } catch (Exception ignore) {

            }
        }
    };

    @Override
    public void markMessageAsDeliveredToAll(String messageId) {
        runOnUiThread(() -> {
            for (int i = messages.size() - 1; i >= 0; i--) {

                if (messages.get(i).getMessageId() != null && messages.get(i).getMessageId().equals(messageId)) {

                    MessagesModel messagesModel = messages.get(i);
                    messagesModel.setDeliveredToAll(true);
                    messages.set(i, messagesModel);
                    conversationMessagesAdapter.notifyItemChanged(i);
                    break;
                }
            }
        });
    }

    @Override
    public void markMessageAsReadByAll(String messageId) {
        runOnUiThread(() -> {

            for (int i = messages.size() - 1; i >= 0; i--) {

                if (messages.get(i).getMessageId() != null && messages.get(i).getMessageId().equals(messageId)) {

                    MessagesModel messagesModel = messages.get(i);
                    messagesModel.setDeliveredToAll(true);
                    messagesModel.setReadByAll(true);

                    messages.set(i, messagesModel);
                    conversationMessagesAdapter.notifyItemChanged(i);
                    break;
                }
            }
        });
    }

    @Override
    public void onMultipleMessagesMarkedAsReadEvent() {
        conversationMessagesPresenter.fetchMessageDeliveryReadStatusOnMultipleMessagesMarkedAsReadEvent(messages);
    }

    @Override
    public void updateMessageReaction(String messageId, ReactionModel reactionModel, boolean reactionAdded) {
        runOnUiThread(() -> {
            for (int i = messages.size() - 1; i >= 0; i--) {

                if (messages.get(i).getMessageId() != null && messages.get(i).getMessageId().equals(messageId)) {

                    MessagesModel messagesModel = messages.get(i);

                    if (messagesModel.hasReactions()) {
                        ArrayList<ReactionModel> reactions = messagesModel.getReactions();

                        boolean reactionAlreadyExists = false;
                        for (int j = reactions.size() - 1; j >= 0; j--) {
                            if (reactions.get(j).getReactionType().equals(reactionModel.getReactionType())) {

                                if (reactionAdded) {
                                    reactions.set(j, reactionModel);
                                } else {
                                    if (reactionModel.getReactionCount() == 0) {
                                        reactions.remove(j);
                                    } else {
                                        reactions.set(j, reactionModel);
                                    }
                                }
                                reactionAlreadyExists = true;
                                break;
                            }
                        }
                        if (!reactionAlreadyExists && (reactionAdded || reactionModel.getReactionCount() > 0)) {
                            reactions.add(0, reactionModel);
                        }
                        messagesModel.setReactions(reactions);
                    } else {

                        if (reactionAdded || reactionModel.getReactionCount() > 0) {
                            messagesModel.setReactions(new ArrayList<>(Collections.singletonList(reactionModel)));
                        }
                    }

                    messages.set(i, messagesModel);
                    conversationMessagesAdapter.notifyItemChanged(i);
                    break;
                }
            }
        });
    }

    @Override
    public void closeConversation() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (clickActionsNotBlocked()) {
            unregisterListeners();
            KeyboardUtil.hideKeyboard(this);
            try {
                super.onBackPressed();
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterListeners();
        super.onDestroy();
    }

    /**
     * Cleanup all realtime isometrik event listeners that were added at time of exit
     */
    private void unregisterListeners() {

        if (!unregisteredListeners) {

            if (joiningAsObserver) {

                conversationMessagesPresenter.leaveAsObserver();
            }
            handler.removeCallbacksAndMessages(null);
            dismissAllDialogs();
            unregisteredListeners = true;
            conversationMessagesPresenter.unregisterConnectionEventListener();
            conversationMessagesPresenter.unregisterConversationEventListener();
            conversationMessagesPresenter.unregisterMessageEventListener();
            conversationMessagesPresenter.unregisterMembershipControlEventListener();
            conversationMessagesPresenter.unregisterReactionEventListener();
            conversationMessagesPresenter.unregisterUserEventListener();
        }
    }

    @Override
    public void onAudioRecordedSuccessfully(String audioFilePath) {

        new androidx.appcompat.app.AlertDialog.Builder(this).setTitle(getString(R.string.ism_audio_recordings_alert_heading)).setMessage(getString(R.string.ism_audio_recordings_alert_message)).setCancelable(true).setPositiveButton(getString(R.string.ism_send), (dialog, id) -> {

            dialog.cancel();
            conversationMessagesPresenter.shareMessage(RemoteMessageTypes.NormalMessage, null, null, CustomMessageTypes.Audio.getValue(), CustomMessageTypes.Audio.getValue(), false, true, true, true, null, null, null, MessageTypesForUI.AudioSent, new ArrayList<>(Collections.singletonList(audioFilePath)), true, PresignedUrlMediaTypes.Audio, AttachmentMessageType.Audio);
        }).setNegativeButton(getString(R.string.ism_discard), (dialog, id) -> {
            dialog.cancel();
            AudioFileUtil.deleteRecordingFile(audioFilePath);
        }).setCancelable(false).create().show();
    }

    @Override
    public void onMessageUpdatedSuccessfully(String messageId, String updatedMessage) {
        runOnUiThread(() -> {
            for (int i = messages.size() - 1; i >= 0; i--) {
                if (messages.get(i).getMessageId() != null && messages.get(i).getMessageId().equals(messageId)) {
                    MessagesModel messagesModel = messages.get(i);
                    messagesModel.setEditedMessage(true);
                    messagesModel.setTextMessage(new SpannableString(updatedMessage));
                    messages.set(i, messagesModel);
                    conversationMessagesAdapter.notifyItemChanged(i);
                    break;
                }
            }
        });
    }

    @Override
    public void updateMessage(String messageId, String updatedMessage, String originalMessage) {

        conversationMessagesPresenter.updateMessage(messageId, updatedMessage, originalMessage);
    }

    @Override
    public void updateParticipantsCount(int participantsCount) {
        runOnUiThread(() -> ismActivityMessagesBinding.tvParticipantsCountOrOnlineStatus.setText(getString(R.string.ism_participants_count, participantsCount)));
    }

    @Override
    public void onSearchedUsersFetched(ArrayList<TagUserModel> usersModels) {

        if (usersModels.size() > 0) {
            tagUserModels.clear();
            tagUserModels.addAll(usersModels);
            tagUserAdapter.notifyDataSetChanged();

            ismActivityMessagesBinding.vTagUsers.getRoot().setVisibility(View.VISIBLE);
        } else {
            ismActivityMessagesBinding.vTagUsers.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void onTaggedUserClicked(String memberId) {

        if (clickActionsNotBlocked() && !messagingDisabled) {
            if (!isFinishing() && !memberDetailsFragment.isAdded()) {
                dismissAllDialogs();
                memberDetailsFragment.updateParameters(conversationId, memberId);
                memberDetailsFragment.show(getSupportFragmentManager(), MemberDetailsFragment.TAG);
            }
        }
    }

    @Override
    public void onConversationDeletedSuccessfully() {
        onBackPressed();
    }

    private boolean clickActionsNotBlocked() {
        return !conversationMessagesPresenter.isRecordingAudio();
    }

    /**
     * Handle click on message cell.
     *
     * @param messagesModel the messages model
     * @param localMedia    the local media
     */
    public void handleClickOnMessageCell(MessagesModel messagesModel, boolean localMedia) {
        if (ismActivityMessagesBinding.vSelectMultipleMessagesHeader.getRoot().getVisibility() == View.GONE && clickActionsNotBlocked()) {
            if (!messagesModel.isUploading() && !messagesModel.isDownloading()) {
                PreviewMessageUtil.previewMessage(ConversationMessagesActivity.this, messagesModel, localMedia);
            }
        }
    }

    @Override
    public void updateConversationDetailsInHeader(boolean local, boolean isPrivateOneToOne, String userName, boolean isOnline, long lastSeenAt, String conversationTitle, int participantsCount) {

        if (isPrivateOneToOne) {

            if (local) {
                if (getIntent().getExtras().containsKey("userName")) {
                    userName = getIntent().getExtras().getString("userName");
                } else {
                    userName = "";
                }
            }

            ismActivityMessagesBinding.vSelectMultipleMessagesHeader.tvConversationTitle.setText(userName);
            ismActivityMessagesBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
            ismActivityMessagesBinding.ivRefreshOnlineStatus.setVisibility(messagingDisabled ? View.GONE : View.VISIBLE);

            if (local) {
                if (getIntent().getExtras().containsKey("isOnline")) {
                    isOnline = getIntent().getExtras().getBoolean("isOnline");
                } else {
                    isOnline = false;
                }
                if (!isOnline) {
                    if (getIntent().getExtras().containsKey("lastSeenAt")) {
                        lastSeenAt = getIntent().getExtras().getLong("lastSeenAt");
                    } else {
                        lastSeenAt = 0;
                    }
                }
            }
            if (messagingDisabled) {
                ismActivityMessagesBinding.ivOnlineStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ism_ic_blocked));
                ismActivityMessagesBinding.tvParticipantsCountOrOnlineStatus.setText(getString(R.string.ism_unavailable));
                SpannableString spannableString = new SpannableString(userName);
                spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
                ismActivityMessagesBinding.tvConversationOrUserName.setText(spannableString);
            } else {
                ismActivityMessagesBinding.tvConversationOrUserName.setText(userName);

                if (isOnline) {
                    ismActivityMessagesBinding.ivOnlineStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ism_user_online_status_circle));
                    ismActivityMessagesBinding.tvParticipantsCountOrOnlineStatus.setText(getString(R.string.ism_online));
                } else {
                    ismActivityMessagesBinding.ivOnlineStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ism_user_offline_status_circle));

                    if (lastSeenAt == 0) {
                        ismActivityMessagesBinding.tvParticipantsCountOrOnlineStatus.setText(getString(R.string.ism_offline));
                    } else {
                        ismActivityMessagesBinding.tvParticipantsCountOrOnlineStatus.setText(getString(R.string.ism_last_seen_at, TimeUtil.formatTimestampToBothDateAndTime(lastSeenAt)));
                    }
                }
            }
        } else {

            if (local) {
                conversationTitle = getIntent().getExtras().getString("conversationTitle");
            }
            ismActivityMessagesBinding.tvConversationOrUserName.setText(conversationTitle);
            ismActivityMessagesBinding.vSelectMultipleMessagesHeader.tvConversationTitle.setText(conversationTitle);
            ismActivityMessagesBinding.ivOnlineStatus.setVisibility(View.GONE);
            ismActivityMessagesBinding.ivRefreshOnlineStatus.setVisibility(View.GONE);

            if (local) {
                if (getIntent().getExtras().containsKey("participantsCount")) {
                    participantsCount = getIntent().getExtras().getInt("participantsCount");
                } else {
                    participantsCount = 1;
                }
            }
            ismActivityMessagesBinding.tvParticipantsCountOrOnlineStatus.setText(getString(R.string.ism_participants_count, participantsCount));
        }
    }

    @Override
    public void messageToScrollToNotFound() {
        runOnUiThread(() -> {
            scrollToMessageNeeded = false;
            ismActivityMessagesBinding.ivSearch.setVisibility(View.VISIBLE);
            ismActivityMessagesBinding.vLoadingOverlay.getRoot().setVisibility(View.GONE);
            scrollToLastMessage();
            Toast.makeText(this, getString(R.string.ism_message_not_found), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onMessagingStatusChanged(boolean disabled) {
        runOnUiThread(() -> {
            messagingDisabled = disabled;
            ismActivityMessagesBinding.rlBottomLayout.setVisibility(disabled ? View.INVISIBLE : View.VISIBLE);
            ismActivityMessagesBinding.rlRecordAudio.setVisibility(disabled ? View.GONE : View.VISIBLE);
            ismActivityMessagesBinding.rlDeleteConversation.setVisibility(disabled ? View.VISIBLE : View.GONE);

            if (disabled) {

                if (!conversationMessagesAdapter.isMessagingDisabled()) {
                    conversationMessagesAdapter.setMessagingDisabled(true);
                    conversationMessagesAdapter.notifyDataSetChanged();
                }
                ismActivityMessagesBinding.ivOnlineStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ism_ic_blocked));
                ismActivityMessagesBinding.tvParticipantsCountOrOnlineStatus.setText(getString(R.string.ism_unavailable));
                ismActivityMessagesBinding.ivRefreshOnlineStatus.setVisibility(View.GONE);
            } else {
                if (conversationMessagesAdapter.isMessagingDisabled()) {
                    conversationMessagesAdapter.setMessagingDisabled(false);
                    conversationMessagesAdapter.notifyDataSetChanged();
                }
                if (conversationMessagesPresenter.isPrivateOneToOne()) {
                    ismActivityMessagesBinding.ivRefreshOnlineStatus.setVisibility(View.VISIBLE);
                    conversationMessagesPresenter.requestConversationDetails();
                }
            }
        });
    }

    @Override
    public void connectionStateChanged(boolean connected) {
        runOnUiThread(() -> ismActivityMessagesBinding.incConnectionState.tvConnectionState.setVisibility(connected ? View.GONE : View.VISIBLE));
    }

    private void updateShimmerVisibility(boolean visible) {
        if (visible) {
            ismActivityMessagesBinding.incShimmer.rlConversationDetailsOne.getRoot().setVisibility(View.GONE);
            ismActivityMessagesBinding.incShimmer.rlConversationDetailsTwo.getRoot().setVisibility(View.GONE);
            ismActivityMessagesBinding.incShimmer.rlConversationDetailsThree.getRoot().setVisibility(View.GONE);
            ismActivityMessagesBinding.incShimmer.rlConversationDetailsFour.getRoot().setVisibility(View.GONE);
            ismActivityMessagesBinding.incShimmer.rlConversationDetailsFive.getRoot().setVisibility(View.GONE);

            ismActivityMessagesBinding.shimmerFrameLayout.startShimmer();
            ismActivityMessagesBinding.rlShimmer.setVisibility(View.VISIBLE);
        } else {
            if (ismActivityMessagesBinding.rlShimmer.getVisibility() == View.VISIBLE) {
                ismActivityMessagesBinding.rlShimmer.setVisibility(View.GONE);
                ismActivityMessagesBinding.shimmerFrameLayout.stopShimmer();
            }
        }
    }

    @Override
    public void showMessageNotification(String conversationId, String conversationTitle, String message, boolean privateOneToOne, Integer messagePlaceHolderImage, boolean isReactionMessage, String conversationImageUrl, String senderImageUrl, String senderName) {
        runOnUiThread(() -> {
            ismActivityMessagesBinding.incMessageNotification.tvConversationTitle.setText(conversationTitle);
            ismActivityMessagesBinding.incMessageNotification.tvConversationType.setText(privateOneToOne ? getString(R.string.ism_1_1) : getString(R.string.ism_group));
            ismActivityMessagesBinding.incMessageNotification.tvMessage.setText(message);
            if (PlaceholderUtils.isValidImageUrl(conversationImageUrl)) {

                try {
                    Glide.with(ConversationMessagesActivity.this).load(conversationImageUrl).placeholder(R.drawable.ism_ic_profile).transform(new CircleCrop()).into(ismActivityMessagesBinding.incMessageNotification.ivConversationImage);
                } catch (IllegalArgumentException | NullPointerException ignore) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(ConversationMessagesActivity.this, conversationTitle, ismActivityMessagesBinding.incMessageNotification.ivConversationImage, 16);
            }

            if (senderImageUrl != null) {
                if (PlaceholderUtils.isValidImageUrl(senderImageUrl)) {

                    try {
                        Glide.with(ConversationMessagesActivity.this).load(senderImageUrl).placeholder(R.drawable.ism_ic_profile).transform(new CircleCrop()).into(ismActivityMessagesBinding.incMessageNotification.ivSenderImage);
                    } catch (IllegalArgumentException | NullPointerException ignore) {
                    }
                } else {

                    PlaceholderUtils.setTextRoundDrawable(ConversationMessagesActivity.this, senderName, ismActivityMessagesBinding.incMessageNotification.ivSenderImage, 5);
                }
                ismActivityMessagesBinding.incMessageNotification.ivSenderImage.setVisibility(View.VISIBLE);
            } else {
                ismActivityMessagesBinding.incMessageNotification.ivSenderImage.setVisibility(View.GONE);
            }

            if (messagePlaceHolderImage != null) {
                try {
                    Glide.with(ConversationMessagesActivity.this).load(messagePlaceHolderImage).diskCacheStrategy(DiskCacheStrategy.NONE).into(ismActivityMessagesBinding.incMessageNotification.ivMessageType);
                } catch (IllegalArgumentException | NullPointerException ignore) {
                }

                if (isReactionMessage) {
                    ismActivityMessagesBinding.incMessageNotification.ivMessageType.clearColorFilter();
                } else {

                    ismActivityMessagesBinding.incMessageNotification.ivMessageType.setColorFilter(ContextCompat.getColor(ConversationMessagesActivity.this, R.color.ism_last_message_grey), PorterDuff.Mode.SRC_IN);
                }

                ismActivityMessagesBinding.incMessageNotification.ivMessageType.setVisibility(View.VISIBLE);
            } else {
                ismActivityMessagesBinding.incMessageNotification.ivMessageType.setVisibility(View.GONE);
            }

            ismActivityMessagesBinding.incMessageNotification.getRoot().setOnClickListener(v -> {

                Intent intent = new Intent(ConversationMessagesActivity.this, ConversationMessagesActivity.class);
                intent.putExtra("conversationId", conversationId);
                intent.putExtra("privateOneToOne", privateOneToOne);
                startActivity(intent);
            });
            ismActivityMessagesBinding.incMessageNotification.getRoot().setVisibility(View.VISIBLE);
            try {
                handler.postDelayed(messageNotificationRunnable, Constants.MESSAGE_NOTIFICATION_VISIBILITY_DURATION_IN_MS);
            } catch (Exception ignore) {

            }
        });
    }

    private final Runnable messageNotificationRunnable = new Runnable() {
        public void run() {
            try {
                ismActivityMessagesBinding.incMessageNotification.getRoot().setVisibility(View.GONE);
            } catch (Exception ignore) {

            }
        }
    };

    @Override
    public void updateVisibilityOfObserversIcon() {
        runOnUiThread(() -> ismActivityMessagesBinding.ivObservers.setVisibility(View.VISIBLE));
    }

    @Override
    public void onJoinedAsObserverSuccessfully() {
        runOnUiThread(() -> {
            ismActivityMessagesBinding.ivObservers.setVisibility(View.VISIBLE);
            updateShimmerVisibility(false);
        });
    }

    @Override
    public void onFailedToJoinAsObserverOrFetchMessagesOrConversationDetails(String errorMessage) {
        onError(errorMessage);
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstResume) {
            firstResume = false;
        } else {
            conversationMessagesPresenter.setActiveInConversation(true);
            if (!joiningAsObserver) {
                conversationMessagesPresenter.markMessagesAsRead();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        conversationMessagesPresenter.setActiveInConversation(false);
    }
}
