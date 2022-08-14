package com.ss_technology.mushinprojectandroidapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONObject;

public class Website extends AppCompatActivity {

    WebView webView;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.website);

        KeepMeLogin keepMeLogin=new KeepMeLogin(getApplicationContext());
        try{
            JSONObject object=new JSONObject(keepMeLogin.getData());
            webView=findViewById(R.id.webview);
            WebSettings settings=webView.getSettings();
            settings.setJavaScriptEnabled(true);
            webView.setWebChromeClient( new MyWebChromeClient());
            webView.setWebViewClient( new webClient());
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(BaseURL.MainURL());
            progress=findViewById(R.id.progress);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error occurred in json parsing!", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(newProgress);
        }
    }

    public class webClient extends WebViewClient {
        public boolean  shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress.setVisibility(View.GONE);
        }
    }

   @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            progress.setVisibility(View.GONE);
        } else  {
            super.onBackPressed();
        }
    }

}