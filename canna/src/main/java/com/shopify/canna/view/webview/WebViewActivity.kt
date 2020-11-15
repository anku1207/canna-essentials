package com.shopify.canna.view.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.shopify.canna.R
import kotlinx.android.synthetic.main.activity_user__login.*
import kotlinx.android.synthetic.main.activity_web_view.*
import timber.log.Timber

class WebViewActivity : AppCompatActivity() {

    companion object {
        val TITLE = "title"
        val URL = "url"
        fun launchActivity(startingActivity: Context, title: String?, url: String) {
            val intent = Intent(startingActivity, WebViewActivity::class.java)
            title?.let { intent.putExtra(TITLE, title) }
            intent.putExtra(URL, url)
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        if (intent.hasExtra(TITLE)) {
            toolbar.setTitleTextAppearance(this@WebViewActivity, R.style.SweetSansMediumTextAppearance)
            toolbar.title = intent.getStringExtra(TITLE)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        } else {
            toolbar.visibility = View.GONE
        }
        val url = intent.getStringExtra(URL)
        Timber.d("URL : "+url)

        webview_url.settings.javaScriptEnabled = true
        webview_url.settings.builtInZoomControls = true
        webview_url.requestFocus()
        webview_url.loadUrl(intent.getStringExtra(URL))
        webview_url.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                Timber.d("URL : "+intent.getStringExtra(URL))
                progress_webview?.visibility = View.VISIBLE
                view?.loadUrl(intent.getStringExtra(URL))
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress_webview?.visibility = View.GONE
                super.onPageFinished(view, url)
            }
        }
    }

    open class AppWebViewClient : WebViewClient(){
        val progressBar : ProgressBar? = null

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}