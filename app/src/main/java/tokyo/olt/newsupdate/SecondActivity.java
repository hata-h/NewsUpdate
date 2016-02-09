package tokyo.olt.newsupdate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.media.MediaPlayer;

//import java.io.IOException;

public class SecondActivity extends AppCompatActivity {
    WebView  wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        wv = (WebView)findViewById(R.id.webView1);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Intent i = getIntent();//Input Data Retrieve
        String url = i.getStringExtra("URL");//Input Data Retrieve
        wv.setWebViewClient(new MyWebViewClient(this));
        wv.loadUrl(url);
    }
    @Override
    protected
    void onPause(){
        super.onPause();
        wv.setWebViewClient(null);
        wv.loadData("", "text/html; charset=utf-8", "UTF-8");
        finish();
    }
}
class MyWebViewClient extends WebViewClient{
    public MediaPlayer mp;
    private Context context = null;

    public MyWebViewClient(Context c){
        this.context = c;
    }
    @Override
    public void onLoadResource(WebView webview, String url)
    {
        if(url.endsWith(".mp3"))
        {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "audio/mp3");
            context.startActivity(intent);
        }else if(url.endsWith(".mp4")) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "video/mp4");
            context.startActivity(intent);
        }
        else if(url.indexOf("movie.html?")>0){
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}
