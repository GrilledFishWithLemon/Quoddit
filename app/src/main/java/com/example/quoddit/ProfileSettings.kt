package com.example.quoddit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quoddit.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile_settings.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.view.*

class ProfileSettings : AppCompatActivity() {

    private lateinit var firebaseUser : FirebaseUser
    private var checker = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        logout_btn_profile_frag.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@ProfileSettings, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }
        save_profileSettings_btn.setOnClickListener {
            if(checker == "clicked"){

            }else{
                updateUserInfoOnly()
            }


        }
        userInfo()
    }

    private fun updateUserInfoOnly() {

        when {
            userName_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "Please enter username first.", Toast.LENGTH_LONG).show()
            }
            bio_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "Please enter profile bio first.", Toast.LENGTH_LONG).show()
            }
            else -> {
                val userRef = FirebaseDatabase.getInstance().getReference().child("Users")

                val userMap = HashMap<String, Any>()
                userMap["username"] = userName_profile_frag.text.toString().toLowerCase()
                userMap["profileBio"] = bio_profile_frag.text.toString().toLowerCase()

                userRef.child(firebaseUser.uid).updateChildren(userMap)

                Toast.makeText(this, "Account infomation has been updated successfully.", Toast.LENGTH_LONG).show()

                val intent = Intent(this@ProfileSettings, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun userInfo(){
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.uid)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                /* if(context != null){
                     return
                 }*/
                if(p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    userName_profile_frag.setText(user!!.getUsername())
                    bio_profile_frag.setText(user!!.getProfileBio())
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
