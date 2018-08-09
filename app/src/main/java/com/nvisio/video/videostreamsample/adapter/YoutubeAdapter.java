package com.nvisio.video.videostreamsample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.model.youtube.Item;

import java.util.List;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.RecyclerViewHolders> {
    private final Context context;
    private List<Item> Items;
    private YoutubeListener youtubeListener;

    public interface YoutubeListener {
        void singleVideoClicked(String url);
    }

    public void setOnClicked(final YoutubeListener youtubeListener) {
        this.youtubeListener = youtubeListener;

    }

    public YoutubeAdapter(Context context, List<Item> items) {
        this.Items = items;
        this.context = context;
    }

    public Item getItem(int position) {
        return Items.get(position);
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_single_row, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        Item itemDeals = Items.get(position);
        Glide.with(context)
                .load(itemDeals.getSnippet().getThumbnails().getMedium().getUrl()).into(holder.thumbnail);
        holder.videoTitle.setText(itemDeals.getSnippet().getTitle());


    }

    @Override
    public int getItemCount() {
        return Items.size();
    }


    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        //get the view's references
        //private Textview textView
        private ImageView thumbnail;
        private LinearLayout container;
        private TextView videoTitle;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            //itemView.setOnClickedListener(this);
            //initialise the Views here and set the views where i want to have onClicked event option
            //i.e textView=(Textview)findViewById(R.id.text);
            //
            thumbnail = itemView.findViewById(R.id.thumbnail);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            container = itemView.findViewById(R.id.container);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("ada>>","click");
            Item data = Items.get(getAdapterPosition());
            //youtubeListener.singleVideoClicked(getAdapterPosition());
            String url = "https://www.youtube.com/watch?v="+data.getId();
            youtubeListener.singleVideoClicked(url);
            Log.d("ada>>","link: "+url);
        }
    }
}
