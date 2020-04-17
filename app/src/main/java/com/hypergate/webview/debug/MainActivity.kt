package com.hypergate.webview.debug

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.http.SslError
import android.webkit.SslErrorHandler




class MainActivity : AppCompatActivity() {

    private inner class SSLTolerentWebViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed() // Ignore SSL certificate errors
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        

        val webview = WebView(this)
        webview.setWebViewClient(object : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                // DO NOT CALL SUPER METHOD
                //super.onReceivedSslError(view, handler, error)
            }
        })

        webview.loadUrl("https://spnego.kubernetes.papers.tech/hypergate_demo_page/index.asp")
        val webSettings = webview.getSettings()
        webSettings.setJavaScriptEnabled(true)
        WebView.setWebContentsDebuggingEnabled(true)
        webview.webViewClient= SSLTolerentWebViewClient()


        setContentView(webview)
    }
}
