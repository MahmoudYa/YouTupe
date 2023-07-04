package com.mastercodingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mastercodingapp.R;

public class YoutubeVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    TextView videoTitle, channelName, labelSimilar;
    RecyclerView recyclerView;
    YouTubePlayerView youTubePlayerView;

    String cid, vid, title,cname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video);

        cid = getIntent().getStringExtra("cid");
        vid = getIntent().getStringExtra("vid");
        title = getIntent().getStringExtra("title");
        cname = getIntent().getStringExtra("cname");

        InitViews();
        videoTitle.setText(title);
        channelName.setText(cname);
        labelSimilar.setText("More From " +cname);

        youTubePlayerView.initialize(getString(R.string.google_api_key), this);

    }

    private void InitViews() {
        videoTitle = findViewById(R.id.video_title);
        channelName = findViewById(R.id.channel_name);
        labelSimilar = findViewById(R.id.label_similar);
        recyclerView = findViewById(R.id.video_recyc);

        youTubePlayerView = findViewById(R.id.youtube_player);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(vid);
    }

    // Amazing !! We are kings !~
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}