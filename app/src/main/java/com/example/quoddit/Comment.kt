package com.example.quoddit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="comment")
data class Comment(val postID: String,
                   val uid: String,
                   val commentContent: String
                   ) {

}

