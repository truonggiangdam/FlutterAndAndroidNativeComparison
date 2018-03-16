package com.giangdam.zalofeed;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpu11326-local on 16/03/2018.
 */

public class FeedUtils {
    private static final String YOUTUBE_LINK = "https://www.youtube.com/watch?v=UNhBsAFSldU";
    private static final String AVATAR_URL = "https://smultron.files.wordpress.com/2008/08/heath-ledger-igen.jpg";
    private static final String IMAGE_URL = "https://vignette.wikia.nocookie.net/godfather/images/e/e5/Don_Michael_Corleone.jpg";
    private static final int TOTAL_LIKE = 10;
    private static final int TOTAL_COMMENT = 6;

    public static final int TYPE_FEED_LINK = 111;
    public static final int TYPE_FEED_PHOTO = 222;
    public static final int TYPE_FEED_TEXT = 333;

    public static List<FeedItem> generateFakeData(Context context) {
        List<FeedItem> list = new ArrayList<>();
        for(int i = 0; i< 100; i++) {
           list.add(
             i % 2 == 0 ? generateFeedLink(context)
                     : (i % 3 == 0 ? generateFeedPhoto(context) : generateFeedText(context))
           );
        }

        return list;
    }

    private static FeedItem generateFeedLink(Context context) {
        FeedItem item = generateFeedBase(context);
        item.link = YOUTUBE_LINK;
        item.type = TYPE_FEED_LINK;
        return item;
    }

    private static FeedItem generateFeedPhoto(Context context) {
        FeedItem item = generateFeedBase(context);
        item.imageText = context.getString(R.string.image_text);
        item.imageUrl = IMAGE_URL;
        item.type = TYPE_FEED_PHOTO;
        return item;
    }

    private static FeedItem generateFeedText(Context context) {
        FeedItem item = generateFeedBase(context);
        item.text = context.getString(R.string.text);
        item.type = TYPE_FEED_TEXT;
        return item;
    }

    private static FeedItem generateFeedBase(Context context){
        FeedItem item = new FeedItem();
        item.userName = context.getString(R.string.user_name);
        item.avatarUrl = AVATAR_URL;
        item.postTime = context.getString(R.string.post_time);
        item.totalLike = TOTAL_LIKE;
        item.totalComment = TOTAL_COMMENT;
        return item;
    }
}
