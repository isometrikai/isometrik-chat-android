package io.isometrik.ui.messages.media.visual

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmBottomsheetVisualMediaBinding
import io.isometrik.chat.utils.RecyclerItemClickListener
import io.isometrik.ui.messages.media.MediaSelectedToBeShared
import io.isometrik.ui.messages.media.gifs.GifCategoriesRepository
import io.isometrik.ui.messages.media.gifs.GifsAdapter
import io.isometrik.ui.messages.media.gifs.GifsCategoryModel
import io.isometrik.ui.messages.media.gifs.GifsModel
import io.isometrik.ui.messages.media.gifstickerutils.Constants
import io.isometrik.ui.messages.media.stickers.StickerCategoriesRepository
import io.isometrik.ui.messages.media.stickers.StickersAdapter
import io.isometrik.ui.messages.media.stickers.StickersCategoryModel
import io.isometrik.ui.messages.media.stickers.StickersModel

/**
 * The fragment to fetch list of stickers in a category, with paging and
 * search.
 */
class VisualMediaFragment
/**
 * Instantiates a stickers fragment.
 */
    : BottomSheetDialogFragment(), VisualContract.View {
    private lateinit var ismBottomsheetVisualBinding: IsmBottomsheetVisualMediaBinding
    private lateinit var visualPresenter: VisualContract.Presenter
    private var activity: Activity? = null
    private var stickersCategories: ArrayList<StickersCategoryModel>? = null
    private var stickers: ArrayList<StickersModel>? = null
    private var stickersAdapter: StickersAdapter? = null
    private var currentlySelectedStickerCategoryId: String? =
        StickerCategoriesRepository.StickerCategoryNameEnum.Featured.value
    private var currentlySelectedGifCategoryId: String =
        GifCategoriesRepository.GifCategoryNameEnum.Featured.value
    private var gridLayoutManagerSticker: GridLayoutManager? = null
    private var mediaSelectedToBeShared: MediaSelectedToBeShared? = null
    private var stickerSelected = true


    private lateinit var gridLayoutManagerGif: GridLayoutManager
    private val gifsCategories: java.util.ArrayList<GifsCategoryModel>? = null
    private var gifs: java.util.ArrayList<GifsModel>? = null
    private var gifsAdapter: GifsAdapter? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ismBottomsheetVisualBinding =
            IsmBottomsheetVisualMediaBinding.inflate(inflater, container, false)
        updateShimmerVisibility(true)

        // stickers
        stickersCategories = ArrayList()
        stickers = ArrayList()
        stickersAdapter = StickersAdapter(stickers, activity)
        gridLayoutManagerSticker = GridLayoutManager(activity, 3)
        ismBottomsheetVisualBinding!!.rvStickers.layoutManager = gridLayoutManagerSticker
        ismBottomsheetVisualBinding!!.rvStickers.isNestedScrollingEnabled = true
        ismBottomsheetVisualBinding!!.rvStickers.adapter = stickersAdapter

        ismBottomsheetVisualBinding!!.pbLoading.visibility = View.VISIBLE

        ismBottomsheetVisualBinding!!.rvStickers.addOnScrollListener(stickersOnScrollListener)
        ismBottomsheetVisualBinding!!.rvStickers.addOnItemTouchListener(
            RecyclerItemClickListener(activity, ismBottomsheetVisualBinding!!.rvStickers,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (position >= 0) {
                            val stickersModel = stickers!![position]
                            mediaSelectedToBeShared!!.stickerShareRequested(
                                stickersModel.stickerName,
                                stickersModel.stickerImageUrl
                            )
                        }
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                    }
                }

            ))

        ismBottomsheetVisualBinding!!.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (stickerSelected) {
                    if (s.isNotEmpty() && s.toString().trim { it <= ' ' }.isNotEmpty()) {
                        visualPresenter!!.searchStickersInACategory(
                            currentlySelectedStickerCategoryId,
                            s.toString(), Constants.GIFS_STICKERS_PAGE_SIZE, 0
                        )
                    } else {
                        visualPresenter!!.fetchStickersInACategory(
                            currentlySelectedStickerCategoryId,
                            Constants.GIFS_STICKERS_PAGE_SIZE, 0
                        )
                    }
                } else {
                    if (s.isNotEmpty() && s.toString().trim { it <= ' ' }.isNotEmpty()) {
                        visualPresenter.searchGifsInACategory(
                            currentlySelectedGifCategoryId, s.toString(),
                            Constants.GIFS_STICKERS_PAGE_SIZE, 0
                        )
                    } else {
                        visualPresenter.fetchGifsInACategory(
                            currentlySelectedGifCategoryId,
                            Constants.GIFS_STICKERS_PAGE_SIZE, 0
                        )
                    }
                }
            }
        })

//        ismBottomsheetVisualBinding!!.btSearch.setOnClickListener { v: View? ->
//            val text = ismBottomsheetVisualBinding!!.etSearch.text.toString()
//            if (text.isEmpty()) {
//                Toast.makeText(
//                    activity,
//                    R.string.ism_enter_stickers_search_text,
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                visualPresenter!!.searchStickersInACategory(
//                    currentlySelectedStickerCategoryId,
//                    text,
//                    Constants.GIFS_STICKERS_PAGE_SIZE,
//                    0
//                )
//            }
//        }

        //To allow scroll on sticker's recyclerview
        ismBottomsheetVisualBinding!!.rvStickers.setOnTouchListener { v: View, event: MotionEvent? ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            v.onTouchEvent(event)
            true
        }
        updateSelection()


        // GIF

        gifs = java.util.ArrayList<GifsModel>()
        gifsAdapter = GifsAdapter(gifs, activity)
        gridLayoutManagerGif = GridLayoutManager(activity, 3)
        ismBottomsheetVisualBinding.rvGifs.setLayoutManager(gridLayoutManagerGif)
        ismBottomsheetVisualBinding.rvGifs.isNestedScrollingEnabled = true
        ismBottomsheetVisualBinding.rvGifs.setAdapter(gifsAdapter)

        ismBottomsheetVisualBinding.rvGifs.addOnScrollListener(gifsOnScrollListener)
        ismBottomsheetVisualBinding.rvGifs.addOnItemTouchListener(
            RecyclerItemClickListener(activity, ismBottomsheetVisualBinding.rvGifs,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (position >= 0) {
                            val giftsModel = gifs!![position]

                            mediaSelectedToBeShared!!.gifShareRequested(
                                giftsModel.gifName,
                                giftsModel.gifImageUrl, giftsModel.gifStillUrl
                            )
                        }
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                    }
                }
            ))


        ismBottomsheetVisualBinding!!.textStickers.setOnClickListener { v: View? ->
            stickerSelected = true
            updateSelection()
        }
        ismBottomsheetVisualBinding!!.textGif.setOnClickListener { v: View? ->
            stickerSelected = false
            updateSelection()
        }
        return ismBottomsheetVisualBinding!!.root
    }

    private fun updateSelection() {
        ismBottomsheetVisualBinding.textStickers.isSelected = stickerSelected
        ismBottomsheetVisualBinding!!.textGif.isSelected = !stickerSelected
        ismBottomsheetVisualBinding!!.etSearch.hint =
            if (stickerSelected) getString(R.string.ism_search_stickers) else getString(R.string.ism_search_gifs)
        ismBottomsheetVisualBinding.rvStickers.visibility =
            if (stickerSelected) View.VISIBLE else View.GONE
        ismBottomsheetVisualBinding.rvGifs.visibility =
            if (!stickerSelected) View.VISIBLE else View.GONE
        ismBottomsheetVisualBinding.etSearch.setText("")

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity()
        visualPresenter = VisualPresenter()
        visualPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        visualPresenter!!.detachView()
        activity = null
    }

    override fun onStickersCategoriesFetchedSuccessfully(
        stickersCategoryModels: ArrayList<StickersCategoryModel>
    ) {
        if (stickersCategoryModels.size > 0) {
            stickersCategories!!.addAll(stickersCategoryModels)

            if (stickersCategories!!.size > 0) {
                currentlySelectedStickerCategoryId = stickersCategories!![0].stickerCategoryName
                stickers!!.addAll(stickersCategories!![0].stickers)
                stickersAdapter!!.notifyDataSetChanged()
                updateShimmerVisibility(false)
                if (currentlySelectedStickerCategoryId == StickerCategoriesRepository.StickerCategoryNameEnum.Classic.value) {
                    ismBottomsheetVisualBinding!!.rlSearch.visibility = View.GONE
                }
            }
        } else {
            ismBottomsheetVisualBinding!!.tvNoGifsStickers.text =
                getString(
                    R.string.ism_no_gifs_stickers_category,
                    getString(R.string.ism_no_stickers)
                )
            ismBottomsheetVisualBinding!!.tvNoGifsStickers.visibility = View.VISIBLE
        }
    }

    override fun onStickersFetchedInCategory(
        categoryId: String,
        stickersModels: ArrayList<StickersModel>, notOnScroll: Boolean
    ) {
        if (categoryId == currentlySelectedStickerCategoryId) {
            requireActivity().runOnUiThread {
                if (notOnScroll) {
                    stickers!!.clear()
                }
                stickers!!.addAll(stickersModels)
                if (stickers!!.size == 0) {
                    ismBottomsheetVisualBinding!!.tvNoGifsStickers.text =
                        getString(R.string.ism_no_stickers_in_category)
                    ismBottomsheetVisualBinding!!.tvNoGifsStickers.visibility =
                        View.VISIBLE
                    ismBottomsheetVisualBinding!!.rvStickers.visibility =
                        View.GONE
                } else {
                    ismBottomsheetVisualBinding!!.tvNoGifsStickers.visibility =
                        View.GONE
                    ismBottomsheetVisualBinding!!.rvStickers.visibility =
                        View.VISIBLE
                    stickersAdapter!!.notifyDataSetChanged()
                }
                ismBottomsheetVisualBinding!!.pbLoading.visibility = View.GONE
                updateShimmerVisibility(false)
            }
        }
    }

    override fun onStickersSearchResultsFetchedInCategory(
        categoryId: String,
        stickersModels: ArrayList<StickersModel>, notOnScroll: Boolean
    ) {
        if (categoryId == currentlySelectedStickerCategoryId) {
            if (notOnScroll) {
                stickers!!.clear()
            }
            stickers!!.addAll(stickersModels)

            requireActivity().runOnUiThread {
                if (stickers!!.size == 0) {
                    ismBottomsheetVisualBinding!!.tvNoGifsStickers.text =
                        getString(R.string.ism_no_stickers_in_category)
                    ismBottomsheetVisualBinding!!.tvNoGifsStickers.visibility =
                        View.VISIBLE
                    ismBottomsheetVisualBinding!!.rvStickers.visibility =
                        View.GONE
                } else {
                    ismBottomsheetVisualBinding!!.tvNoGifsStickers.visibility =
                        View.GONE
                    ismBottomsheetVisualBinding!!.rvStickers.visibility =
                        View.VISIBLE
                    stickersAdapter!!.notifyDataSetChanged()
                }
                ismBottomsheetVisualBinding!!.pbLoading.visibility = View.GONE
                updateShimmerVisibility(false)
            }
        }
    }

    override fun onError(errorMessage: String?) {
        if (stickerSelected) {
            requireActivity().runOnUiThread {
                ismBottomsheetVisualBinding!!.pbLoading.visibility = View.GONE
                updateShimmerVisibility(false)
                if (errorMessage != null) {
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getActivity(), getString(R.string.ism_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            requireActivity().runOnUiThread {
                ismBottomsheetVisualBinding.pbLoading.setVisibility(View.GONE)
                updateShimmerVisibility(false)
                if (errorMessage != null) {
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        getActivity(),
                        getString(R.string.ism_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val stickersOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (currentlySelectedStickerCategoryId != null) {
                    visualPresenter!!.fetchStickersOnScroll(
                        currentlySelectedStickerCategoryId,
                        gridLayoutManagerSticker!!.findFirstVisibleItemPosition(),
                        gridLayoutManagerSticker!!.childCount,
                        gridLayoutManagerSticker!!.itemCount
                    )
                }
            }
        }

    private val gifsOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (currentlySelectedGifCategoryId != null) {
                    visualPresenter.fetchGifsOnScroll(
                        currentlySelectedGifCategoryId,
                        gridLayoutManagerGif.findFirstVisibleItemPosition(),
                        gridLayoutManagerGif.getChildCount(),
                        gridLayoutManagerGif.getItemCount()
                    )
                }
            }
        }

    /**
     * Update parameters.
     *
     * @param mediaSelectedToBeShared the media selected to be shared
     */
    fun updateParameters(mediaSelectedToBeShared: MediaSelectedToBeShared?) {
        this.mediaSelectedToBeShared = mediaSelectedToBeShared
    }

    private fun updateShimmerVisibility(visible: Boolean) {
        if (visible) {
            ismBottomsheetVisualBinding!!.shimmerFrameLayout.startShimmer()
            ismBottomsheetVisualBinding!!.shimmerFrameLayout.visibility = View.VISIBLE
        } else {
            if (ismBottomsheetVisualBinding!!.shimmerFrameLayout.visibility == View.VISIBLE) {
                ismBottomsheetVisualBinding!!.shimmerFrameLayout.visibility = View.GONE
                ismBottomsheetVisualBinding!!.shimmerFrameLayout.stopShimmer()
            }
        }
    }

    override fun onGifsCategoriesFetchedSuccessfully(gifsCategoryModels: java.util.ArrayList<GifsCategoryModel>) {

    }

    override fun onGifsFetchedInCategory(
        categoryId: String, gifsModels: java.util.ArrayList<GifsModel>,
        notOnScroll: Boolean
    ) {
        if (categoryId == currentlySelectedGifCategoryId) {
            requireActivity().runOnUiThread {
                if (notOnScroll) {
                    gifs!!.clear()
                }
                gifs!!.addAll(gifsModels)

                if (gifs!!.size == 0) {
                    ismBottomsheetVisualBinding.tvNoGifsStickers.setText(
                        getString(R.string.ism_no_gifs_in_category)
                    )
                    ismBottomsheetVisualBinding.tvNoGifsStickers.setVisibility(View.VISIBLE)
                    ismBottomsheetVisualBinding.rvGifs.setVisibility(View.GONE)
                } else {
                    ismBottomsheetVisualBinding.tvNoGifsStickers.setVisibility(View.GONE)
                    ismBottomsheetVisualBinding.rvGifs.setVisibility(View.VISIBLE)
                    gifsAdapter!!.notifyDataSetChanged()
                }
                ismBottomsheetVisualBinding.pbLoading.setVisibility(View.GONE)
                updateShimmerVisibility(false)
            }
        }
    }

    override fun onGifsSearchResultsFetchedInCategory(
        categoryId: String,
        gifsModels: java.util.ArrayList<GifsModel>, notOnScroll: Boolean
    ) {
        if (categoryId == currentlySelectedGifCategoryId) {
            if (notOnScroll) {
                gifs!!.clear()
            }
            gifs!!.addAll(gifsModels)
            requireActivity().runOnUiThread {
                if (gifs!!.size == 0) {
                    ismBottomsheetVisualBinding.tvNoGifsStickers.setText(
                        getString(R.string.ism_no_gifs_in_category)
                    )
                    ismBottomsheetVisualBinding.tvNoGifsStickers.setVisibility(View.VISIBLE)
                    ismBottomsheetVisualBinding.rvGifs.setVisibility(View.GONE)
                } else {
                    ismBottomsheetVisualBinding.tvNoGifsStickers.setVisibility(View.GONE)
                    ismBottomsheetVisualBinding.rvGifs.setVisibility(View.VISIBLE)
                    gifsAdapter!!.notifyDataSetChanged()
                }
                ismBottomsheetVisualBinding.pbLoading.setVisibility(View.GONE)
                updateShimmerVisibility(false)
            }
        }
    }

    companion object {
        /**
         * The constant TAG.
         */
        const val TAG: String = "VisualFragment"
    }
}
