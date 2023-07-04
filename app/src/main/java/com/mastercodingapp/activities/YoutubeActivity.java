package com.mastercodingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;
import com.mastercodingapp.R;
import com.mastercodingapp.adapters.ViewPagerAdapter;
import com.mastercodingapp.model.OurYtModel;
import com.mastercodingapp.rest.ApiClient;
import com.mastercodingapp.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YoutubeActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.tollbary);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tablayout);


        // Tab Layout
        tabLayout.setupWithViewPager(viewPager);

        // toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Youtube Videos");
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //getting Youtube data
        getYoutubeData();


    }

    private void getYoutubeData() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<OurYtModel> call  = apiInterface.getYoutubeDetailsFromServer();
        call.enqueue(new Callback<OurYtModel>() {
            @Override
            public void onResponse(Call<OurYtModel> call, Response<OurYtModel> response) {
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                        response.body(),
                        YoutubeActivity.this);
                viewPager.setAdapter(viewPagerAdapter);

            }

            @Override
            public void onFailure(Call<OurYtModel> call, Throwable t) {
                Toast.makeText(YoutubeActivity.this, "Sorry!!!", Toast.LENGTH_SHORT).show();
            }
        });








    }

}



