package com.kuwapp.twitcasting_android.authorize

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kuwapp.twitcasting_android.core.TwitCasting

class TwitCastingAuthorizationActivity : AppCompatActivity() {

    companion object {

        private const val REQUEST_CODE = 2257
        private const val RESULT_ALLOWED = 2646

        private const val KEY_URI = "key_uri"

        fun startActivity(activity: Activity) {
            val config = TwitCasting.config()
            val uri = Uri.parse("https://apiv2.twitcasting.tv/oauth2/authorize?client_id=${config.clientId}&response_type=token")
            val intent = Intent(activity, TwitCastingAuthorizationActivity::class.java).apply {
                putExtra(KEY_URI, uri)
            }
            activity.startActivityForResult(intent, REQUEST_CODE)
        }

        fun handelOnActivityResult(requestCode: Int, resultCode: Int, authorizeResultFun: (isAllowed: Boolean) -> Unit) {
            if (requestCode != REQUEST_CODE) return
            authorizeResultFun(resultCode == RESULT_ALLOWED)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.getParcelableExtra<Uri>(KEY_URI)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val intent = intent ?: return
        if (intent.action != Intent.ACTION_VIEW) return
        val uri = intent.data ?: return
        val uriHandler = TwitcastingUri(uri)
        uriHandler.accessToken()?.let {
            setResult(RESULT_ALLOWED)
        }
        finish()
    }

}

internal class TwitcastingUri(private val uri: Uri) {

    internal fun isValid() = uri.fragment?.startsWith("access_token") ?: false

    internal fun accessToken(): String? {
        if (!isValid()) return null
        return uri.fragment.split("&")
                .firstOrNull { it.startsWith("access_token") }
                ?.split("=")
                ?.getOrNull(1)
    }

}



