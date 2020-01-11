package com.example.quoddit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btn_signup.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
    //TODO : finish up signinactivity, signup with firebase is successfully added
}
