package com.example.quoddit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePassword : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        firebaseAuth = FirebaseAuth.getInstance().currentUser!!
        btn_change_password.setOnClickListener{
            changePassword()
        }
    }

    private fun changePassword(){

        firebaseAuth.updatePassword(editTextNewPassword.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Re-Authentication success.",
                        Toast.LENGTH_SHORT)


                } else {
                    println("Error Update")
                }
            }
        /*
        if(editTextCurrentPassword.text.isNotEmpty() &&
            editTextNewPassword.text.isNotEmpty() &&
            editTextConfirmPassword.text.isNotEmpty()) {
        }
               /*if (editTextNewPassword.text.toString().equals(editTextConfirmPassword.text.toString())) {


                    if (firebaseAuth != null && firebaseAuth.email != null) {
                        val credential = EmailAuthProvider.getCredential(
                            firebaseAuth.email!!,
                            editTextCurrentPassword.text.toString()
                        )

                        firebaseAuth?.updatePassword(editTextNewPassword.text.toString()).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Toast.makeText(
                                    this,
                                    "Re-Authentication success.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                firebaseAuth?.updatePassword(editTextNewPassword.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Password changed successfully.",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            startActivity(Intent(this, SignInActivity::class.java))
                                            finish()
                                        }
                                    }
                            }else
                            {
                                Toast.makeText(
                                    this,
                                    "Re-Authentication success.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        firebaseAuth?.reauthenticate(credential)?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Re-Authentication success.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                firebaseAuth?.updatePassword(editTextNewPassword.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Password changed successfully.",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            startActivity(Intent(this, SignInActivity::class.java))
                                            finish()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Re-Authentication success.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Password mismatching.", Toast.LENGTH_SHORT).show()

            }*/
         else{
            Toast.makeText(this, "Please enter all the fields.", Toast.LENGTH_SHORT).show()
        }*/
    }
}
