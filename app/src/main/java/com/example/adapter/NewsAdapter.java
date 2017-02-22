//package com.example.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.example.bean.BeforeNews;
//import com.example.bean.Fruit;
//
//import java.util.List;
//
//import example.com.rxjavatest.R;
//
///**
// * Created by wumingjun1 on 2017/2/16.
// */
//
//public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
//    private Context mContext;
//    private List<BeforeNews> mNewsList;
//
//
//    static  class ViewHolder extends RecyclerView.ViewHolder{
//        CardView cardView;
//        ImageView fruitImage;
//        TextView fruitName;
//
//        public ViewHolder(View view){
//            super(view);
//            cardView = (CardView) view;
//            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
//            fruitName = (TextView) view.findViewById(R.id.fruit_name);
//        }
//    }
//
//    public NewsAdapter(List<BeforeNews> newsList){
//        mNewsList = newsList;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (mContext == null){
//            mContext = parent.getContext();
//        }
//        View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item,parent,false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
//        BeforeNews news = mNewsList.get(position);
//        holder.fruitName.setText(news.getName());
//        Glide.with(mContext).load(news.getImageId()).into(holder.fruitImage);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mFruitList.size();
//    }
//}
