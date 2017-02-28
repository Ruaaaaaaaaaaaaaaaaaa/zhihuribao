package example.com.rxjavatest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bean.LastNews;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by wumingjun1 on 2017/2/16.
 */

public class Test {
    private Context mContext;
    private RequestQueue mRequestQueue;

    public Test(Context context){
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }
    public  void getPic(String url, final ImageView imageView)
    {
        ImageRequest imageRequest = new ImageRequest(
               url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.ic_launcher);
            }
        });

    }

   public void getOBJ(String url,  LastNews bean)
    {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                url,null,new Response.Listener<JSONObject>() {
            LastNews b ;

            @Override
            public void onResponse(JSONObject response) {
                Log.i("qqqq",response.toString());
                Gson gson = new GsonBuilder().serializeNulls().create();
                b = gson.fromJson(String.valueOf(response),LastNews.class);

                Log.i("qqqq",b.toString());
                Log.i("qqqq","111"+b.getStories().toString());
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
}
