package me.onionpie.pandorabox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nightonke.wowoviewpager.Color.ColorChangeType;
import com.nightonke.wowoviewpager.Eases.EaseType;
import com.nightonke.wowoviewpager.ViewAnimation;
import com.nightonke.wowoviewpager.WoWoBackgroundColorAnimation;
import com.nightonke.wowoviewpager.WoWoTranslationAnimation;
import com.nightonke.wowoviewpager.WoWoUtil;
import com.nightonke.wowoviewpager.WoWoViewPager;
import com.nightonke.wowoviewpager.WoWoViewPagerAdapter;

import butterknife.OnClick;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.AppManager;
import me.onionpie.pandorabox.Utils.CommonPreference;

public class WelcomeActivity extends AppCompatActivity {

    //    @Bind(R.id.view_pager)
//    ViewPager mViewPager;
//    @Bind(R.id.indicator)
//    CircleIndicator mIndicator;
//    @Bind(R.id.in)
//    Button mIn;
//    @Bind(R.id.animation_view_pager)
    WoWoViewPager mWoWoViewPager;
    //    @Bind(R.id.convenience)
//    TextView mConvenience;
//    @Bind(R.id.efficient)
//    TextView mEfficient;
//    @Bind(R.id.safe)
//    TextView mSafe;
//    @Bind(R.id.convenience_description)
//    TextView mConvenienceDescription;
//    @Bind(R.id.efficient_description)
//    TextView mEfficientDescription;
//    @Bind(R.id.safe_description)
//    TextView mSafeDescription;
//    @Bind(R.id.experience)
//    Button mExperience;
//    @Bind(R.id.sub_base)
//    RelativeLayout mSubBase;
    private WoWoViewPagerAdapter mWoWoViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        mWoWoViewPager = (WoWoViewPager) findViewById(R.id.animation_view_pager);
        initWowopager();
//        initView();
    }

    private void initWowopager() {
        mWoWoViewPagerAdapter = new WoWoViewPagerAdapter(getSupportFragmentManager());
        mWoWoViewPagerAdapter.setFragmentsNumber(4);
        mWoWoViewPagerAdapter.setColorRes(android.R.color.transparent);
//        mWoWoViewPagerAdapter.setColorsRes(new Integer[]{
//                R.color.light_blue,
//                R.color.delete_color,
//                R.color.background_bg
//        });
        mWoWoViewPager.setAdapter(mWoWoViewPagerAdapter);
    }

    private int screenW = 1;
    private int screenH = 1;
    private int circleR = 1;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        screenW = WoWoUtil.getScreenWidth(this);
        screenH = WoWoUtil.getScreenHeight(this);
        circleR = (int) Math.sqrt(screenW * screenW + screenH * screenH) + 10;
        RelativeLayout base = (RelativeLayout) findViewById(R.id.base_background);
        ViewGroup.LayoutParams layoutParams = base.getLayoutParams();

        layoutParams.height = circleR * 2;
        layoutParams.width = circleR * 2;
        base.setLayoutParams(layoutParams);

        RelativeLayout subBase = (RelativeLayout) findViewById(R.id.sub_base);
        layoutParams = subBase.getLayoutParams();
        layoutParams.height = screenH;
        layoutParams.width = screenW;
        subBase.setLayoutParams(layoutParams);

        setBase();
        circleTitle();
        descriptionAnimation();
        setOpenKey();
//        setPath();
    }

    private void setBase() {
        ViewAnimation animation = new ViewAnimation(findViewById(R.id.base_background));
        animation.addPageAnimaition(new WoWoBackgroundColorAnimation(
                0, 0, 1,
                ContextCompat.getColor(this, R.color.light_blue),
                ContextCompat.getColor(this, R.color.my_pink),
                ColorChangeType.RGB,
                EaseType.Linear,
                true
        ));
        animation.addPageAnimaition(new WoWoBackgroundColorAnimation(
                1, 0, 1,
                ContextCompat.getColor(this, R.color.my_pink),
                ContextCompat.getColor(this, R.color.kgps_text_color11),
                ColorChangeType.RGB,
                EaseType.Linear,
                true
        ));
        animation.addPageAnimaition(new WoWoBackgroundColorAnimation(
                1, 0, 1,
                ContextCompat.getColor(this, R.color.kgps_text_color11),
                ContextCompat.getColor(this, R.color.grey300),
                ColorChangeType.RGB,
                EaseType.Linear,
                true
        ));
        mWoWoViewPager.addAnimation(animation);
    }

    private void circleTitle() {

        ViewAnimation animation = new ViewAnimation(findViewById(R.id.convenience));
        animation.addPageAnimaition(new WoWoTranslationAnimation(
                0, 0f, 1f,
                findViewById(R.id.convenience).getTranslationX(),
                findViewById(R.id.convenience).getTranslationY(),
                -screenW / 2 + WoWoUtil.dp2px(80, this),
                -screenH / 2 + WoWoUtil.dp2px(80, this),
                EaseType.EaseInCubic,
                true));
        mWoWoViewPager.addAnimation(animation);
        ViewAnimation animation1 = new ViewAnimation(findViewById(R.id.efficient));
        animation1.addPageAnimaition(new WoWoTranslationAnimation(
                1, 0f, 1f,
                findViewById(R.id.efficient).getTranslationX(),
                findViewById(R.id.efficient).getTranslationY(),
                0,
                -screenH / 2 + WoWoUtil.dp2px(60, this),
                EaseType.EaseInCubic,
                true));
        mWoWoViewPager.addAnimation(animation1);
        ViewAnimation animation2 = new ViewAnimation(findViewById(R.id.safe));
        animation2.addPageAnimaition(new WoWoTranslationAnimation(
                2, 0f, 1f,
                findViewById(R.id.safe).getTranslationX(),
                findViewById(R.id.safe).getTranslationY(),
                screenW / 2 - WoWoUtil.dp2px(80, this),
                -screenH / 2 + WoWoUtil.dp2px(80, this),
                EaseType.EaseInCubic,
                true));
        mWoWoViewPager.addAnimation(animation2);
    }

    private void descriptionAnimation() {
        ViewAnimation viewAnimation = new ViewAnimation(findViewById(R.id.efficient_description));
        viewAnimation.addPageAnimaition(new WoWoTranslationAnimation(
                0, 0, 1,
                findViewById(R.id.efficient_description).getTranslationX(),
                findViewById(R.id.efficient_description).getTranslationY(),
                0,
                -screenH / 2 + WoWoUtil.dp2px(50, this),
                EaseType.EaseOutBack,
                false
        ));
        viewAnimation.addPageAnimaition(new WoWoTranslationAnimation(
                1, 0, 1,
                0,
                -screenH / 2 + WoWoUtil.dp2px(55, this),
                -screenW / 2 - findViewById(R.id.efficient_description).getWidth(),
                0,
                EaseType.EaseOutBack,
                false
        ));
        ViewAnimation animationConvinice = new ViewAnimation(findViewById(R.id.convenience_description));
        animationConvinice.addPageAnimaition(new WoWoTranslationAnimation(
                0, 0, 1,
                findViewById(R.id.convenience_description).getTranslationX(),
                findViewById(R.id.convenience_description).getTranslationY(),
                -screenW / 2 - findViewById(R.id.convenience_description).getWidth(),
                0,
                EaseType.EaseOutBack,
                false
        ));
        ViewAnimation viewAnimationsafe = new ViewAnimation(findViewById(R.id.safe_description));
        viewAnimationsafe.addPageAnimaition(new WoWoTranslationAnimation(
                1, 0, 1,
                findViewById(R.id.safe_description).getTranslationX(),
                findViewById(R.id.safe_description).getTranslationY(),
                0,
                -screenH / 2 + WoWoUtil.dp2px(50, this),
                EaseType.EaseOutBack,
                false
        ));
        viewAnimationsafe.addPageAnimaition(new WoWoTranslationAnimation(
                2, 0, 1,
                0,
                -screenH / 2 + WoWoUtil.dp2px(55, this),
                -screenW / 2 - findViewById(R.id.safe_description).getWidth(),
                0,
                EaseType.EaseOutBack,
                false
        ));
        mWoWoViewPager.addAnimation(viewAnimationsafe);
        mWoWoViewPager.addAnimation(animationConvinice);
        mWoWoViewPager.addAnimation(viewAnimation);
    }

    private void setOpenKey() {
        ImageView openkey = (ImageView) findViewById(R.id.open_key);
        ViewAnimation viewAnimation = new ViewAnimation(openkey);
        viewAnimation.addPageAnimaition(new WoWoTranslationAnimation(
                3, 0, 1,
                openkey.getTranslationX(),
                openkey.getTranslationY(),
                0,
                screenH - WoWoUtil.dp2px(150, this),
                EaseType.EaseOutBack, true
        ));
        mWoWoViewPager.addAnimation(viewAnimation);
    }
//    private void initView() {
//        mViewPager.setPageTransformer(false, new CubeOutTransformer());
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 3)
//                    mIn.setVisibility(View.VISIBLE);
//                else mIn.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        mViewPager.setAdapter(new WelcomeViewPagerAdapter());
//        mIndicator.setViewPager(mViewPager);
//    }

    public void saveFirstTimeOpenStatus() {
        CommonPreference.putBoolean(this, Constans.KEY_IS_APP_FIRST_TIME_OPEN, true);
        Intent in = new Intent(this, HomeActivity.class);
        startActivity(in);
        AppManager.getAppManager().finishActivity();
    }

}
