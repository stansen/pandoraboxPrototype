package me.onionpie.pandorabox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.Animation.ViewPagerAnimation.CubeInTransformer;
import me.onionpie.pandorabox.Animation.ViewPagerAnimation.CubeOutTransformer;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.UI.Adapter.WelcomeViewPagerAdapter;
import me.relex.circleindicator.CircleIndicator;

public class WelcomeActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.indicator)
    CircleIndicator mIndicator;
    @Bind(R.id.in)
    Button mIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mViewPager.setPageTransformer(false, new CubeOutTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3)
                    mIn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(new WelcomeViewPagerAdapter());
        mIndicator.setViewPager(mViewPager);
    }

    @OnClick(R.id.in)
    public void in() {
        Intent in = new Intent(this, HomeActivity.class);
        startActivity(in);
    }

}
