package io.isometrik.ui.messages.reaction

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.isometrik.chat.R
import io.isometrik.chat.databinding.DialogFullScreenReactionBinding
import io.isometrik.ui.messages.reaction.add.AddReactionAdapter
import io.isometrik.ui.messages.reaction.add.ReactionModel
import io.isometrik.ui.messages.reaction.util.ReactionRepository
import java.util.ArrayList

class FullScreenReactionDialog(private val customView: View) : DialogFragment() {
    companion object {
        val TAG = "FullScreenReactionDialog"
    }

    private lateinit var binding: DialogFullScreenReactionBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        dialog.setCancelable(true)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_full_screen_reaction, container, false)
        binding = DialogFullScreenReactionBinding.inflate(inflater, container, false)
        (customView.parent as? ViewGroup)?.removeView(customView)

        binding.containerView.addView(customView)

        binding.rvReactions.setLayoutManager(
            LinearLayoutManager(
                activity,
                RecyclerView.HORIZONTAL,
                false
            )
        )

        val reactions = ArrayList<ReactionModel>()
        reactions.addAll(ReactionRepository.getReactions())
        val addReactionAdapter = AddReactionAdapter(context, reactions)
        binding.rvReactions.setAdapter(addReactionAdapter)

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

}
