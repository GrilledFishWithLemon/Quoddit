package com.example.quoddit


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import android.widget.Toast.makeText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_signin_signup.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
        }

        btn_register.setOnClickListener {
            CreateAccount()
        }
    }

    private fun CreateAccount() {
        val email = email_signup.text.toString()
        val userName = usernameSignup.text.toString()
        val password = password_signup.text.toString()

        when {
            TextUtils.isEmpty(email) -> makeText(this,"email is required.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(userName) -> makeText(this,"username is required.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) -> makeText(this,"password is required.", Toast.LENGTH_LONG).show()

            else ->{


                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful){
                            saveUserInfo(email, userName, password )
                        }
                        else{
                            val message = task.exception!!.toString()
                            makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                            mAuth.signOut()

                        }
                    }
            }
        }
    }

    private fun saveUserInfo(email: String, userName: String, password: String) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["email"] = currentUserID
        userMap["password"] = currentUserID
        userMap["profileBio"] = "this is my bio."

        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){

                    makeText(this, "Account has been created successfully.", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else{
                    val message = task.exception!!.toString()
                    makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()

                }
            }
    }
}
