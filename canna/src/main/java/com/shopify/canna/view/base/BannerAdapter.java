package com.shopify.canna.view.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.shopify.canna.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private Context context;
    private List<String> banners;


    public BannerAdapter(Context context, List<String> banners ){
        this.context = context;
        this.banners = banners;

    }

    @Override
    public int getCount() {
        return banners.size();
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String image = banners.get(position);

        View view = inflater.inflate(R.layout.item_slider, null);

        ImageView imgView = (ImageView) view.findViewById(R.id.bannerImage);
        ImageView loadimage = (ImageView) view.findViewById(R.id.loadimage);
        loadimage.setVisibility(View.VISIBLE);
        imgView.setVisibility(View.GONE);

        //(cache Image run in banner )
        //new DiskLruImageCache(context, Utils_Cache.CACHE_FILEPATH_BANNER,Utils_Cache.CACHE_FILE_SIZE, Bitmap.CompressFormat.PNG,100);
        //imgView.setImageBitmap(DiskLruImageCache.containsKey(Utils_Cache.BANNER_PREFIX+bannerVO.getBannerId()) ? DiskLruImageCache.getBitmap(Utils_Cache.BANNER_PREFIX+bannerVO.getBannerId()) :null);

        Picasso.with(context)
                .load(image)
                .into(imgView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imgView.setVisibility(View.VISIBLE);
                        loadimage.setVisibility(View.GONE);
                        //do smth when picture is loaded successfully
                    }
                    @Override
                    public void onError() {
                        imgView.setVisibility(View.GONE);
                        loadimage.setVisibility(View.VISIBLE);
                    }
                });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        /*ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);*/
        container.removeView((View) object);
    }


}
