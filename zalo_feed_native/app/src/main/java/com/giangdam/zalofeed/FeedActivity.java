package com.giangdam.zalofeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FeedAdapter mFeedAdapter;
    private List<FeedItem> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mRecyclerView = findViewById(R.id.recyclerView);

        // get Fake data
        mDataList = FeedUtils.generateFakeData(this);
        mFeedAdapter = new FeedAdapter(this, mDataList);

        //associate with recycler View
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFeedAdapter);
    }
}
