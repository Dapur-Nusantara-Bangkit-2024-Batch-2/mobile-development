package com.dicoding.dapurnusantara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.dicoding.dapurnusantara.R
import com.dicoding.dapurnusantara.databinding.SlideLayoutBinding

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
        val binding = SlideLayoutBinding.inflate(inflater, container, false)

        binding.slideImage.setImageResource(slideImages[position])
        binding.slideTitle.text = slideTitles[position]
        binding.slideDescription.text = slideDescriptions[position]

        container.addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}