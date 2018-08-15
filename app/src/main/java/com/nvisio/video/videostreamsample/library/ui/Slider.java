package com.nvisio.video.videostreamsample.library.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.nvisio.video.videostreamsample.R;
import com.nvisio.video.videostreamsample.library.listener.OnSlideChangeListener;
import com.nvisio.video.videostreamsample.library.model.Slide;
import com.nvisio.video.videostreamsample.library.ui.adapter.SliderAdapter;
import com.nvisio.video.videostreamsample.library.ui.customUI.LooperWrapViewPager;

import java.util.List;
import java.util.Random;

public class Slider extends FrameLayout implements ViewPager.OnPageChangeListener{
    public static final String TAG = "SLIDER";

    private LooperWrapViewPager viewPager;
    private AdapterView.OnItemClickListener itemClickListener;

    //Custom attributes
    private boolean mustLoopSlides;
    private int slideShowInterval = 5000;
    private Handler handler = new Handler();
    private int slideCount;
    private int currentPageNumber;
    //shakil
    private OnSlideChangeListener onSlideChangeListener;
    private Runnable finalRun;

    public Slider(@NonNull Context context) {
        super(context);
    }

    public Slider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseCustomAttributes(attrs);
    }

    public Slider(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseCustomAttributes(attrs);
    }

    private void parseCustomAttributes(AttributeSet attributeSet) {
        try {
            if (attributeSet != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.BannerSlider);
                try {
                    mustLoopSlides = typedArray.getBoolean(R.styleable.BannerSlider_loopSlides, false);
                    int slideShowIntervalSecond = typedArray.getInt(R.styleable.BannerSlider_intervalSecond, 5);
                    slideShowInterval = slideShowIntervalSecond * 1000;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    typedArray.recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSlides(List<Slide> slideList) {
        if (slideList == null || slideList.size() == 0)
            return;

        viewPager = new LooperWrapViewPager(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewPager.setId(View.generateViewId());
        } else {
            int id = Math.abs(new Random().nextInt((5000 - 1000) + 1) + 1000);
            viewPager.setId(id);
        }
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewPager.addOnPageChangeListener(Slider.this);
        addView(viewPager);
        SliderAdapter adapter = new SliderAdapter(getContext(), slideList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(adapterView, view, i, l);
            }
        });
        viewPager.setAdapter(adapter);
        slideCount = slideList.size();
        /*viewPager.setCurrentItem(slideCount - 1);
        int s = slideCount-1;
        Log.d("sc>>","count: "+slideCount+" set: "+s);
        if (slideCount > 1)
            setupTimer();*/

        //shakil
        viewPager.setCurrentItem(0);
        if (slideCount>1){
            setupTimer();
        }

    }

    // shakil
    public void pauseSlide(){
        mustLoopSlides = false;
    }

    // shakil
    public void resumeSlide(){
        mustLoopSlides = true;
        setupTimer();
    }

    //shakil
    public void backFromExpand(int currentPage){
        viewPager.setCurrentItem(currentPage);
        setupTimer();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("sc>>","position: "+position);
        currentPageNumber = position;
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                handler.removeCallbacksAndMessages(null);
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                    setupTimer();
                break;
        }
    }

    private void setupTimer() {
        try {
            if (mustLoopSlides) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //viewPager.setCurrentItem(currentPageNumber - 1, true);
                            //shakil

                            if (mustLoopSlides){
                                //shakil
                                Log.d("scc>>","Before current: "+currentPageNumber+"\n");
                                if (currentPageNumber <slideCount){
                                    currentPageNumber += 1;
                                    Log.d("scc>>","current: "+currentPageNumber);
                                }
                                else{
                                    currentPageNumber = 1;
                                    Log.d("scc>>","current: "+currentPageNumber);
                                }
                                viewPager.setCurrentItem(currentPageNumber-1, true);
                           /*     if (currentPageNumber < slideCount)
                                    currentPageNumber += 1;
                                else
                                    currentPageNumber = 1;
                                Log.d("sc>>","currentPage after: "+currentPageNumber+"\n");
                                viewPager.setCurrentItem(currentPageNumber-1, true);*/
                                handler.removeCallbacksAndMessages(null);
                                handler.postDelayed(this, slideShowInterval);
                                onSlideChangeListener.onSlideChange(currentPageNumber);


                            }
                            else{
                                handler.removeCallbacksAndMessages(null);
                                onSlideChangeListener.onSlideChange(currentPageNumber);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, slideShowInterval);

            }
            else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // setters
    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //shakil
    public void slideChange(final OnSlideChangeListener onSlideChangeListener){
        this.onSlideChangeListener = onSlideChangeListener;
    }

    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }*/
}
