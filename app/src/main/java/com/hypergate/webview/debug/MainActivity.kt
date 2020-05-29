package com.hypergate.webview.debug

import android.Manifest
import android.content.Context
import android.content.RestrictionsManager
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.http.SslError
import android.os.Build
import android.webkit.SslErrorHandler
import androidx.appcompat.app.AppCompatActivity
import com.hypergate.webview.debug.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private inner class SSLTolerentWebViewClient : WebViewClient() {
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed() // Ignore SSL certificate errors
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.logEditText.setText("${binding.logEditText.text}\nGENERAL DETAILS:")
        binding.logEditText.setText("${binding.logEditText.text}\n\t\tSDK Version: ${Build.VERSION.SDK_INT}")

        binding.logEditText.setText("${binding.logEditText.text}\nWEBVIEW DETAILS:")
        binding.logEditText.setText("${binding.logEditText.text}\n\t\tpackagename: ${WebView.getCurrentWebViewPackage()?.applicationInfo?.packageName}")
        binding.logEditText.setText("${binding.logEditText.text}\n\t\tversionname:${WebView.getCurrentWebViewPackage()?.versionName}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.logEditText.setText("${binding.logEditText.text}\n\t\tversioncode:${WebView.getCurrentWebViewPackage()?.longVersionCode}")
        }

        binding.logEditText.setText("${binding.logEditText.text}\nPERMISSION DETAILS:")
        binding.logEditText.setText(
            "${binding.logEditText.text}\n\t\thas INTERNET: ${this.checkSelfPermission(
                Manifest.permission.INTERNET
            )}"
        )
        binding.logEditText.setText(
            "${binding.logEditText.text}\n\t\thas GET_ACCOUNTS:${this.checkSelfPermission(
                Manifest.permission.GET_ACCOUNTS
            )}}"
        )

        val restrictionsMgr =
            this.getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager
        val bundle = restrictionsMgr.applicationRestrictions
        binding.logEditText.setText("${binding.logEditText.text}\nMANAGED CONFIGURATIONS:")
        if (bundle.keySet().size > 0) {
            for (key in bundle.keySet()) {
                binding.logEditText.setText(
                    "${binding.logEditText.text}\n\t\t${key}:'${bundle.get(
                        key
                    )}'"
                )
            }
        } else {
            binding.logEditText.setText("${binding.logEditText.text}\n\t\tNo Managed Configuration Found")
        }

        WebView.setWebContentsDebuggingEnabled(true)

        binding.contentWebview.setWebViewClient(object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                // DO NOT CALL SUPER METHOD
                //super.onReceivedSslError(view, handler, error)
            }
        })
        binding.contentWebview.getSettings().setJavaScriptEnabled(true)
        binding.contentWebview.webViewClient = SSLTolerentWebViewClient()

        binding.goButton.setOnClickListener {
            binding.contentWebview.loadUrl(binding.urlEditText.text.toString())
        }

        setContentView(binding.root)
    }
}
