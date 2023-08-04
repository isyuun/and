package kr.carepet.service.app.navi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.carepet.service.app.navi.R

class CarouselRVAdapter(private val carouselDataList: ArrayList<Int>) :
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel_board, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val item = carouselDataList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }

    class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val imageView:ImageView = view.findViewById(R.id.board_iv_main)

        fun bind(item: Int){
            imageView.setImageResource(item)
        }
    }

}