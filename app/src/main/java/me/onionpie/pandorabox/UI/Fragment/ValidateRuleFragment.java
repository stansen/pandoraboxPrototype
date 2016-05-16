package me.onionpie.pandorabox.UI.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Rx.Event.UpdateValidateConfigEvent;
import me.onionpie.pandorabox.Rx.RxBus;
import me.onionpie.pandorabox.UI.Activity.PasswordDetailActivity;
import me.onionpie.pandorabox.UI.Activity.ResetValidateActivity;
import me.onionpie.pandorabox.Utils.CommonPreference;
import rx.Subscription;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class ValidateRuleFragment extends BaseFragment {
    @Bind(R.id.scan_code_switch)
    SwitchButton mScanCodeSwitch;
    @Bind(R.id.rule_setting)
    LinearLayout mRuleSetting;
    @Bind(R.id.password_switch)
    SwitchButton mPasswordSwitch;


//    @Bind(R.id.password_rule_list)
//    RecyclerView mPasswordRuleList;

    public ValidateRuleFragment() {
        // Required empty public constructor
    }
    private Subscription mSubscription;
    @Override
    public void onStart() {
        super.onStart();
        if (mSubscription == null || !mSubscription.isUnsubscribed()){
            mSubscription = RxBus.getInstance().toObserverable().subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    if (o instanceof UpdateValidateConfigEvent){
                        try {
                            setSwitchButtonStatus();
                        }catch (Exception e){

                        }
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_rule, container, false);
        ButterKnife.bind(this, view);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        mPasswordRuleList.setLayoutManager(linearLayoutManager);
//        mPasswordRuleList.setAdapter(new PasswordRuleListAdapter(generateDefaultRules()));
        setSwitchButtonStatus();
        mScanCodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CommonPreference.putBoolean(getContext(),Constans.KEY_SET_SCAN_CODE_VALIDATE,isChecked);
            }
        });
        mPasswordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (TextUtils.isEmpty(CommonPreference.getString(getContext(),Constans.KEY_PASSWORD_VALUE)))
                        showPasswordDialog();
                    else CommonPreference.putBoolean(getContext(),Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE,true);
                }else CommonPreference.putBoolean(getContext(),Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE,false);

            }
        });
        return view;
    }
    private void showPasswordDialog() {
        new MaterialDialog.Builder(getContext())
                .title("验证密码")
                .content("请输入验证的密码")
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(4, 16)
                .positiveText("确定")
                .negativeText("取消")
                .cancelable(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPasswordSwitch.setChecked(false);
                    }
                })
                .input("密码不要太短了哦", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        String password = input.toString();
                        showToast(password);
                        CommonPreference.putString(getContext(),Constans.KEY_PASSWORD_VALUE,password);
                        CommonPreference.putBoolean(getContext(),Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE,true);
                    }
                }).show();
    }
    private void setSwitchButtonStatus(){
        if (CommonPreference.getBoolean(getContext(),Constans.KEY_SET_SCAN_CODE_VALIDATE)){
            mScanCodeSwitch.setChecked(true);
            mScanCodeSwitch.setEnabled(false);
        }else {
            mScanCodeSwitch.setEnabled(true);
            mScanCodeSwitch.setChecked(false);
        }
        if (CommonPreference.getBoolean(getContext(),Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE)){
            mPasswordSwitch.setChecked(true);
            mScanCodeSwitch.setEnabled(false);
        }else {
            mPasswordSwitch.setChecked(false);
            mScanCodeSwitch.setEnabled(true);
        }
    }


    //    private RecyclerViewItemArray generateDefaultRules(){
//        RecyclerViewItemArray recyclerViewItemArray = new RecyclerViewItemArray();
//        ItemData<PasswordRuleNameModel> itemDataHide = new ItemData<>();
//        itemDataHide.setDataType(0);
//        itemDataHide.setData(new PasswordRuleNameModel(PasswordRuleDefault.RULE_HIDE,"隐藏","隐藏某个位置的密码"));
//        recyclerViewItemArray.add(itemDataHide);
//        ItemData<PasswordRuleNameModel> itemDataReplace = new ItemData<>();
//        itemDataReplace.setDataType(0);
//        itemDataReplace.setData(new PasswordRuleNameModel(PasswordRuleDefault.RULE_REPLACE,"替换","替换某个位置的密码"));
//        recyclerViewItemArray.add(itemDataReplace);
//        ItemData<PasswordRuleNameModel> itemDataReverse = new ItemData<>();
//        itemDataReverse.setDataType(0);
//        itemDataReverse.setData(new PasswordRuleNameModel(PasswordRuleDefault.RULE_REPLACE,"反转","反转某个位置的密码"));
//        recyclerViewItemArray.add(itemDataReplace);
//        return recyclerViewItemArray;
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription !=null)
            mSubscription.unsubscribe();
    }

    @OnClick(R.id.reset)
    public void onClickReset(){
        Intent intent = new Intent(getActivity(), ResetValidateActivity.class);
        startActivity(intent);
    }

}
