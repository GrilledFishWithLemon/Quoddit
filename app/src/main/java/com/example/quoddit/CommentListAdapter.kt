package com.example.quoddit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var comments = emptyList<Comment>() // Cached copy of user

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pidTextView: TextView = itemView.findViewById(R.id.textViewPID)
        val uidTextView: TextView = itemView.findViewById(R.id.textViewUID)
        val commentTextView: TextView = itemView.findViewById(R.id.textViewComments)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = inflater.inflate(R.layout.comment_record, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val current = comments[position]
        holder.pidTextView.text = current.postID
        holder.uidTextView.text = current.uid
        holder.commentTextView.text = current.commentContent

    }

    internal fun setComments(comments: List<Comment>) {
        this.comments = comments
        notifyDataSetChanged()
    }
}