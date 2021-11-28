package com.uzair.myapplication.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager.widget.ViewPager
import com.uzair.myapplication.R
import com.uzair.myapplication.databinding.ActivityMainBinding


class MainActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPager()
    }

    override fun onResume() {
        super.onResume()
        binding.recycler.currentItem = 0
    }

    private fun setPager() {
        setPadding()
        val strings = getData()
        val videoAdapter = VideoAdapter(strings)
        binding.recycler.adapter = videoAdapter
        binding.recycler.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                descriptionAnimation(strings, position)
                if (position == 0) {
                    playVideo()
                } else {
                    pauseVideo()
                }
            }
        })
    }

    private fun descriptionAnimation(
        strings: List<Triple<String, String, Drawable?>>,
        position: Int
    ) {
        slideDown {
            binding.desc.text = strings[position].second
            binding.descHeading.text = strings[position].first
            slideUp()
        }
    }

    private fun pauseVideo() {
        binding.recycler.findViewWithTag<VideoView>("videoViewTag")?.pause()
    }

    private fun playVideo() {
        binding.recycler.findViewWithTag<VideoView>("videoViewTag")?.start()
    }

    private fun getData(): List<Triple<String, String, Drawable?>> {

        return listOf(
            Triple(
                getString(R.string.desc_heading1),
                getString(R.string.description1),
                AppCompatResources.getDrawable(this, R.drawable.card)
            ),
            Triple(
                getString(R.string.desc_heading2),
                getString(R.string.description2),
                AppCompatResources.getDrawable(this, R.drawable.card)
            ),
            Triple(
                getString(R.string.desc_heading3),
                getString(R.string.description3),
                AppCompatResources.getDrawable(this, R.drawable.outfit)
            ),
            Triple(
                getString(R.string.desc_heading4),
                getString(R.string.description4),
                AppCompatResources.getDrawable(this, R.drawable.ring)
            ),
            Triple(
                getString(R.string.desc_heading5),
                getString(R.string.description5),
                AppCompatResources.getDrawable(this, R.drawable.necklace)
            )
        )
    }

    private fun setPadding() {
        val padding = resources.getDimension(R.dimen.margin_51).toInt()
        binding.recycler.clipToPadding = false
        binding.recycler.setPaddingRelative(padding, 0, padding, 0)
        binding.recycler.pageMargin = resources.getDimension(R.dimen.margin_15).toInt()
    }


    // slide the view from below itself to the current position
    private fun slideUp() {
        val animate = TranslateAnimation(
            ZERO,  // fromXDelta
            ZERO,  // toXDelta
            binding.descLayout.height.toFloat().div(ANIMATION_DELTA_FACTOR),  // fromYDelta
            ZERO
        )
        animate.duration = ANIMATION_DURATION
        animate.fillAfter = true
        binding.descLayout.startAnimation(animate)
    }

    // slide the view from its current position to below itself
    private fun slideDown(callback: () -> Unit) {
        val animate = TranslateAnimation(
            ZERO,  // fromXDelta
            ZERO,  // toXDelta
            ZERO,  // fromYDelta
            binding.descLayout.height.toFloat().div(ANIMATION_DELTA_FACTOR)
        ) // toYDelta
        animate.duration = ANIMATION_DURATION
        animate.fillAfter = true
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) = Unit
            override fun onAnimationEnd(animation: Animation?) = callback()
            override fun onAnimationRepeat(animation: Animation?) = Unit
        })
        binding.descLayout.startAnimation(animate)
        setAlphaAnimation(binding.descLayout)
    }


    private fun setAlphaAnimation(v: View?) {
        val fadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f)
        fadeOut.duration = ANIMATION_DURATION
        val fadeIn = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f)
        fadeIn.duration = ANIMATION_DURATION
        val mAnimationSet = AnimatorSet()
        mAnimationSet.play(fadeIn).after(fadeOut)
        mAnimationSet.start()
    }

    companion object {
        private const val ANIMATION_DURATION: Long = 500
        private const val ANIMATION_DELTA_FACTOR = 4
        private const val ZERO: Float = 0F
    }
}