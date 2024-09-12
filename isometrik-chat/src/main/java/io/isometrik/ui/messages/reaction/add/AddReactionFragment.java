package io.isometrik.ui.messages.reaction.add;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBottomsheetReactionBinding;
import io.isometrik.ui.messages.action.MessageActionCallback;
import io.isometrik.ui.messages.reaction.util.ReactionRepository;
import io.isometrik.chat.utils.AlertProgress;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * The fragment to select reaction add a reaction of particular type on a message by logged in user.
 */
public class AddReactionFragment extends BottomSheetDialogFragment
    implements AddReactionContract.View {

  /**
   * The constant TAG.
   */
  public static final String TAG = "AddReactionFragment";

  private AddReactionContract.Presenter addReactionPresenter;
  private IsmBottomsheetReactionBinding ismBottomsheetReactionBinding;
  private ArrayList<ReactionModel> reactions;
  private Activity activity;
  private String messageId, conversationId;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private int selectedReactionPosition;
  private MessageActionCallback messageActionCallback;

  /**
   * Instantiates a new add reaction fragment.
   */
  public AddReactionFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    ismBottomsheetReactionBinding =
        IsmBottomsheetReactionBinding.inflate(inflater, container, false);
    alertProgress = new AlertProgress();
    ismBottomsheetReactionBinding.rvReactions.setLayoutManager(
        new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
    reactions = new ArrayList<>();
    reactions.addAll(ReactionRepository.getReactions());
    AddReactionAdapter addReactionAdapter = new AddReactionAdapter(activity, reactions);
    ismBottomsheetReactionBinding.rvReactions.setAdapter(addReactionAdapter);

    ismBottomsheetReactionBinding.tvReactionName.setText(reactions.get(0).getReactionName());
    try {
      Glide.with(activity)
          .load(reactions.get(0).getReactionIcon())
          .into(ismBottomsheetReactionBinding.ivReactionImage);
    } catch (IllegalArgumentException | NullPointerException ignore) {
    }

    ismBottomsheetReactionBinding.rvReactions.addOnItemTouchListener(
        new RecyclerItemClickListener(activity, ismBottomsheetReactionBinding.rvReactions,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {
                  if (position != selectedReactionPosition) {
                    selectedReactionPosition = position;
                    ReactionModel reactionModel = reactions.get(position);
                    ismBottomsheetReactionBinding.tvReactionName.setText(
                        reactionModel.getReactionName());

                    try {
                      Glide.with(activity)
                          .load(reactionModel.getReactionIcon())
                          .into(ismBottomsheetReactionBinding.ivReactionImage);
                    } catch (IllegalArgumentException | NullPointerException e) {
                      e.printStackTrace();
                    }
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismBottomsheetReactionBinding.ivAddReaction.setOnClickListener(v -> {
      showProgressDialog(getString(R.string.ism_adding_reaction,
          reactions.get(selectedReactionPosition).getReactionName()));
      addReactionPresenter.addReaction(conversationId, messageId,
          reactions.get(selectedReactionPosition).getReactionType());
    });

    return ismBottomsheetReactionBinding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ismBottomsheetReactionBinding = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    addReactionPresenter = new AddReactionPresenter();
    addReactionPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    addReactionPresenter.detachView();
    activity = null;
  }

  @Override
  public void onError(String errorMessage) {

    hideProgressDialog();

    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  @Override
  public void onReactionAddedSuccessfully(String messageId, ReactionModel reactionModel) {
    hideProgressDialog();
    messageActionCallback.updateMessageReaction(messageId, reactionModel,true);
    try {
      dismiss();
    } catch (Exception ignore) {
    }
  }

  private void showProgressDialog(String message) {
    if (activity != null) {
      alertDialog = alertProgress.getProgressDialog(activity, message);
      if (!activity.isFinishing()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  /**
   * Update parameters.
   *
   * @param conversationId the conversation id
   * @param messageId the message id
   * @param messageActionCallback the message action callback
   */
  public void updateParameters(String conversationId, String messageId,
      MessageActionCallback messageActionCallback) {
    this.messageId = messageId;
    this.conversationId = conversationId;
    this.messageActionCallback = messageActionCallback;
  }
}
