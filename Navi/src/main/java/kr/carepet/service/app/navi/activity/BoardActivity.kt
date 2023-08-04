package kr.carepet.service.app.navi.activity

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.adapter.CarouselRVAdapter
import kr.carepet.service.app.navi.databinding.ActivityBoardBinding
import kr.carepet.service.app.navi.fragment.CommentFragment
import kr.carepet.service.app.navi.model.CommentData


class BoardActivity : AppCompatActivity() {

    val binding by lazy { ActivityBoardBinding.inflate(layoutInflater) }
    var isChecked = false
    var commentDataList:MutableList<CommentData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.boardExpandableContainer.text = getString(R.string.expandable_text)

        // Activity가 실행되면 서버로부터 정보부터 가져오기
        getComment()

        getData()

        val demoData = arrayListOf<Int>(
            R.drawable.dog1,
            R.drawable.dog2,
            R.drawable.dog3,
            R.drawable.dog4,
            R.drawable.dog5,
        )

        binding.boardViewPager.adapter = CarouselRVAdapter(demoData)
        boardViewPagerSet()
        pageTransformer()

        binding.boardIvFavorite.setOnClickListener { clickFavorite() }

        binding.boardIvComment.setOnClickListener { clickComment() }
    }

    fun getData():MutableList<CommentData> {
        return commentDataList
    }

    private fun getComment() {
        // 서버에서 댓글관련 정보 불러오기
        // 없으니 더미 데이터로 처리

        val itemlist = arrayListOf<CommentData>(
            CommentData("Dal1", R.drawable.dog1, "댓글 1"),
            CommentData("Dal2", R.drawable.dog2, "댓글 2"),
            CommentData("Dal3", R.drawable.dog3, "댓글 3"),
            CommentData("Dal4", R.drawable.dog4, "댓글 4"),
            CommentData("Dal5", R.drawable.dog5, "댓글 5"),
            CommentData("Dal6", R.drawable.dog1, "댓글 6")
        )
        commentDataList.addAll(itemlist)
    }

    private fun clickComment() {
        val bottomSheetFragment = CommentFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun clickFavorite() {

        if(isChecked){
            //체크가 되어있는 상태
            isChecked =false
            binding.boardIvFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
            sendFavToServer(1)
        }else{
            isChecked =true
            binding.boardIvFavorite.setImageResource(R.drawable.baseline_favorite_24)
            sendFavToServer(2)
        }
    }

    private fun sendFavToServer(i: Int) {
        // 서버에 전송 할일 있으면 여기에 구현
    }

    private fun boardViewPagerSet() {
        // ViewPager 세팅
        binding.boardViewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun pageTransformer(){
        // ViewPager 애니메이션 효과
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }

        binding.boardViewPager.setPageTransformer(compositePageTransformer)
    }
}