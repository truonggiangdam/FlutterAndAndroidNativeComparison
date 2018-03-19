package com.giangdam.zalofeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by cpu11326-local on 16/03/2018.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
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
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        FeedItem data = mListData.get(position);

        bindTop(data, holder.imageAvatar, holder.textUserName, holder.textPostTime);
        bindBottom(data, holder.textTotalLike, holder.textTotalComment);

        if(holder instanceof FeedLinkViewHolder) {
            bindFeedLink((FeedLinkViewHolder)holder, data);
        } else if (holder instanceof FeedPhotoViewHolder) {
            bindFeedPhoto((FeedPhotoViewHolder)holder, data);
        } else {
            bindFeedText((FeedTextViewHolder)holder, data);
        }
    }

    private void bindFeedText(FeedTextViewHolder holder, FeedItem data) {
        holder.textViewContent.setText(data.text);
    }

    private void bindFeedPhoto(FeedPhotoViewHolder holder, FeedItem data) {
        holder.textViewContent.setText(data.imageText);
        ImageHelper.displayImage(data.imageUrl, holder.imageViewContent, false);
    }

    private void bindFeedLink(final FeedLinkViewHolder holder, final FeedItem data) {

        //set placeholder
        holder.imageViewThumbnail.setImageResource(R.drawable.place_holder);

        String url = FeedUtils.buildUri(data.link);
        ApiHelper.getJsonData(url, new ApiHelper.ApiCallback() {
            @Override
            public void getApiDone(String result) {
                JSONObject jsonObject = FeedUtils.parseJson(result);
                if(jsonObject != null) {
                    String thumbnailUrl = FeedUtils.getThumbnailYoutubeVideo(jsonObject);
                    String title = FeedUtils.getTitleYoutubeVideo(jsonObject);

                    ImageHelper.displayImage(thumbnailUrl, holder.imageViewThumbnail, false);
                    holder.textViewTitle.setText(title);
                    holder.textViewDomain.setText("www.youtube.com");
                }

            }

            @Override
            public void getApiFailed() {
            }
        });

    }

    private void bindTop(FeedItem data, ImageView imageAvatar, TextView textUserName, TextView textPostTime) {
        ImageHelper.displayImage(data.avatarUrl, imageAvatar, true);
        textUserName.setText(data.userName);
        textPostTime.setText(data.postTime);
    }

    private void bindBottom(FeedItem data, TextView textTotalLike, TextView textTotalComment) {
        textTotalLike.setText(String.valueOf(data.totalLike));
        textTotalComment.setText(String.valueOf(data.totalComment));
    }



    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        ImageView imageAvatar;
        TextView textUserName;
        TextView textPostTime;
        TextView textTotalLike;
        TextView textTotalComment;

        public FeedViewHolder(View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
            textUserName = itemView.findViewById(R.id.textUserName);
            textPostTime = itemView.findViewById(R.id.textPostTime);
            textTotalLike = itemView.findViewById(R.id.textViewLike);
            textTotalComment = itemView.findViewById(R.id.textViewComment);
        }
    }


    public static class FeedLinkViewHolder extends FeedViewHolder {

        ImageView imageViewThumbnail;
        TextView textViewTitle;
        TextView textViewDomain;

        public FeedLinkViewHolder(View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewTitle = itemView.findViewById(R.id.textViewVideoTitle);
            textViewDomain = itemView.findViewById(R.id.textViewDomain);
        }
    }

    public static class FeedPhotoViewHolder extends FeedViewHolder {

        ImageView imageViewContent;
        TextView textViewContent;

        public FeedPhotoViewHolder(View itemView) {
            super(itemView);
            imageViewContent = itemView.findViewById(R.id.imageViewContent);
            textViewContent = itemView.findViewById(R.id.textViewContent);
        }
    }

    public static class FeedTextViewHolder extends FeedViewHolder {

        TextView textViewContent;

        public FeedTextViewHolder(View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
        }
    }
}
