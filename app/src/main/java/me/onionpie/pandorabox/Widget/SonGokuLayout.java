package me.onionpie.pandorabox.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by jiudeng007 on 2015/12/31.
 */
public class SonGokuLayout extends LinearLayout implements View.OnClickListener {

    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int NODATA_ENABLE_CLICK = 5;
    public static final int NO_LOGIN = 6;
    private final Context mContext;
    private int mState;
    /**
     * 咩有数据布局
     */
    private RelativeLayout mNoDataContainerLL;
    /**
     * 请求加载布局
     */
    private RelativeLayout mLoadingDataContainerLL;
    /**
     * 无网络布局
     */
    private RelativeLayout mNoNetWorkContainerLL;
    private ImageView mEmptyBgIV;
    private TextView mEmptyBgDescriptionTV;
    private ProgressBar mLoadingProgressBar;
    private TextView mLoadingDescriptionTV;
    private ImageView mNetWorkErrorIV;
    private TextView mNetWorkErrorMessageTV;
    private OnClickListener mOnClickListener;
    private boolean mIsClickable = false;

    public SonGokuLayout(Context context) {
        super(context);
        mContext = context;
//        initView();
    }

    public SonGokuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
//        initView();
    }

    @Override
    public void onClick(View view) {

    }

//    public void initView() {
//        View view = View.inflate(mContext, R.layout.son_go_ku_layout, null);
//        mNoDataContainerLL = (RelativeLayout) view.findViewById(R.id.style1_container);
//        mNoDataContainerLL.setOnClickListener(this);
//        mLoadingDataContainerLL = (RelativeLayout) view.findViewById(R.id.style2_container);
//        mNoNetWorkContainerLL = (RelativeLayout) view.findViewById(R.id.style3_container);
//        mEmptyBgIV = (ImageView) view.findViewById(R.id.empty_bg);
//        mEmptyBgDescriptionTV = (TextView) view.findViewById(R.id.image_description);
//        mLoadingProgressBar = (ProgressBar) view.findViewById(R.id.loading_progress);
//        mLoadingDescriptionTV = (TextView) view.findViewById(R.id.description_content);
//        mNetWorkErrorIV = (ImageView) view.findViewById(R.id.netWorkErrorImage);
//        mNetWorkErrorMessageTV = (TextView) view.findViewById(R.id.netWordErrorMessage);
////        mLoadingDataContainerLL.setOnClickListener(this);
////        mNoDataContainerLL.setOnClickListener(this);
//        mNoNetWorkContainerLL.setOnClickListener(this);
//        setBackgroundColor(-1);
//        addView(view);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (mIsClickable) {
//            if (mOnClickListener !=null){
//                mOnClickListener.onClick(v);
//            }
//        }
//    }
//
//    public void setNoDataImage(int resId) {
//        try {
//            mEmptyBgIV.setImageResource(resId);
//        } catch (Exception e) {
//            Log.e("jd_no_such_res", e.toString());
//        }
//    }
//
//    public int getStateType() {
//        return mState;
//    }
//    public void setLayoutClickListener(OnClickListener onClickListener){
//        mOnClickListener = onClickListener;
//    }
//    public void setStateType(int type) {
//        mState = type;
//        switch (type) {
//            case NETWORK_ERROR:
//                dismissAll();
//                mNoNetWorkContainerLL.setVisibility(VISIBLE);
//                mIsClickable = true;
//                break;
//            case NETWORK_LOADING:
//                dismissAll();
//                mLoadingDataContainerLL.setVisibility(VISIBLE);
//                break;
//            case NODATA:
//                dismissAll();
//                mNoDataContainerLL.setVisibility(VISIBLE);
//                break;
//            case HIDE_LAYOUT:
//                dismissAll();
//                setVisibility(GONE);
//                break;
//            case NODATA_ENABLE_CLICK:
//                dismissAll();
//
//                break;
//            case NO_LOGIN:
//                dismissAll();
//                mNoDataContainerLL.setVisibility(VISIBLE);
//                mIsClickable = true;
//                break;
//        }
//    }
//
//    private void dismissAll() {
//        mNoNetWorkContainerLL.setVisibility(GONE);
//        mLoadingDataContainerLL.setVisibility(GONE);
//        mNoDataContainerLL.setVisibility(GONE);
//        mIsClickable = false;
//    }
//
//    @Override
//    public void setVisibility(int visibility) {
//        if (visibility == GONE) {
//            mState = HIDE_LAYOUT;
//        }
//        super.setVisibility(visibility);
//    }
}
