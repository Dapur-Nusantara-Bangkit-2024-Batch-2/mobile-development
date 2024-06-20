package com.dicoding.dapurnusantara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.dicoding.dapurnusantara.R

class OnboardingPagerAdapter(private val context: Context) : PagerAdapter() {
    private val slideImages =
        intArrayOf(R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3)
    private val slideTitles = arrayOf(
        context.getString(R.string.slide_1_title),
        context.getString(R.string.slide_2_title),
        context.getString(R.string.slide_3_title)
    )

    private val slideDescriptions = arrayOf(
        context.getString(R.string.slide_1_subtitle),
        context.getString(R.string.slide_2_subtitle),
        context.getString(R.string.slide_3_subtitle)
    )

    private val slideProgress =
        intArrayOf(R.drawable.progress_1, R.drawable.progress_2, R.drawable.progress_3)

    override fun getCount(): Int {
        return slideImages.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val slideLayout = inflater.inflate(R.layout.slide_layout, container, false)

        val slideImageView = slideLayout.findViewById<ImageView>(R.id.slideImage)
        val slideTitleView = slideLayout.findViewById<TextView>(R.id.slideTitle)
        val slideDescriptionView = slideLayout.findViewById<TextView>(R.id.slideDescription)

        slideImageView.setImageResource(slideImages[position])
        slideTitleView.text = slideTitles[position]
        slideDescriptionView.text = slideDescriptions[position]

        container.addView(slideLayout)

        return slideLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}