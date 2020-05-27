package com.gibsoncodes.weatherapp.ui.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gibsoncodes.weatherapp.appcomponent.App
import com.gibsoncodes.weatherapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this,
            App.injectGoogleSignInOptions()
        )
        sign_in_button.setOnClickListener {
            signIn()
        }
    }
        private fun signIn(){
         googleSignInClient.signInIntent.also {
             intent-> startActivityForResult(intent,100) }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }
    //TODO: put a better tag instead of name
    private fun updateUi(user: FirebaseUser?){
        if (user!=null){
            sign_in_button.visibility= View.GONE
            if (user.displayName?.isNotEmpty()!!){

               /* val intent=Intent(this,MainScreen::class.java).also {
                    intent-> intent.putExtra(NameTag.name,user.displayName?.get(0).toString())
                }
                startActivity(intent)*/
                startActivity(Intent(this,TestActivity::class.java))
                finish()
            }
        }
    }

    object NameTag {
       const val name="_nameTag"
    }

    private fun handleSignInResult(completedTask:Task<GoogleSignInAccount>){
        try{
           val account= completedTask.getResult(ApiException::class.java)
            fireBaseAuthWithGoogle(account?.idToken)
        }catch (e: ApiException){
            updateUi(null)
            Timber.e("Failed ${e.message}")
            e.printStackTrace()
        }
    }
    private fun fireBaseAuthWithGoogle(idToken:String?){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener(this){
            if (it.isSuccessful){
                val user= auth.currentUser
                Timber.d("Sign in successful")
                updateUi(user)
            }else{
                Snackbar.make(mainCord,"Failed to sign in please try again (;-",Snackbar.LENGTH_LONG)
                    .setTextColor(Color.WHITE)
                    .setBackgroundTint(getColor(R.color.colorPrimary))
                    .show()
                Timber.e("Failed to sign in")
                updateUi(null)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        val currentUser= auth.currentUser
        updateUi(currentUser)
    }
      }