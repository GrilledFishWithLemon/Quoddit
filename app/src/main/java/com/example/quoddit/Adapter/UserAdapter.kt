package com.example.quoddit.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.quoddit.Model.User
import com.example.quoddit.ProfileFragment
import com.example.quoddit.R
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text


class UserAdapter (private  var mContext: Context,
                   private var mUser:List<User>,
                   private var isFragment: Boolean = false) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){
    private var firebaseUser : FirebaseUser? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        var user = mUser[position]
        Picasso.get().load(user.getProfileImage()).placeholder(R.drawable.profile).into(holder.userProfileImage)
        holder.userNameTextView.text = user.getUsername()
        holder.userBioTextView.text = user.getProfileBio()

        holder.itemView.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("profileId", user.getUid())
            pref.apply()

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, ProfileFragment()).commit()

        })
    }
    class ViewHolder (@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userNameTextView: TextView = itemView.findViewById(R.id.user_name_search)
        var userBioTextView: TextView = itemView.findViewById(R.id.user_bio_search)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.circleImageView_searchProfile)
    }

}