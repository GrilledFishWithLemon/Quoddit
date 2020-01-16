package com.example.quoddit.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quoddit.AddCommentActivity
import com.example.quoddit.CommentActivity
import com.example.quoddit.CommentPost
import com.example.quoddit.Model.Post
import com.example.quoddit.R


class PostAdapter (val context: Context, val PostList: ArrayList<Post>)
    : RecyclerView.Adapter<PostAdapter.holder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_post_layout, parent,false)
        return holder(view)
    }

    override fun getItemCount(): Int {
        return PostList.size
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder?.bind(PostList[position], context)

        holder.commentbutton?.setOnClickListener {
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra("postId", PostList[position].postId)
            intent.putExtra("postTitle", PostList[position].title)
            intent.putExtra("postContent", PostList[position].content)
            intent.putExtra("postDate", PostList[position].date)
            intent.putExtra("postUsername", PostList[position].uid)

            context.startActivity(intent)
        }

    }




    inner class holder(view: View?) : RecyclerView.ViewHolder(view!!)
    {
        val postId = view?.findViewById<TextView>(R.id.post_id)
        val postTitle = view?.findViewById<TextView>(R.id.post_title)
        val postContent = view?.findViewById<TextView>(R.id.post_content)
        val postUsername = view?.findViewById<TextView>(R.id.post_username)
        val postDate = view?.findViewById<TextView>(R.id.post_date)
        val commentbutton = view?.findViewById<ImageView>(R.id.comment_button)


        fun bind(post: Post, context: Context)
        {
            postTitle?.text = post.title
            postContent?.text = post.content
            postUsername?.text = post.uid
            postDate?.text = post.date.toString()
            postId?.text = post.postId


        }
    }



}