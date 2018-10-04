package com.kuwapp.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kuwapp.twitcasting_android.authorize.TwitCastingAuthorizationActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TwitCastingAuthorizationActivity.startActivity(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        TwitCastingAuthorizationActivity.handelOnActivityResult(requestCode, resultCode) { isAllowed ->
            Toast.makeText(this, "Authorization ${if (isAllowed) "success" else "failure"}", Toast.LENGTH_SHORT).show()
        }
    }

}
