package kr.carepet.service.app.navi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.model.CommentData

class CommentRVAdapter(val commentDataList:MutableList<CommentData>): RecyclerView.Adapter<CommentRVAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)

        return VH(view)
    }

    override fun getItemCount(): Int = commentDataList.size


    override fun onBindViewHolder(holder: VH, position: Int) {
        val item=commentDataList[position]
        holder.bind(item)
    }

    class VH(view:View) :RecyclerView.ViewHolder(view) {
        val ivProfile: ImageView = view.findViewById(R.id.comment_iv_profile)
        val tvUserId: TextView = view.findViewById(R.id.comment_tv_userid)
        val tvComment: TextView = view.findViewById(R.id.comment_tv_main)

        fun bind(item:CommentData){
            ivProfile.setImageResource(item.userProfile)
            tvUserId.text = item.userId
            tvComment.text = item.comment
        }
    }
}