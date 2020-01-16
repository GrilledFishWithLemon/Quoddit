package com.example.quoddit

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import kotlinx.android.synthetic.main.activity_comment.*
import org.json.JSONArray
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth

class CommentActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1
    lateinit var commentList: ArrayList<Comment>
    lateinit var adapter: CommentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //To set theme
        val sharedpref: SharePref = SharePref(this)

        if (sharedpref.loadNightModeState() == true){
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_comment)

        val postIdString = intent.getStringExtra("postId")
        val postTitleString = intent.getStringExtra("postTitle")
        val postContentString = intent.getStringExtra("postContent")
        val postDateString = intent.getStringExtra("postDate")
        val postUsernameString = intent.getStringExtra("postUsername")


        textViewPostID.text = postIdString
        textViewTitle.text = postTitleString
        textViewPost.text = postContentString
        textViewDate.text = postDateString
        textViewUser.text = postUsernameString

        commentList = ArrayList<Comment>()

        adapter = CommentListAdapter(this)
        adapter.setComments(commentList)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComment)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        imageButtonComment.setOnClickListener {
            val intent = Intent(this, AddCommentActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        syncComments()

    }




    private fun syncComments() {
        val url = getString(R.string.url_server) + getString(R.string.url_comments_readLatest) + "?postid=" +textViewPostID.text

        //Delete all user records
        commentList.clear()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val jsonArray: JSONArray = jsonResponse.getJSONArray("records")
                        val size: Int = jsonArray.length()
                        for(i in 0..size-1){
                            var jsonUser: JSONObject = jsonArray.getJSONObject(i)
                            var comment: Comment = Comment(jsonUser.getString("postID"), jsonUser.getString("uid"), jsonUser.getString("comments"))

                            commentList.add(comment)
                        }
                        adapter.notifyDataSetChanged()
                        //Toast.makeText(applicationContext, "Record found :$size", Toast.LENGTH_LONG).show()

                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))


                }
            },
            Response.ErrorListener { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))

            }
        )

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                data?.let{
                    val comment = Comment(textViewPostID.text.toString(),FirebaseAuth.getInstance().currentUser!!.uid,it.getStringExtra(AddCommentActivity.EXTRA_COMMENT)!!.toString())
                    postComment(comment)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun postComment(comment: Comment) {
        val url = getString(R.string.url_server) + getString(R.string.url_comment_create) + "?postid=" + textViewPostID.text + "&uid=" + FirebaseAuth.getInstance().currentUser!!.uid +"&comments=" + comment.commentContent


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val success: String = jsonResponse.get("success").toString()

                        if(success.equals("1")){
                            Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                            //Add record to user list
                            commentList.add(comment)
                        }else{
                            Toast.makeText(applicationContext, "Record not saved", Toast.LENGTH_LONG).show()
                        }

                    }
                }catch (e:Exception){
                    Log.d("Main", "Response: %s".format(e.message.toString()))

                }
            },
            Response.ErrorListener { error ->
                Log.d("Main", "Response: %s".format(error.message.toString()))
            }
        )

        //Volley request policy, only one time request
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, //no retry
            1f
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}
