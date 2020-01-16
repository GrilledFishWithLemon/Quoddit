package com.example.quoddit

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_add_comment.*

class AddCommentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //To set theme
        val sharedpref: SharePref = SharePref(this)

        if (sharedpref.loadNightModeState() == true){
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_add_comment)

        imageButtonPostComment.setOnClickListener {
            postComment()
        }
    }

    private fun postComment(){

        if(TextUtils.isEmpty(editTextCommentBox.text)){
            editTextCommentBox.error = getString(R.string.error_value_required)
            return
        }
        val name = editTextCommentBox.text.toString()

        val intent = Intent()
        intent.putExtra(EXTRA_COMMENT, name)

        setResult(Activity.RESULT_OK, intent)

        finish()
    }

    companion object{
        const val EXTRA_COMMENT = "New comment."
    }
}
