package me.onionpie.pandorabox.Widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.Helper.ApiLevelHelper;
import me.onionpie.pandorabox.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTopBarInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopBarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    @Bind(R.id.back)
    ImageButton mBack;
    @Bind(R.id.navi_bar_title)
    TextView mNaviBarTitle;
    @Bind(R.id.function_button)
    Button mFunctionButton;
    @Bind(R.id.image_function_button)
    ImageButton mImageFunctionBtn;
    @Bind(R.id.navi_top_bar)
    RelativeLayout mNaviTopBar;

    // TODO: Rename and change types of parameters
    private String mTitle ="";
    private String mRightTitle ="";
    /**
     * 1 文本 2 图片
     */
    private int mImageResId = -1;
    private OnTopBarInteractionListener mListener;
    private OnBackListener mBackListener;

    public TopBarFragment() {
        // Required empty public constructor
    }

    /**
     * @param title Parameter 1.
     * @param rightTitle Parameter 2.
     * @return A new instance of fragment TopBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopBarFragment newInstance(String title, String rightTitle) {
        TopBarFragment fragment = new TopBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, rightTitle);
        fragment.setArguments(args);
        return fragment;
    }
    public static TopBarFragment newInstance(String title, int resId) {
        TopBarFragment fragment = new TopBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putInt(ARG_PARAM3, resId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_PARAM1);
            mRightTitle = getArguments().getString(ARG_PARAM2);
            mImageResId = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topbar, container, false);
        ButterKnife.bind(this, view);
        mNaviBarTitle.setText(mTitle);
        initView();
        return view;
    }
    public void initView(){
        if (mRightTitle != null){
            mFunctionButton.setVisibility(View.VISIBLE);
            mFunctionButton.setText(mRightTitle);
            mImageFunctionBtn.setVisibility(View.GONE);
        }else if (mImageResId !=-1){
            mImageFunctionBtn.setVisibility(View.VISIBLE);
            mImageFunctionBtn.setImageDrawable(getResources().getDrawable(mImageResId));
            mFunctionButton.setVisibility(View.GONE);
        }

    }
    @OnClick(R.id.back)
    public void onClickBack(){
        ViewCompat.animate(mBack)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(100)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }
                    @SuppressLint("NewApi")
                    @Override
                    public void onAnimationEnd(View view) {
                        if (getActivity().isFinishing() ||
                                (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1)
                                        && getActivity().isDestroyed())) {
                            return;
                        }
                        if (mBackListener != null){
                            mBackListener.onBackClick();
                        }
                        else {
                            getActivity().onBackPressed();
                        }
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .start();

    }

    @OnClick(R.id.function_button)
    public void onClickFunction(){
        if (mListener != null) {
            mListener.onTopBarFunctionClicked();
        }
    }
    @OnClick(R.id.image_function_button)
    public void onClickImageFunction(){
        if (mListener != null) {
            mListener.onTopBarFunctionClicked();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTopBarInteractionListener && mListener == null) {
            mListener = (OnTopBarInteractionListener) context;
        } else if (mListener == null){
            Log.d("top_bar_listener","top bar listener is null");
        }
        else {
            Log.d("top_bar_listener","top bar is setted");
        }
    }

    public TopBarFragment setTopBarInteractionListener(OnTopBarInteractionListener listener){
        mListener = listener;
        return this;
    }

    public TopBarFragment setBackListener(OnBackListener mBackListener){
        this.mBackListener = mBackListener;
        return this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mBackListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface OnTopBarInteractionListener {
        // TODO: Update argument type and name
        void onTopBarFunctionClicked();
    }

    public interface OnBackListener{
        void onBackClick();
    }
}
