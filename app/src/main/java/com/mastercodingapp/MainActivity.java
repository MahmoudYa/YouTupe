package com.mastercodingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mastercodingapp.activities.YoutubeActivity;
import com.mastercodingapp.adapters.GridCategoryAdapter;
import com.mastercodingapp.adapters.NewsAdapter;
import com.mastercodingapp.model.HomepageModel;
import com.mastercodingapp.rest.ApiClient;
import com.mastercodingapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //youtube key
     // AIzaSyDYka_VNTEuFaz7InMqCHrjQPwhvgdrFzU

    FloatingActionButton fab;

    SliderLayout sliderLayout;

    GridView gridView;
    GridCategoryAdapter adapter;

    // we will load real news from our website
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    List<HomepageModel.News> news;

    // Categories
    List<HomepageModel.CategoryBotton> categoryBottons;


    // Variables for making infinite news feed
    int posts = 3;
    int page = 1;
    boolean isFromStart = true;

    // Progressbar
    ProgressBar progressBar;

    NestedScrollView nestedScrollView;

    // Swipe to refresh
    SwipeRefreshLayout swipeRefreshLayout;

    // We need to add toolbar to display the icon of youtube !


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitiateViews();

        AddImagesToSlider();

        //Initial conditions
        page = 1;
        isFromStart = true;


        // Getting Data
        getHomeData();

        // Getting new data on scrolling and swiping down
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())){
                    isFromStart = false;
                    progressBar.setVisibility(View.VISIBLE);
                    page++;
                    getHomeData();
                }
            }
        });










    }

    private void getHomeData() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("page",page+"");
        params.put("posts", posts+"");

        Call<HomepageModel> call = apiInterface.getHomepageApi(params);
        call.enqueue(new Callback<HomepageModel>() {


            @Override
            public void onResponse(Call<HomepageModel> call, Response<HomepageModel> response) {
                UpdateDataOnHomePage(response.body());

            }

            @Override
            public void onFailure(Call<HomepageModel> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private void UpdateDataOnHomePage(HomepageModel body) {

        // Adding Slider images from server
        // We are getting images now from body response and not from locally stored images (Drawables)

        // we are not getting images, since we are loading the images from localhost server
        //"image": "http://localhost/newsapp/wp-content/uploads/2020/12/bas.jpg"
        // So, our emulator will not get the images
        // we need to replace localhost with the emulator local host 10.0.2.2


        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

        if (isFromStart){
            news.clear();
            categoryBottons.clear();
        }

        for (int i= 0; i < body.getBanners().size() ; i++){
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            defaultSliderView.setRequestOption(new RequestOptions().centerCrop());
            defaultSliderView.image(body.getBanners().get(i).getImage());
            defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    // Handling Click event for slides

                }
            });

            sliderLayout.addSlider(defaultSliderView);

        }

        // Setting the slider options
        sliderLayout.startAutoCycle();
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Stack);
        sliderLayout.setDuration(3000);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);





        // Making way for getting news in correct order and conserving the page numbers
        int beforeNewsSize = news.size();


        for(int i = 0; i < body.getNews().size(); i++){
            news.add(body.getNews().get(i));
        }
        categoryBottons.addAll(body.getCategoryBotton());


        if (isFromStart){
            recyclerView.setAdapter(newsAdapter);
            gridView.setAdapter(adapter);
        }else{
            newsAdapter.notifyItemRangeInserted(beforeNewsSize, body.getNews().size());
        }




    }

    private void AddImagesToSlider() {
    }

    @SuppressLint("ResourceAsColor")
    private void InitiateViews() {


        // Fab
        fab = findViewById(R.id.floatings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, YoutubeActivity.class);
                startActivity(i);

            }
        });


        // Amazing Guys !
        // Help us making new courses and videos
        // Rate us 5 stars on UDemy
        // Subscirbe to our youtube channel: Master Coding
        // Download our app from playstroe




        categoryBottons = new ArrayList<>();


        sliderLayout = findViewById(R.id.slider);
        gridView = findViewById(R.id.grid_view);
        adapter = new GridCategoryAdapter(this,categoryBottons);

        // progressbar
        progressBar = findViewById(R.id.progressBar);

        // Nested scrollview
        nestedScrollView = findViewById(R.id.nested);

        // RecyclerView
        recyclerView = findViewById(R.id.recy_news);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        news = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, news);



        swipeRefreshLayout = findViewById(R.id.swipy);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeColors(R.color.orange,
                R.color.blue,
                R.color.green);


        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        sliderLayout.stopAutoCycle();
    }

    @Override
    public void onRefresh() {
        isFromStart = true;
        page = 1;
        getHomeData();
    }
}





// Let's add retrofit library
// https://square.github.io/retrofit/








// Amazing !
// every change in categories, posts, news images
// or any thing,
// You will get updated using this swipe to refresh!~
// Thanks


