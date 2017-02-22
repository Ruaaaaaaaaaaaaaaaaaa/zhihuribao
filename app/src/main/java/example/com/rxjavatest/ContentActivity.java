package example.com.rxjavatest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bean.ContentBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class ContentActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("title");
        ImageView TopImageView = (ImageView) findViewById(R.id.top_image);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRequestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        String s = intent.getStringExtra("id");

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String url = "http://news-at.zhihu.com/api/4/news/"+s;
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                url
                ,null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                Log.i("content","1");
                ContentBean b = gson.fromJson(String.valueOf(response),ContentBean.class);
                Log.i("content",b.toString());
                webView.loadDataWithBaseURL("file:///android_asset/",
                        "<link rel=\"stylesheet\" href=\"file:///android_asset/news_qa.auto.css\" type=\"text/css\" />"+
                        b.getBody() , "text/html", "utf-8", null);
                Glide.with(ContentActivity.this).load(b.getImage()).
                        into(TopImageView );
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error==null) {
                    Log.i("qqqq", "null");
                }else
                {
                    Log.i("qqqq", error.toString());
                }
            }
        });
        mRequestQueue.add(jr);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
