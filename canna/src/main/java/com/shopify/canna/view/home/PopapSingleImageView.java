package com.shopify.canna.view.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopify.canna.R;
import com.shopify.canna.view.base.BannerAdapter;

import java.util.ArrayList;

public class PopapSingleImageView extends AppCompatActivity {
    ViewPager viewPager;
    TextView indicator;
    ArrayList<String> stringArrayList;
    ImageView cancel_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popap_single_image_view);



    /*    DisplayMetrics displayMetrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;

        getWindow().setLayout(width,height);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params =getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        int x = getIntent().getIntExtra("x",0);
        int y = getIntent().getIntExtra("y",0);
        params.x=x;
        params.y=y;
        getWindow().setAttributes(params);
*/


        viewPager=findViewById(R.id.viewPager);
        indicator=findViewById(R.id.indicator);
        cancel_action=findViewById(R.id.cancel_action);

        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        stringArrayList = getIntent().getStringArrayListExtra("data");

        viewPager.setAdapter(new BannerAdapter(this, stringArrayList));

        indicator.setText(1+"/"+stringArrayList.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                TranslateAnimation animObj= new TranslateAnimation(0,0, 0, 0);
                animObj.setDuration(500);
                indicator.startAnimation(animObj);
                indicator.setText(position+1+"/"+stringArrayList.size());
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }
}