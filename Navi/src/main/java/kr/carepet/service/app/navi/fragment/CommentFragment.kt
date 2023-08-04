/*
 *  * Copyright(c) 2023 CarePat All right reserved.
 *  * This software is the proprietary information of CarePet.
 *  *
 *  * Revision History
 *  * Author                         Date             Description
 *  * --------------------------    -------------    ----------------------------------------
 *  * sanghun.lee@care-biz.co.kr     2023. 7. 28.    CarePet Development...
 *  *
 */

package kr.carepet.service.app.navi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.activity.BoardActivity
import kr.carepet.service.app.navi.adapter.CommentRVAdapter
import kr.carepet.service.app.navi.model.CommentData

class CommentFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_comment, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.comment_recycler_view)
        val dataList: MutableList<CommentData>? = (requireActivity() as? BoardActivity)?.getData()

        val etComment = view.findViewById<EditText>(R.id.comment_et_post)
        val ivPost = view.findViewById<ImageView>(R.id.comment_iv_post)

        inflateRV(recyclerView, dataList)

        ivPost.setOnClickListener { postingComment(etComment) }

        return view
    }

    private fun inflateRV(recyclerView: RecyclerView, dataList: MutableList<CommentData>?) {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter= dataList?.let { CommentRVAdapter(it) }
        dataList?.size?.let { recyclerView.scrollToPosition(it-1) }
    }

    private fun postingComment(etComment: EditText) {
        val commentText = etComment.text.toString()

    }

}