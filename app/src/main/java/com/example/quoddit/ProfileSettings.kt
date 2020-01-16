package com.example.quoddit

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quoddit.Model.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
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
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.view.*

class ProfileSettings : AppCompatActivity() {

    private lateinit var firebaseUser : FirebaseUser
    private var checker = ""
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //To set theme
        val sharedpref: SharePref = SharePref(this)

        if (sharedpref.loadNightModeState() == true){
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_profile_settings)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")
        close_profileSettings_btn.setOnClickListener{
            finish()
        }
        logout_btn_profile_frag.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@ProfileSettings, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }


        change_image_textViewButton.setOnClickListener{
            checker = "clicked"

            CropImage.activity()
                .setAspectRatio(1,1)
                .start(this@ProfileSettings)

        }


        save_profileSettings_btn.setOnClickListener {
            if(checker == "clicked"){
                uploadImageAndUpdateInfo()
            }else{
                updateUserInfoOnly()
            }


        }
        userInfo()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data!=null){
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            circleImageView_profileSettings.setImageURI(imageUri)
        }
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
                val userRef = FirebaseDatabase.getInstance().reference.child("Users")

                val userMap = HashMap<String, Any>()
                userMap["username"] = userName_profile_frag.text.toString().toLowerCase()
                userMap["profileBio"] = bio_profile_frag.text.toString().toLowerCase()

                userRef.child(firebaseUser.uid).updateChildren(userMap)

                Toast.makeText(this, "Account infomation has been updated successfully.", Toast.LENGTH_LONG).show()

                val intent = Intent(this@ProfileSettings, MainActivity::class.java)

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
                    Picasso.get().load(user!!.getProfileImage()).placeholder(R.drawable.profile).into(circleImageView_profileSettings)
                    userName_profile_frag.setText(user!!.getUsername())
                    bio_profile_frag.setText(user!!.getProfileBio())
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun uploadImageAndUpdateInfo() {

        when {
            imageUri == null -> Toast.makeText(this, "Please select image first.", Toast.LENGTH_LONG).show()
            userName_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "Please enter username first.", Toast.LENGTH_LONG).show()
            }
            bio_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "Please enter profile bio first.", Toast.LENGTH_LONG).show()
            }
            else ->{
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Account settings")
                progressDialog.setTitle("Updating profile...")
                progressDialog.show()

                val fileRef = storageProfilePicRef!!.child(firebaseUser!!.uid + ".jpg")
                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)
                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{  task ->
                    if (task.isSuccessful){
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener(OnCompleteListener <Uri>{task ->
                    if(task.isSuccessful){
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("Users")

                        val userMap = HashMap<String, Any>()
                        userMap["username"] = userName_profile_frag.text.toString().toLowerCase()
                        userMap["profileBio"] = bio_profile_frag.text.toString().toLowerCase()
                        userMap["profileImage"] = myUrl

                        ref.child(firebaseUser.uid).updateChildren(userMap)

                        val intent = Intent(this@ProfileSettings, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    }
                    else{
                        progressDialog.dismiss()
                    }

                })
            }
        }

    }
}
