package com.example.quoddit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.recycler_view_post_layout.*

class CommentPost : AppCompatActivity() {

        //private var postId = ""
        //private var postTitle = ""
        //private var postContent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //To set theme
        val sharedpref: SharePref = SharePref(this)

        if (sharedpref.loadNightModeState() == true){
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)

        setContentView(R.layout.recycler_view_post_layout)

        val postIdString = intent.getStringExtra("postId")
        val postTitleString = intent.getStringExtra("postTitle")
        val postContentString = intent.getStringExtra("postContent")
        val postDateString = intent.getStringExtra("postDate")
        val postUsernameString = intent.getStringExtra("postUsername")


        post_id.text = postIdString
        post_title.text = postTitleString
        post_content.text = postContentString
        post_date.text = postDateString
        post_username.text = postUsernameString
    }
}
