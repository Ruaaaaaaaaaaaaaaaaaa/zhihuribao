package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bean.Stories;

import java.util.List;

import example.com.rxjavatest.ContentActivity;
import example.com.rxjavatest.R;

/**
 * Created by wumingjun1 on 2017/2/15.
 */

public class StoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     *  1.  有图有文字
     *  2.  有图无文字
     *  3.  日期
     *  4.  主编图像
     *  5.  顶部viewpager
     */
    public static final int CONTENT = 1;
    public static final int CONTENT_NO_IMAGE=2;
    public static final int DATE = 3;
    public static final int EDITOR =4;
    public static final int PAGE = 5;

    private Context mContext;
    private List<Stories> mStoriesList;
    private View HEADER;

    static  class PagerViewHolder extends RecyclerView.ViewHolder{
        ViewPager viewPager;

        public PagerViewHolder(View view){
            super(view);
            //viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        }
    }

    static  class DateViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate;
        public DateViewHolder(View view){
            super(view);
            tvDate = (TextView) view.findViewById(R.id.date);
        }
    }
    static  class ContentViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView StoriesImage;
        TextView StoriesName;

        public ContentViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            StoriesImage = (ImageView) view.findViewById(R.id.news_image);
            StoriesName = (TextView) view.findViewById(R.id.news_name);
        }
    }

    static  class ContentViewNoImageHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView StoriesName;

        public ContentViewNoImageHolder(View view){
            super(view);
            cardView = (CardView) view;
            StoriesName = (TextView) view.findViewById(R.id.news_name);
        }
    }

    public StoriesAdapter(Context context,List<Stories> StoriesList){
        mContext = context;
        mStoriesList = StoriesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view;
        if(viewType == CONTENT){
            view = LayoutInflater.from(mContext).inflate(R.layout.news_item,parent,false);
            final ContentViewHolder  holder =  new ContentViewHolder(view);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition()-1;
                    Intent intent = new Intent(mContext, ContentActivity.class);
                    intent.putExtra("id", mStoriesList.get(position).getId());
                    mContext.startActivity(intent);
                }
            });
            return holder;
        }else if (viewType == CONTENT_NO_IMAGE){
            view = LayoutInflater.from(mContext).inflate(R.layout.news_item_no_img,parent,false);
            final ContentViewNoImageHolder holder = new ContentViewNoImageHolder(view);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Intent intent = new Intent(mContext, ContentActivity.class);
                    intent.putExtra("id", mStoriesList.get(position).getId());
                    mContext.startActivity(intent);
                }
            });
            return holder;
        }else if(viewType == DATE){
            view = LayoutInflater.from(mContext).inflate(R.layout.date_item,parent,false);
            return new DateViewHolder(view);
        }else if(viewType == PAGE){
            return new PagerViewHolder(HEADER);
        }else{
            return  null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder==null){
            return ;
        }
        if(HEADER!=null){
            position--;
        }
        if(holder instanceof ContentViewHolder){
            Stories Stories = mStoriesList.get(position);
            ((ContentViewHolder) holder).StoriesName.setText(Stories.getTitle());

            Glide.with(mContext).load(Stories.getImages().get(0)).
                    into( ((ContentViewHolder) holder).StoriesImage);
        }else if(holder instanceof DateViewHolder){
            ((DateViewHolder)holder).tvDate.setText(mStoriesList.get(position).getTitle());
        }else if(holder instanceof ContentViewNoImageHolder){
            ((ContentViewNoImageHolder)holder).StoriesName.setText(
                    mStoriesList.get(position).getTitle());
        }else if(holder instanceof PagerViewHolder){
            //((PagerViewHolder)holder).viewPager.
        }else{

        }

    }

    @Override
    public int getItemCount() {
        int count = mStoriesList.size();
        if(HEADER!=null){
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if(HEADER==null) {
            Stories s = mStoriesList.get(position);
            return s.getDateType();
        }else{
            if(position==0) {
                return PAGE;
            }else{
                Stories s = mStoriesList.get(position-1);
                return  s.getDateType();
            }
        }
    }


    public void addHeaderView(View headerView){
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(params);
        HEADER = headerView;
        notifyItemInserted(0);
    }
}
