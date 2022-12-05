package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.paszlelab.dcroarapp.R;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TermsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_terms_page);


        WebView mywebView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        mywebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }

        });

        mywebView.loadUrl("file:///android_asset/dcr_terms.html");

    }

    public void goBack(View v){
        TermsPage.this.finish();
    }

    public void goToRegistration(View v){

        startActivityForResult(new Intent(this, RegistrationPage.class),1);
    }
}