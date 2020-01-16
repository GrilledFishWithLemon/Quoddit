package com.example.quoddit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quoddit.Model.Post
import com.example.quoddit.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_fragment.view.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.database.DatabaseError


class AddPostActivity : AppCompatActivity()
{
    private lateinit var profileId:String
    lateinit var postTitle: EditText
    lateinit var postContent: EditText
    lateinit var savePostBtn: ImageView
    lateinit var closePostBtn: ImageView
    private lateinit var firebaseUser : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?)
    {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        super.onCreate(savedInstanceState)

        //To set theme
        val sharedpref: SharePref = SharePref(this)

        if (sharedpref.loadNightModeState() == true){
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_add_post)

        postTitle = findViewById(R.id.post_title)
        postContent = findViewById(R.id.post_content)
        savePostBtn = findViewById(R.id.save_post_btn)
        closePostBtn = findViewById(R.id.close_post_btn)


        savePostBtn.setOnClickListener {
            savePost()

        }

        closePostBtn.setOnClickListener {
            finish()
        }

    }


    private fun savePost() {
        val title = postTitle.text.toString()
        val content = postContent.text.toString()
        val date = System.currentTimeMillis()
        val profileId = FirebaseAuth.getInstance().currentUser!!.uid
        val user = FirebaseAuth.getInstance().currentUser
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(profileId)
        var username = ""





        if (title.isEmpty())
        {
            postTitle.error = "Please enter a title."
            return
        }

        if (content.isEmpty())
        {
            postContent.error = "Please fill up the content."
            return
        }


        val ref = FirebaseDatabase.getInstance().getReference("Posts")

        val postId = ref.push().key

        val post = Post(postId, title, content, date, profileId, username)

        if (postId != null) {
            ref.child(postId).setValue(post).addOnCanceledListener {
                Toast.makeText(this, "Post saved successfully.", Toast.LENGTH_LONG).show()
                val intent = Intent(this@AddPostActivity, MainActivity::class.java)
                startActivity(intent)
                finish()



    }
}

    }





    /*private var myUrl = ""
    private var imageUri: Uri? = null
    private var storagePostRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        storagePostRef = FirebaseStorage.getInstance().reference.child("Post image")

        save_post_btn.setOnClickListener { uploadImage() }

        CropImage.activity()
            .setAspectRatio(2,1)
            .start(this@AddPostActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri

        }
    }

    private fun uploadImage() {
        when {
            TextUtils.isEmpty(post_title.text.toString()) -> Toast.makeText(
                this,
                "Title is required.",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(post_content.text.toString()) -> Toast.makeText(
                this,
                "Content is required.",
                Toast.LENGTH_LONG
            ).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading post")
                progressDialog.setMessage("Please wait for awhile...")
                progressDialog.show()

                val fileRef = storagePostRef!!.child(System.currentTimeMillis().toString() + ".jpg")

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                })
                    .addOnCompleteListener(OnCompleteListener<Uri> { task ->
                        if (task.isSuccessful)  {
                            val downloadUri = task.result
                            myUrl = downloadUri.toString()

                            val ref = FirebaseDatabase.getInstance().reference.child("Posts")

                            val postId = ref.push().key
                            val postdate = System.currentTimeMillis()

                            val postMap = HashMap<String, Any>()
                            postMap["postid"] = postId!!
                            postMap["date"] = postdate
                            postMap["title"] = post_title.text.toString()
                            postMap["content"] = post_content.text.toString()
                            postMap["image"] = myUrl
                            postMap["uid"] = FirebaseAuth.getInstance().currentUser!!.uid

                            ref.child(postId).updateChildren(postMap)

                            Toast.makeText(this, "Post uploaded successfully.", Toast.LENGTH_LONG)
                                .show()

                            val intent = Intent(this@AddPostActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            progressDialog.dismiss()
                        }


                    })
            }
        }
    }*/

}