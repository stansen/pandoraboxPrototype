package me.onionpie.pandorabox.UI.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.greendao.DBHelper;
import me.onionpie.greendao.PasswordTextItem;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;
import me.onionpie.pandorabox.Model.SingleCharPasswordRuleModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Temp.ItemData;
import me.onionpie.pandorabox.Temp.RecyclerViewItemArray;
import me.onionpie.pandorabox.UI.Adapter.PasswordRecyclerViewAdapter;
import me.onionpie.pandorabox.Utils.Sercurity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PasswordListFragment extends BaseFragment {

    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerViewItemArray mRecyclerViewItemArray = new RecyclerViewItemArray();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PasswordListFragment() {
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
                List<PasswordTextItem> passwordTextItems = DBHelper.getInstance().getPasswordTextItemDao().loadAll();
                ArrayList<PasswordTextInfoModel> passwordTextInfoModels = new ArrayList<PasswordTextInfoModel>();
                for (PasswordTextItem temp : passwordTextItems) {
                    PasswordTextInfoModel passwordTextInfoModel = new PasswordTextInfoModel();
                    passwordTextInfoModel.id = temp.getId();
                    passwordTextInfoModel.jsonString = temp.getJsonString();
                    passwordTextInfoModel.description = temp.getDescription();
                    passwordTextInfoModel.time = temp.getDate();
                    passwordTextInfoModel.akString = temp.getAk();
                    Log.d("akstring",passwordTextInfoModel.akString);
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
                            ruleNames.add(singleCharPasswordRuleModel.mRuleName);
                        }
                        passwordTextInfoModel.realPassword = realPassword;
                        int position = 0;
                        for (String tempName : ruleNames) {
                            if (position != ruleNames.size() - 1)
                                passwordTextInfoModel.ruleName += tempName + ",";
                            else passwordTextInfoModel.ruleName += tempName;
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
        for (PasswordTextInfoModel textInfoModel : mPasswordTextInfoModels) {
            ItemData<PasswordTextInfoModel> infoModelItemData = new ItemData<>(1, textInfoModel);
            mRecyclerViewItemArray.add(infoModelItemData);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mSubscription && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PasswordTextInfoModel item);
    }
}
