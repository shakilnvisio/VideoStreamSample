package com.nvisio.video.videostreamsample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.model.news.Article;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.RecyclerViewHolders> {
    private final Context context;
    private List<Article> Items;
    private ReadMoreListener readMoreListener;

    public interface ReadMoreListener {
        void readMoreClicked(int p);
    }

    public void setOnClicked(final ReadMoreListener readMoreListener) {
        this.readMoreListener = readMoreListener;

    }

    public NewsAdapter(Context context, List<Article> items) {
        this.Items = items;
        this.context = context;
    }

    public Article getItem(int position) {
        return Items.get(position);
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        //View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_single_row, null);
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_single_row_tow, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        Article itemDeals = Items.get(position);
        Glide.with(context)
                .load(itemDeals.getUrlToImage())
                .into(holder.newsImage);
        holder.headline.setText(itemDeals.getTitle());
        holder.source.setText(itemDeals.getSource().getName());
        holder.publishedText.setText(itemDeals.getPublishedAt());

    }

    @Override
    public int getItemCount() {
        return Items.size();
    }


    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView newsImage;
        private TextView headline;
        //private RelativeLayout readmore;
        private TextView source;
        private TextView publishedText;


        public RecyclerViewHolders(View itemView) {
            super(itemView);
//            newsImage = itemView.findViewById(R.id.newsImage);
//            headline = itemView.findViewById(R.id.headline);
//            source = itemView.findViewById(R.id.source);
//            readmore = itemView.findViewById(R.id.readMoreRec);
//            readmore.setOnClickListener(this);

            newsImage = itemView.findViewById(R.id.newsThumbnail);
            headline = itemView.findViewById(R.id.headlineText);
            source = itemView.findViewById(R.id.sourceText);
            publishedText = itemView.findViewById(R.id.publishedText);
        }

        @Override
        public void onClick(View view) {
            readMoreListener.readMoreClicked(getAdapterPosition());
        }
    }
}
