package me.onionpie.pandorabox.UI.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.onionpie.pandorabox.PandoraApplication;
import me.onionpie.pandorabox.R;

/**
 * Created by Gstansen on 2016/4/13.
 */
public class WelcomeViewPagerAdapter extends PagerAdapter {


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_view_navi_item,container,false);

        ImageView imageView = (ImageView)view.findViewById(R.id.pic);
        switch (position){
            case 0:
                imageView.setBackgroundResource(R.color.blueviolet);
                break;
            case 1:
                imageView.setBackgroundResource(R.color.lightskyblue);
                break;
            case 2:
                imageView.setBackgroundResource(R.color.cardview_shadow_end_color);
                break;
            case 3:
                imageView.setBackgroundResource(R.color.blueviolet);
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof ImageView)
           container.removeView((ImageView)object);
    }

}
