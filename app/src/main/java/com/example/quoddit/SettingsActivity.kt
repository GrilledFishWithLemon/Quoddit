package com.example.quoddit

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.quoddit.Model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_profile_settings.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*

class SettingsActivity : AppCompatActivity() {
    private var eyp: Switch? = null
    internal lateinit var sharedpref: SharePref
    lateinit var languageBtn : Button
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedpref = SharePref(this)
        if (sharedpref.loadNightModeState() == true){
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)


        loadLocate()

        eyp = findViewById<View>(R.id.enableDark) as Switch?
        if(sharedpref.loadNightModeState() == true){
            eyp!!.isChecked = true
        }

        eyp!!.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked){
                sharedpref.setNightModeState(true)
                restartApp()
            }else{
                sharedpref.setNightModeState(false)
                restartApp()
            }
        }
        //Change Language
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)

        languageBtn = findViewById(R.id.btn_changeLanguage)

        languageBtn.setOnClickListener{
            showChangeLanguage()
        }

        //Edit Profile
        btn_editProfile.setOnClickListener{
            val intent = Intent(this, ProfileSettings::class.java)
            startActivity(intent)

        }

        //Change Password
        btn_changePassword.setOnClickListener{
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        //Log Out
        btn_logout.setOnClickListener{

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@SettingsActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }

    fun restartApp(){
        val i = Intent(getApplicationContext(), MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun showChangeLanguage(){
        val listLanguage = arrayOf("English","Malay","Chinese")

        val nBuilder = AlertDialog.Builder(this)
        nBuilder.setTitle("Choose Language")
        nBuilder.setSingleChoiceItems(listLanguage, -1) { dialog, which ->
            if (which == 0) {
                setLocate("en")
                recreate()
            } else if (which == 1) {
                setLocate("b+ace")
                recreate()
            } else if (which == 2) {
                setLocate("ms")
                recreate()
            }

            dialog.dismiss()
        }
        val nDialog = nBuilder.create()
        nDialog.show()
    }

    private fun setLocate(Lang: String){
        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Language", Lang)
        editor.apply()
    }

    private fun loadLocate(){
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Language", "")
        setLocate(language.toString())
    }



}
