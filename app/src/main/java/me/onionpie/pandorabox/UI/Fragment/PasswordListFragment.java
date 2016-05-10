package me.onionpie.pandorabox.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.QueryBuilder;
import me.onionpie.greendao.DBHelper;
import me.onionpie.greendao.PasswordTextItem;
import me.onionpie.greendao.PasswordTextItemDao;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;
import me.onionpie.pandorabox.Model.SingleCharPasswordRuleModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Rx.Event.UpdatePasswordListEvent;
import me.onionpie.pandorabox.Rx.RxBus;
import me.onionpie.pandorabox.Temp.ItemData;
import me.onionpie.pandorabox.Temp.RecyclerViewItemArray;
import me.onionpie.pandorabox.UI.Adapter.PasswordRecyclerViewAdapter;
import me.onionpie.pandorabox.Utils.Sercurity;
import me.onionpie.pandorabox.Widget.SonGokuLayout;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PasswordListFragment extends BaseFragment {

    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    @Bind(R.id.son_gu_ku)
    ImageView mSonGokuLayout;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerViewItemArray mRecyclerViewItemArray = new RecyclerViewItemArray();
    private Subscription mUpdateSubscrition;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PasswordListFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUpdateSubscrition == null || mUpdateSubscrition.isUnsubscribed()) {
            mUpdateSubscrition = RxBus.getInstance().toObserverable().subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    if (o instanceof UpdatePasswordListEvent) {
                        UpdatePasswordListEvent updatePasswordListEvent = (UpdatePasswordListEvent) o;
                        if (updatePasswordListEvent.mIsAdd) {
//                            mPasswordTextInfoModels.add(updatePasswordListEvent.mPasswordTextInfoModel);
                            mPasswordTextInfoModels.add(0, updatePasswordListEvent.mPasswordTextInfoModel);
                            mRecyclerViewItemArray.addBeforFirst(1, new ItemData<>(1, updatePasswordListEvent.mPasswordTextInfoModel));
//                            mRecyclerViewItemArray.add(new ItemData<>(1, updatePasswordListEvent.mPasswordTextInfoModel));
                            if (mRecyclerView != null) {
                                mRecyclerView.getAdapter().notifyItemInserted(0);
                                mRecyclerView.getAdapter().notifyItemRangeChanged(0, mRecyclerViewItemArray.size());
                            }

                        } else {
                            mPasswordTextInfoModels.set(updatePasswordListEvent.position, updatePasswordListEvent.mPasswordTextInfoModel);
                            mRecyclerViewItemArray.set(updatePasswordListEvent.position, new ItemData<>(1, updatePasswordListEvent.mPasswordTextInfoModel));
                            if (mRecyclerView != null)
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                        if (mRecyclerView !=null){
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mSonGokuLayout.setVisibility(View.GONE);
                        }
//                        if (mRecyclerView != null)
//                            mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            });
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_item_list, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        readLocal();
        return view;
    }

    private Subscription mSubscription;
    private MaterialDialog mProgressMaterialDialog;
    ArrayList<PasswordTextInfoModel> mPasswordTextInfoModels = new ArrayList<PasswordTextInfoModel>();

    private void readLocal() {
        mSubscription = Observable.create(new Observable.OnSubscribe<ArrayList<PasswordTextInfoModel>>() {
            @Override
            public void call(Subscriber<? super ArrayList<PasswordTextInfoModel>> subscriber) {
//                List<PasswordTextItem> passwordTextItems = DBHelper.getInstance().getPasswordTextItemDao().loadAll();
                PasswordTextItemDao passwordTextItemDao = DBHelper.getInstance().getPasswordTextItemDao();
                List<PasswordTextItem> passwordTextItems = passwordTextItemDao.queryBuilder()
                        .orderDesc(PasswordTextItemDao.Properties.Id).list();
                ArrayList<PasswordTextInfoModel> passwordTextInfoModels = new ArrayList<PasswordTextInfoModel>();
                for (PasswordTextItem temp : passwordTextItems) {
                    PasswordTextInfoModel passwordTextInfoModel = new PasswordTextInfoModel();
                    passwordTextInfoModel.id = temp.getId();
                    passwordTextInfoModel.jsonString = temp.getJsonString();
                    passwordTextInfoModel.description = temp.getDescription();
                    passwordTextInfoModel.time = temp.getDate();
                    passwordTextInfoModel.akString = temp.getAk();
                    Log.d("akstring", passwordTextInfoModel.akString);
                    String realPassword = "";
                    try {
                        String decryptedString = Sercurity.aesDecrypt(passwordTextInfoModel.jsonString
                                , passwordTextInfoModel.akString.getBytes());
                        Gson gson = new Gson();
                        ArrayList<SingleCharPasswordRuleModel> singleCharPasswordRuleModels =
                                gson.fromJson(decryptedString,
                                        new TypeToken<ArrayList<SingleCharPasswordRuleModel>>() {
                                        }.getType());
                        HashSet<String> ruleNames = new HashSet<String>();
                        for (SingleCharPasswordRuleModel singleCharPasswordRuleModel : singleCharPasswordRuleModels) {
                            realPassword += singleCharPasswordRuleModel.mTargetChar;
                            if (!TextUtils.isEmpty(singleCharPasswordRuleModel.mRuleName))
                                ruleNames.add(singleCharPasswordRuleModel.mRuleName);
                            passwordTextInfoModel.passwordPreview += singleCharPasswordRuleModel.mDestinyChar;
                        }
                        passwordTextInfoModel.realPassword = realPassword;
                        if (ruleNames.size() == 0) {
                            passwordTextInfoModel.ruleName = "";
                        } else {
                            for (String tempName : ruleNames) {
                                passwordTextInfoModel.ruleName += tempName + ",";
                            }
                        }

                        if (passwordTextInfoModel.ruleName.endsWith(",")) {
                            passwordTextInfoModel.ruleName = passwordTextInfoModel.ruleName.substring(0, passwordTextInfoModel.ruleName.length() - 2);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    passwordTextInfoModels.add(passwordTextInfoModel);
                }
                subscriber.onNext(passwordTextInfoModels);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mProgressMaterialDialog = new MaterialDialog.Builder(getContext())
                                .customView(R.layout.single_progress, false)
                                .show();
                    }
                }).subscribe(new Subscriber<ArrayList<PasswordTextInfoModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressMaterialDialog.dismiss();
                    }

                    @Override
                    public void onNext(ArrayList<PasswordTextInfoModel> passwordTextInfoModels) {
                        mProgressMaterialDialog.dismiss();
                        mPasswordTextInfoModels = passwordTextInfoModels;
                        setRV();
                    }
                });
    }

    private void setRV() {
        mRecyclerViewItemArray.clear();
        for (PasswordTextInfoModel textInfoModel : mPasswordTextInfoModels) {
            ItemData<PasswordTextInfoModel> infoModelItemData = new ItemData<>(1, textInfoModel);
            mRecyclerViewItemArray.add(infoModelItemData);
        }
        if (mRecyclerViewItemArray.size() == 0) {
            mSonGokuLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mSonGokuLayout.setImageResource(R.mipmap.no_order_bg);
        } else {
            mSonGokuLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setAdapter(new PasswordRecyclerViewAdapter(mRecyclerViewItemArray, mListener));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (null != mSubscription && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        if (mUpdateSubscrition != null && mUpdateSubscrition.isUnsubscribed()) {
            mUpdateSubscrition.unsubscribe();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PasswordTextInfoModel item, int position);
    }
}
