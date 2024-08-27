package io.isometrik.ui.messages.tag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBottomsheetMemberDetailsBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;

/**
 * The fragment to fetch details of a tagged user on click.
 */
public class MemberDetailsFragment extends BottomSheetDialogFragment
    implements MemberDetailsContract.View {

  /**
   * The constant TAG.
   */
  public static final String TAG = "MemberDetailsFragment";

  private MemberDetailsContract.Presenter memberDetailsPresenter;

  private Activity activity;
  private IsmBottomsheetMemberDetailsBinding ismBottomsheetMemberDetailsBinding;

  private String conversationId, memberId;

  /**
   * Instantiates a new Member details fragment.
   */
  public MemberDetailsFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    ismBottomsheetMemberDetailsBinding =
        IsmBottomsheetMemberDetailsBinding.inflate(inflater, container, false);
    ismBottomsheetMemberDetailsBinding.ivUserImage.setVisibility(View.VISIBLE);
    ismBottomsheetMemberDetailsBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
    ismBottomsheetMemberDetailsBinding.tvFailedToFetchMemberDetails.setVisibility(View.GONE);

    memberDetailsPresenter.fetchMemberDetails(conversationId, memberId);
    return ismBottomsheetMemberDetailsBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    memberDetailsPresenter = new MemberDetailsPresenter();
    memberDetailsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    memberDetailsPresenter.detachView();
    activity = null;
  }

  @Override
  public void onMemberDetailsFetchedSuccessfully(String memberName, String memberIdentifier,
      String memberProfileImageUrl, boolean isOnline) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        ismBottomsheetMemberDetailsBinding.tvUserName.setText(memberName);
        ismBottomsheetMemberDetailsBinding.tvUserIdentifier.setText(memberIdentifier);

        ismBottomsheetMemberDetailsBinding.ivOnlineStatus.setImageDrawable(
            isOnline ? ContextCompat.getDrawable(activity, R.drawable.ism_user_online_status_circle)
                : ContextCompat.getDrawable(activity, R.drawable.ism_user_offline_status_circle));
        if (PlaceholderUtils.isValidImageUrl(memberProfileImageUrl)) {

          try {
            GlideApp.with(activity)
                .load(memberProfileImageUrl)
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(ismBottomsheetMemberDetailsBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(activity, memberName,
              ismBottomsheetMemberDetailsBinding.ivUserImage, 26);
        }
        ismBottomsheetMemberDetailsBinding.ivUserImage.setVisibility(View.VISIBLE);
        ismBottomsheetMemberDetailsBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
        ismBottomsheetMemberDetailsBinding.tvFailedToFetchMemberDetails.setVisibility(View.GONE);

      });
    }
  }

  @Override
  public void onNotAllowedToFetchMemberDetails(String errorMessage) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        ismBottomsheetMemberDetailsBinding.ivUserImage.setVisibility(View.INVISIBLE);
        ismBottomsheetMemberDetailsBinding.ivOnlineStatus.setVisibility(View.INVISIBLE);
        ismBottomsheetMemberDetailsBinding.tvFailedToFetchMemberDetails.setText(errorMessage);
        ismBottomsheetMemberDetailsBinding.tvFailedToFetchMemberDetails.setVisibility(View.VISIBLE);
      });
    }
  }

  @Override
  public void onError(String errorMessage) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        //hideProgressDialog();
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  /**
   * Update parameters.
   *
   * @param conversationId the conversation id
   * @param memberId the member id
   */
  public void updateParameters(String conversationId, String memberId) {
    this.conversationId = conversationId;
    this.memberId = memberId;
  }
}
