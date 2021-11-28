package com.uzair.myapplication.ui

import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.viewpager.widget.PagerAdapter
import com.uzair.myapplication.R

class VideoAdapter(private val list: List<Triple<String, String, Drawable?>>) : PagerAdapter() {


    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(collection.context)
        val viewGroup = if (position == 0) {
            (inflater.inflate(R.layout.video_item, collection, false) as ViewGroup).apply {
                val videoView = findViewById<VideoView>(R.id.video)
                videoView.setVideoURI(collection.context.resourceUri(R.raw.zoo))
                videoView.setOnPreparedListener {
                    it.start()
                    it.isLooping = true

                }
                videoView.setTag("videoViewTag")
            }

        } else {
            (inflater.inflate(R.layout.image_item, collection, false) as ViewGroup).apply {
                val image = findViewById<ImageView>(R.id.image)
                image.setImageDrawable(list[position].third)
            }
        }

        collection.addView(viewGroup)
        return viewGroup
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, object1: Any): Boolean {
        return view === object1
    }


    override fun getCount(): Int {
        return list.size
    }

    fun Context.resourceUri(resourceId: Int): Uri = with(resources) {

        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(getResourcePackageName(resourceId))
            .appendPath(getResourceTypeName(resourceId))
            .appendPath(getResourceEntryName(resourceId))
            .build()
    }
}