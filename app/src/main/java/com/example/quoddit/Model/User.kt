package com.example.quoddit.Model

class User {
    private var profileBio : String = ""
    private var uid: String = ""
    private var username: String = ""

    constructor()

    constructor(username: String, profileBio: String, uid : String){
        this.username = username
        this.profileBio = profileBio
        this.uid = uid
    }

    fun getUsername() : String{
        return username
    }
    fun setUsername(username : String){
        this.username = username
    }
    fun getProfileBio() : String{
        return profileBio
    }
    fun setProfileBio(profileBio: String){
        this.profileBio = profileBio
    }
    fun getUid() : String{
        return uid
    }
    fun setUid(uid : String) {
        this.uid = uid
    }
}