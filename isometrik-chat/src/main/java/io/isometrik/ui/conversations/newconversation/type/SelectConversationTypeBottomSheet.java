package io.isometrik.ui.conversations.newconversation.type;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.isometrik.chat.databinding.IsmActivitySelectConversationTypeBinding;
import io.isometrik.ui.IsometrikChatSdk;

public class SelectConversationTypeBottomSheet extends BottomSheetDialogFragment {

    private IsmActivitySelectConversationTypeBinding ismActivitySelectConversationTypeBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ismActivitySelectConversationTypeBinding = IsmActivitySelectConversationTypeBinding.inflate(inflater, container, false);
        return ismActivitySelectConversationTypeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ismActivitySelectConversationTypeBinding.rlPrivateOneToOne.setOnClickListener(v -> {
            IsometrikChatSdk.getInstance().getChatActionsClickListener().onCreateChatIconClicked(false);
            dismiss();
        });

        ismActivitySelectConversationTypeBinding.rlPrivateGroup.setOnClickListener(v -> {
            IsometrikChatSdk.getInstance().getChatActionsClickListener().onCreateChatIconClicked(true);
            dismiss();
        });

        ismActivitySelectConversationTypeBinding.rlPublicGroup.setOnClickListener(v -> {

            dismiss();
        });

        ismActivitySelectConversationTypeBinding.rlOpen.setOnClickListener(v -> {

            dismiss();
        });

        ismActivitySelectConversationTypeBinding.ibBack.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ismActivitySelectConversationTypeBinding = null;
    }
}
