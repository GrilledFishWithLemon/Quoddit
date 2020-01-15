package com.example.quoddit.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quoddit.Adapter.PostAdapter
import com.example.quoddit.AddPostActivity
import com.example.quoddit.Model.Post
import com.example.quoddit.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var postAdapter: PostAdapter? = null
    private var mPost: MutableList<Post>? = null

   // private lateinit var homeViewModel: HomeViewModel
    //Not using viewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        recyclerView = view.findViewById(R.id.recycleview_posts)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mPost = ArrayList()
        postAdapter = context?.let { PostAdapter(it, mPost as ArrayList<Post> ) }
        recyclerView?.adapter = postAdapter

        retrievePosts()




        view.addPost_btn.setOnClickListener{
            startActivity(Intent(context, AddPostActivity::class.java))
        }

       /* homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)*/

        /*val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })*/
        return view




    }

    private fun retrievePosts() {
        val postRef = FirebaseDatabase.getInstance().getReference().child("Posts")
        postRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mPost?.clear()
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(Post::class.java)
                    if(post!=null){
                        mPost?.add(post)
                    }
                }
                postAdapter?.notifyDataSetChanged()
            }
            override fun onCancelled(p0:DatabaseError){

            }

        })
    }


}