package com.kuwapp.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.kuwapp.twitcasting_android.api.TwitCastingApi
import com.kuwapp.twitcasting_android.authorize.TwitCastingAuthorizationActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.authorization).setOnClickListener {
            TwitCastingAuthorizationActivity.startActivity(this)
        }
        findViewById<View>(R.id.user_info_api).setOnClickListener {
            TwitCastingApi.client().getUserInfo("kuwapp_dev")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ json ->
                        Toast.makeText(this, json.toString(), Toast.LENGTH_SHORT).show()
                    }, { e ->

                    })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        TwitCastingAuthorizationActivity.handelOnActivityResult(requestCode, resultCode) { isAllowed ->
            Toast.makeText(this, "Authorization ${if (isAllowed) "success" else "failure"}", Toast.LENGTH_SHORT).show()
        }
    }

}
