package com.life.scalebar

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.to.aboomy.statusbar_lib.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StatusBarUtil.transparencyBar(this, false);

        recycler.layoutManager = LinearLayoutManager(this)
        val adapter = Adapter()
        recycler.adapter = adapter

        val data = mutableListOf<String>()
        for (i in 0..80) {
            data.add("$i")
        }
        adapter.data.addAll(data)
        adapter.notifyDataSetChanged()

        recycler.post { translationScaleSearch() }
    }


    private fun translationScaleSearch() {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val searchLayoutParams = search.layoutParams as ViewGroup.MarginLayoutParams
            //向上滑动顶部的距离
            private val TOP_MARGIN = appName.top  - appName.top / 4 + statusBar.height * 1.0f
            //原来的初始位置距离
            private val MAX_MARGIN = search.top
            //搜索框的完整宽度
            private val MAX_WIDTH = search.width
            //搜索框的最小宽度
            private val MIN_WIDTH = search.width - appName.width - SizeUtils.dp2px(16f)
            //累加距离
            private var mDy = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mDy += dy
                var searchLayoutNewTopMargin = MAX_MARGIN - mDy * 0.5f
                if (searchLayoutNewTopMargin < TOP_MARGIN) {
                    searchLayoutNewTopMargin = TOP_MARGIN
                }

                val alpha = (MAX_MARGIN - mDy * 1.0f) / searchLayoutNewTopMargin
                titleView.alpha = alpha
                titleView.visibility = if (alpha > 0) View.VISIBLE else View.GONE
                message.alpha = alpha
                message.visibility = if (alpha > 0) View.VISIBLE else View.GONE

                var searchLayoutNewWidth = MAX_WIDTH - mDy * 0.5f//此处 * 1.3f 可以设置搜索框宽度缩放的速率
                if (searchLayoutNewWidth < MIN_WIDTH) {
                    searchLayoutNewWidth = MIN_WIDTH.toFloat()
                }

                val appNameAlpha = (MAX_WIDTH - mDy * 2.0f) / searchLayoutNewWidth
                appName.alpha = if(1 - appNameAlpha < 0) 1f else 1 - appNameAlpha
                appName.visibility = if (appName.alpha > 0) View.VISIBLE else View.INVISIBLE

                searchLayoutParams.topMargin = searchLayoutNewTopMargin.toInt()
                searchLayoutParams.width = searchLayoutNewWidth.toInt()
                search.layoutParams = searchLayoutParams
            }
        })
    }
}
