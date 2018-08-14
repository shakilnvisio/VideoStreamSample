package com.nvisio.video.videostreamsample.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.library.model.Slide;
import com.nvisio.video.videostreamsample.library.ui.Slider;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class AdvertiseDemoActivity extends AppCompatActivity{

    private SlidingUpPanelLayout mLayout;
    private Slider slider;
    private String TAG = "slide>>";
    private boolean isCollapsed= false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertise_demo);
        init();
        SlidingUpPanel();
        AddDataToSlider();
    }

    private void init(){
        mLayout = findViewById(R.id.sliding_layout);
        slider = findViewById(R.id.slide);
    }

    private void SlidingUpPanel(){
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
           /*     if (isCollapsed && slideOffset>0.9){
                    Log.i("Tag>>", "offset > " + slideOffset);
                    YoYo.with(Techniques.FadeOutUp)
                            .duration(100)
                            .playOn(slider);
                }
                else if (isCollapsed && slideOffset<0.1){
                    Log.i("Tag>>", "offset < " + slideOffset);
                    YoYo.with(Techniques.FadeIn)
                            .duration(500)
                            .playOn(slider);
                }
                else{}*/
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
               if (newState.toString().equals("EXPANDED")){
                    slider.setVisibility(View.GONE);
                    slider.pauseSlide();
                    Log.i(TAG, "Visibility GONE");
                }
                else{
                    slider.setVisibility(View.VISIBLE);
                    slider.resumeSlide();
                    Log.i(TAG, "Visibility VISIBLE");
                }
/*
               if (newState.toString().equals("EXPANDED")){
                   isCollapsed = false;
               }
               else{
                   isCollapsed = true;
               }*/
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }
    private void AddDataToSlider(){
        //create list of slides
        List<Slide> slideList = new ArrayList<>();
        slideList.add(new Slide(0,"http://cssslider.com/sliders/demo-20/data1/images/picjumbo.com_img_4635.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        slideList.add(new Slide(1,"http://cssslider.com/sliders/demo-12/data1/images/picjumbo.com_hnck1995.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        slideList.add(new Slide(2,"http://cssslider.com/sliders/demo-19/data1/images/picjumbo.com_hnck1588.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        slideList.add(new Slide(3,"http://wowslider.com/sliders/demo-18/data1/images/shanghai.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));

        //handle slider click listener
        slider.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //do what you want
            }
        });
        //add slides to slider
        slider.addSlides(slideList);
    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
            startActivity(new Intent(AdvertiseDemoActivity.this,NavigationActivity.class));
            finish();
        }
    }

    public void Pause(View view) {
        slider.pauseSlide();

    }

}
