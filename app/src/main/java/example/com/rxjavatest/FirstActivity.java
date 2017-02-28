package example.com.rxjavatest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.adapter.MenuItemAdapter;
import com.example.adapter.StoriesAdapter;
import com.example.bean.BeforeNewsBean;
import com.example.bean.LastNews;
import com.example.bean.Others;
import com.example.bean.Stories;
import com.example.bean.ThemeItemBean;
import com.example.bean.ThemesBean;
import com.example.bean.TopStories;
import com.example.utils.WmjUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    List<Others> others = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LastNews lastNewsBean = new LastNews();
    private Stories mStories;
    private String Date;
    private GridLayoutManager layoutManager;
    private List<Stories> StoriesList = new ArrayList<>();
    private StoriesAdapter adapter;
    private RequestQueue mRequestQueue;
    private int lastVisiableItem;
    private ListView mLvLeftMenu;
    private ThemesBean mThemesBean;
    private ThemeItemBean mThemeItemBean;
    private MenuItemAdapter menuAdapter;
    private List<MenuItemAdapter.LvMenuItem> mLvMenuItem = null;
    private String first_url = "http://news-at.zhihu.com/api/4/";
    private String second_url = "news/latest";
    private List<View> pageList;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private LinearLayout ll;
    private ImageView whiteCicle;
    private Handler mHandler = new Handler() {
        int c;
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 1:
                    menuAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    if(pageList.size()!=0) {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1 )% pageList.size());
                    }

                    mHandler.sendEmptyMessageDelayed(2,3000);
                    break;
            }
        }
    };
    private RecyclerView recyclerView;
    //viewpager 白色原点偏移量
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mRequestQueue = Volley.newRequestQueue(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerlayout);

        mLvLeftMenu = (ListView) findViewById(R.id.id_lv_left_menu);

//        NavigationView  naviVIew = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        mLvLeftMenu.addHeaderView(inflater.inflate(R.layout.nav_header, mLvLeftMenu, false));
        mLvMenuItem = new ArrayList<>();
        menuAdapter = new MenuItemAdapter(this, mLvMenuItem);
        mLvLeftMenu.setAdapter(menuAdapter);
        mLvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }

                if (i == 1) {
                    second_url = "news/latest";
                    toolbar.setTitle("首页");
                    refreshDate();
                } else {
                    i = i - 1;
                    second_url = mLvMenuItem.get(i).getId();
                    toolbar.setTitle(mThemesBean.getOthers().get(i - 1).getName());
                    refreshDate();

                }
                mDrawerLayout.closeDrawers();

            }
        });


//        naviVIew.setCheckedItem(R.id.nav_home);
//        naviVIew.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected( MenuItem item) {
//                        mDrawerLayout.closeDrawers();
//                        return true;
//                    }
//                });
//        naviVIew.setItemIconTintList(getColorStateList(R.color.colorPrimary));
//        naviVIew.setItemTextColor(getResources().getColorStateList(R.color.colorPrimary,null));

        recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StoriesAdapter(this, StoriesList);
        recyclerView.setAdapter(adapter);
        //// TODO: 2017/2/21  首页顶部viewpager
        initViewPager();


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private List<Integer> p = new ArrayList<Integer>();
            private List<String> s = new ArrayList<String>();

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiableItem = layoutManager.findLastVisibleItemPosition();


//                if (second_url=="news/latest") {
//                    int position = layoutManager.findFirstVisibleItemPosition();
//                    //Log.i("qqq","position"+position+"p"+p+"s"+s);
//
//                    if(p.size()>0&&position<p.get(p.size()-1)){
//
//                        s.remove(s.size()-1);
//                        p.remove(p.size()-1);
//                        toolbar.setTitle(s.get(s.size()-1));
//                        return ;
//                    }
//
//                    TextView tv = (TextView) layoutManager.findViewByPosition(position).findViewById(R.id.date);
//                    if (tv != null) {
//                        toolbar.setTitle(tv.getText());
//                        if(s.contains(""+tv.getText())==false) {
//                            s.add("" + tv.getText());
//                            p.add(position);
//                        }
//                    }
//                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && (lastVisiableItem + 1) == adapter.getItemCount()) {

                    //进行网络请求 请求下一页数据

                    if (second_url.equals("news/latest")) {
                        getBeforeDates();
                    } else {
                        //
                    }
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDate();
            }
        });
        refreshDate();


        JsonObjectRequest themeJson = new JsonObjectRequest(Request.Method.GET,
                "http://news-at.zhihu.com/api/4/themes"
                , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                mThemesBean = gson.fromJson(String.valueOf(response), ThemesBean.class);
                others.addAll(mThemesBean.getOthers());
                for (int i = 0; i < others.size(); i++) {
                    MenuItemAdapter.LvMenuItem tmp =
                            new MenuItemAdapter.LvMenuItem(others.get(i).getName());
                    tmp.setId(others.get(i).getId());
                    mLvMenuItem.add(tmp);
                }

//                Handler handler = new Handler(getMainLooper()){
//                    @Override
//                    public void handleMessage(Message msg) {
//                        ((MenuItemAdapter)mLvLeftMenu.getAdapter()).notifyDataSetChanged();
//                    }
//                };
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
                mHandler.sendEmptyMessage(1);

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
        mRequestQueue.add(themeJson);


    }

    private void initViewPager() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.pageview_top, null);
        viewPager = (ViewPager) view.
                findViewById(R.id.viewpager);
        ll = (LinearLayout) view.findViewById(R.id.LLcircles);

        pageList = new ArrayList<>();

        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pageList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pageList.get(position));
                return pageList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pageList.get(position));
            }
        };
        viewPager.setAdapter(pagerAdapter);
        adapter.addHeaderView(view);

        mHandler.sendEmptyMessage(2);

    }

    private void getItemTheme() {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                first_url + "theme/" + second_url
                , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                mThemeItemBean = gson.fromJson(String.valueOf(response), ThemeItemBean.class);
                StoriesList.clear();
                List<Stories> s = mThemeItemBean.getStories();
                for (int i = 0; i < s.size(); i++) {
                    if (s.get(i).getImages() != null) {
                        s.get(i).setDateType(1);
                    } else {
                        s.get(i).setDateType(2);
                    }
                }
                StoriesList.addAll(s);
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
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

    private void getBeforeDates() {


        String url = "http://news-at.zhihu.com/api/4/news/before/" + Date;
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                url
                , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                BeforeNewsBean b = gson.fromJson(String.valueOf(response), BeforeNewsBean.class);

                List<Stories> stories = b.getStories();
                for (int i = 0; i < stories.size(); i++) {
                    if (stories.get(i).getImages() != null) {
                        stories.get(i).setDateType(1);
                    } else {
                        stories.get(i).setDateType(2);
                    }
                }

                Calendar c = Calendar.getInstance();
                c.set(Integer.valueOf(Date.substring(0, 4)),
                        Integer.valueOf(Date.substring(4, 6)),
                        Integer.valueOf(Date.substring(6)) - 2);
                StringBuilder sb = new StringBuilder();
                sb.append(" ").append(c.get(Calendar.MONTH)).append("月").
                        append(c.get(Calendar.DATE) + 1)
                        .append("日").append(" 星期");
                String s = null;
                switch (c.get(Calendar.DAY_OF_WEEK)) {
                    case 1:
                        s = "一";
                        break;
                    case 2:
                        s = "二";
                        break;
                    case 3:
                        s = "三";
                        break;
                    case 4:
                        s = "四";
                        break;
                    case 5:
                        s = "五";
                        break;
                    case 6:
                        s = "六";
                        break;
                    case 7:
                        s = "日";
                        break;
                }
                sb.append(s);
                Stories tmp = new Stories();
                tmp.setDateType(3);
                tmp.setTitle(sb.toString());
                StoriesList.add(tmp);
                StoriesList.addAll(stories);
//                for(int i=0;i<stories.size();i++){
//                    Stories tmp2 = new Stories();
//                    StoriesList.add(tmp);
//                }
                adapter.notifyDataSetChanged();
                int d = Integer.valueOf(Date) - 1;
                Date = "" + d;

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

    private void initDates() {
        second_url = "news/latest";
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                first_url + second_url
                , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                lastNewsBean = gson.fromJson(String.valueOf(response), LastNews.class);

                StoriesList.clear();
                Date = lastNewsBean.getDate();
                List<Stories> stories = lastNewsBean.getStories();
                for (int i = 0; i < stories.size(); i++) {
                    if (stories.get(i).getImages() != null) {
                        stories.get(i).setDateType(1);
                    } else {
                        stories.get(i).setDateType(2);
                    }
                }
                Stories s = new Stories();
                s.setTitle(" 今日新闻");
                s.setDateType(3);

                List<TopStories> topStories = lastNewsBean.getTop_stories();
                pageList.clear();
                ll.removeAllViews();
                for (int i = 0; i < topStories.size(); i++) {

                    View view = getLayoutInflater().inflate(R.layout.viewpager_item, null);
                    ImageView iv = (ImageView) view.findViewById(R.id.img);
                    Glide.with(FirstActivity.this).load(topStories.get(i).getImage()).
                            into(iv);
                    ImageView circle = new ImageView(FirstActivity.this);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            WmjUtils.dp2px(getApplicationContext(), 10f)
                            , WmjUtils.dp2px(getApplicationContext(), 10f));
                    circle.setLayoutParams(params);
                    circle.setPadding(WmjUtils.dp2px(getApplicationContext(), 2f),
                            WmjUtils.dp2px(getApplicationContext(), 2f),
                            WmjUtils.dp2px(getApplicationContext(), 2f),
                            WmjUtils.dp2px(getApplicationContext(), 2f));
                    circle.setImageDrawable(getResources().
                            getDrawable(R.drawable.ic_circle_grey, null));
                    ll.addView(circle);
                    pageList.add(view);
                }
                pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
                offset = 0;

                //oncreate 中获取不到view的宽高
                ll.post(new Runnable() {
                    @Override
                    public void run() {
                        if (whiteCicle==null) {
                            whiteCicle = new ImageView(FirstActivity.this);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    WmjUtils.dp2px(getApplicationContext(), 10f)
                                    , WmjUtils.dp2px(getApplicationContext(), 10f));
                            params.leftMargin = ll.getLeft();
                            params.topMargin = ll.getTop();
                            whiteCicle.setLayoutParams(params);

                            whiteCicle.setPadding(WmjUtils.dp2px(getApplicationContext(), 2f),
                                    WmjUtils.dp2px(getApplicationContext(), 2f),
                                    WmjUtils.dp2px(getApplicationContext(), 2f),
                                    WmjUtils.dp2px(getApplicationContext(), 2f));
                            whiteCicle.setImageDrawable(getResources().
                                    getDrawable(R.drawable.ic_circle_white, null));
                            ((RelativeLayout) ll.getParent()).addView(whiteCicle);
                            final int l = ll.getLeft();
                            final int t = ll.getTop();
                            final int r = l+WmjUtils.dp2px(getApplicationContext(),10);
                            final int b = t+WmjUtils.dp2px(getApplicationContext(),10);
                            whiteCicle.layout(l+ offset,
                                    t,
                                    r+ offset,
                                    b);
                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                int  x=0;
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                    //// TODO: 2017/2/21

//                                    Log.i("qqq", "position = " + position
//                                          + " positionOffset = " + positionOffset
//                                           + " positionOffsetPixels = " + positionOffsetPixels);
                                    offset = (int) (WmjUtils.dp2px(getApplicationContext(), 10) * (position + positionOffset));
                                    //Log.i("qqq","x1 = "+l+ offset);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                            WmjUtils.dp2px(getApplicationContext(), 10f)
                                            , WmjUtils.dp2px(getApplicationContext(), 10f));
                                    params.leftMargin = ll.getLeft()+offset;
                                    params.topMargin = ll.getTop();
                                    whiteCicle.setLayoutParams(params);
                                    whiteCicle.layout(l + offset,
                                                t,
                                                r + offset,
                                                b);
                                    //Log.i("qqq","x2 = "+x);
                                }

                                @Override
                                public void onPageSelected(int position) {
                                    whiteCicle.layout(l+offset,
                                            t,
                                            r+offset,
                                            b);
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                    whiteCicle.layout(l+offset,
                                            t,
                                            r+offset,
                                            b);
                                }
                            });
                        }
                    }
                });
                pagerAdapter.notifyDataSetChanged();
                StoriesList.add(s);
                StoriesList.addAll(stories);
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
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

    private void refreshDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try{
//                    Thread.sleep(2000);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(0);
                        //mSwipeRefreshLayout.setRefreshing(false);
                        if (second_url.equals("news/latest")) {
                            initDates();
                        } else {
                            getItemTheme();
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.message:
                Toast.makeText(this, "message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.changeMode:
                Toast.makeText(this, "changeMOde", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }



}
