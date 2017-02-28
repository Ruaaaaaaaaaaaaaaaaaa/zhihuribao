package com.example.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by wumingjun1 on 2017/2/20.
 */

public class VolleyUtils {
    private static VolleyUtils volleyUtils;
    private RequestQueue mRequestQueue;
    private String url = "http://news-at.zhihu.com/api/4/";

    private VolleyUtils(Context context){
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyUtils getInstance(Context context){
        if (volleyUtils==null){
            volleyUtils = new VolleyUtils(context);
        }
        return volleyUtils;
    }

    public void JSRequest(String s, final Class c ){
        url = url+s;
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                url
                , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                 gson.fromJson(String.valueOf(response), c);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null) {
                    Log.i("qqqq", "null");
                } else {
                    Log.i("qqqq", error.toString());
                }
            }
        });
        mRequestQueue.add(jr);
    }
}
