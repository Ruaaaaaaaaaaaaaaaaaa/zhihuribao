package example.com.rxjavatest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.security.MessageDigest;

import rx.Observable;
import rx.Subscriber;





public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);

        Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {

                subscriber.onNext(d);
                subscriber.onCompleted();
            }

        })
                .subscribe(image -> img.setImageDrawable(image));

        Observable.just("123")
                .map(s -> s + "world")
                .subscribe(s -> Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show());

        mRequestQueue = Volley.newRequestQueue(this);

        String appkey = "f3bb208b3d081dc8";
        String secretkey = "ea85624dfcf12d7cc7b2b3a94fac1f2c";
        int cid = 805489;
        String sign_this = string2MD5("appkey=" + appkey + "&cid=" + cid + secretkey);
//        String url = "http://interface.bilibili.com/playurl?appkey=" + appkey + "&cid=" + cid + "&sign=" + sign_this;
        String url = "http://news-at.zhihu.com/api/25/start-image/";
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("qqqq",response.toString());
                //parseJSON(response);
                //va.notifyDataSetChanged();
                //pd.dismiss();
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
        StringRequest sr = new StringRequest(Request.Method.GET,url,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.i("qqqq",response.toString());
            }
        },new Response.ErrorListener(){

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
        mRequestQueue.add(sr);


    }

    /***
     * MD5加码 生成32位md5码
     */
    static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }
}