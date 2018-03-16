package com.giangdam.zalofeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cpu11326-local on 16/03/2018.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<FeedItem> mListData;
    Context mContext;

    FeedAdapter(Context context, List<FeedItem> listData) {
        mContext = context;
        mListData = listData;
    }

    @Override
    public int getItemViewType(int position) {
        return mListData.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView;
        switch (viewType) {
            case FeedUtils.TYPE_FEED_LINK:
                itemView = inflater.inflate(R.layout.view_feed_link, parent, false);
                return new FeedLinkViewHolder(itemView);
            case FeedUtils.TYPE_FEED_PHOTO:
                itemView = inflater.inflate(R.layout.view_feed_photo, parent, false);
                return new FeedPhotoViewHolder(itemView);
            default:
                itemView = inflater.inflate(R.layout.view_feed_text, parent, false);
                return new FeedTextViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FeedItem data = mListData.get(position);

        if(holder instanceof FeedLinkViewHolder) {
            bindFeedLink((FeedLinkViewHolder)holder, data, position);
        } else if (holder instanceof FeedPhotoViewHolder) {
            bindFeedPhoto((FeedPhotoViewHolder)holder, data, position);
        } else {
            bindFeedText((FeedTextViewHolder)holder, data, position);
        }
    }

    private void bindFeedText(FeedTextViewHolder holder, FeedItem data, int position) {

    }

    private void bindFeedPhoto(FeedPhotoViewHolder holder, FeedItem data, int position) {

    }

    private void bindFeedLink(FeedLinkViewHolder holder, FeedItem data, int position) {
        Picasso.get().load(data.avatarUrl).into(holder.imageAvatar);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    private static class FeedLinkViewHolder extends RecyclerView.ViewHolder {

        ImageView imageAvatar;

        public FeedLinkViewHolder(View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
        }
    }

    private static class FeedPhotoViewHolder extends RecyclerView.ViewHolder {

        public FeedPhotoViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class FeedTextViewHolder extends RecyclerView.ViewHolder {

        public FeedTextViewHolder(View itemView) {
            super(itemView);
        }
    }
}
