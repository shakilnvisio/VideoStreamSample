package com.nvisio.video.videostreamsample.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.adapter.DemoAdapter;
import com.nvisio.video.videostreamsample.library.listener.OnSlideChangeListener;
import com.nvisio.video.videostreamsample.library.model.Slide;
import com.nvisio.video.videostreamsample.library.ui.Slider;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class AdvertiseDemoActivity extends AppCompatActivity implements OnSlideChangeListener{

    private SlidingUpPanelLayout mLayout;
    private DiscreteScrollView discreteScrollView;
    private Slider slider;
    private String TAG = "slide>>";
    private boolean isCollapsed= false;
    List<Slide> slideList;
    private TextView changeBg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.advertise_demo);
        init();
        SlidingUpPanel();
        AddDataToSlider();
        setupDiscreteScrollView();
    }

    private void init(){
        mLayout = findViewById(R.id.sliding_layout);
        slider = findViewById(R.id.slide);
        discreteScrollView = findViewById(R.id.picker);
        changeBg = findViewById(R.id.background);
    }

    private void SlidingUpPanel(){
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (!isCollapsed){
                    // discrete is not showing
                    if (slideOffset > 0.1 && slideOffset < 0.2){
                        changeBg.setBackgroundColor(ContextCompat.getColor(AdvertiseDemoActivity.this,R.color.twenty));
                    }
                    else if (slideOffset > 0.2 && slideOffset < 0.3){
                        changeBg.setBackgroundColor(ContextCompat.getColor(AdvertiseDemoActivity.this,R.color.thirty));
                    }
                    else if (slideOffset > 0.3 && slideOffset < 0.4){
                        changeBg.setBackgroundColor(ContextCompat.getColor(AdvertiseDemoActivity.this,R.color.fifty));
                    }
                    else if (slideOffset > 0.4 && slideOffset < 0.5){
                        changeBg.setBackgroundColor(ContextCompat.getColor(AdvertiseDemoActivity.this,R.color.sixty));
                    }
                    else if (slideOffset > 0.5 && slideOffset <0.6){
                        changeBg.setBackgroundColor(ContextCompat.getColor(AdvertiseDemoActivity.this,R.color.seventy));
                    }
                    else if (slideOffset > 0.6 && slideOffset <0.7){
                        changeBg.setBackgroundColor(ContextCompat.getColor(AdvertiseDemoActivity.this,R.color.ninety));
                    }
                    else if (slideOffset > 0.8 && slideOffset < 0.9){
                        changeBg.setBackgroundColor(ContextCompat.getColor(AdvertiseDemoActivity.this,R.color.hundred));
                    }
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
               if (newState.toString().equals("EXPANDED")){
                    slider.setVisibility(View.GONE);
                    slider.pauseSlide();
                }
                else{
                    slider.setVisibility(View.VISIBLE);
                    slider.resumeSlide();
                   changeBg.setBackgroundColor(ContextCompat.getColor(AdvertiseDemoActivity.this,R.color.zero));
                    doWhenSlideUiIsCollapsed();
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    @Override
    public void onSlideChange(int selectedSlidePosition) {
        //Toast.makeText(this, ""+selectedSlidePosition, Toast.LENGTH_SHORT).show();
        discreteScrollView.smoothScrollToPosition(selectedSlidePosition-1);
        discreteScrollView.setItemTransitionTimeMillis(1000);
    }

    private void doWhenSlideUiIsCollapsed(){
        int currentItem = discreteScrollView.getCurrentItem();
        slider.backFromExpand(currentItem);
    }

    private void AddDataToSlider(){
        //create list of slides
        slideList = new ArrayList<>();
        slideList.add(new Slide(1,"http://cssslider.com/sliders/demo-20/data1/images/picjumbo.com_img_4635.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        slideList.add(new Slide(2,"http://cssslider.com/sliders/demo-12/data1/images/picjumbo.com_hnck1995.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        slideList.add(new Slide(3,"http://cssslider.com/sliders/demo-19/data1/images/picjumbo.com_hnck1588.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        slideList.add(new Slide(4,"http://wowslider.com/sliders/demo-18/data1/images/shanghai.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));

        //handle slider click listener
        slider.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //do what you want
                // when clicked, expand the view
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        //add slides to slider
        slider.addSlides(slideList);
        slider.slideChange(this);
    }

    private void setupDiscreteScrollView(){
        DemoAdapter demoAdapter = new DemoAdapter(slideList,this);
        discreteScrollView.setAdapter(demoAdapter);
        discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());
    }



    public void Pause(View view) {
        slider.pauseSlide();
    }
    public void Resume(View view) {
        slider.resumeSlide();
    }



    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
            slider.pauseSlide();
            startActivity(new Intent(AdvertiseDemoActivity.this,NavigationActivity.class));
            finish();
        }
    }
}
