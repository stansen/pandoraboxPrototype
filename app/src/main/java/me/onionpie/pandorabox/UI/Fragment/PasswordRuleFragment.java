package me.onionpie.pandorabox.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.Model.PasswordRuleDefault;
import me.onionpie.pandorabox.Model.SingleCharPasswordRuleModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Temp.ItemData;
import me.onionpie.pandorabox.Temp.RecyclerViewItemArray;
import me.onionpie.pandorabox.UI.Adapter.PasswordRuleListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordRuleFragment extends Fragment {


    @Bind(R.id.password_rule_list)
    RecyclerView mPasswordRuleList;

    public PasswordRuleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_rule, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mPasswordRuleList.setLayoutManager(linearLayoutManager);
        mPasswordRuleList.setAdapter(new PasswordRuleListAdapter(generateDefaultRules()));
        return view;
    }
    private RecyclerViewItemArray generateDefaultRules(){
        RecyclerViewItemArray recyclerViewItemArray = new RecyclerViewItemArray();
        ItemData<SingleCharPasswordRuleModel> itemDataHide = new ItemData<>();
        itemDataHide.setDataType(0);
        itemDataHide.setData(new SingleCharPasswordRuleModel(PasswordRuleDefault.RULE_HIDE,"隐藏","隐藏某个位置的密码"));
        recyclerViewItemArray.add(itemDataHide);
        ItemData<SingleCharPasswordRuleModel> itemDataReplace = new ItemData<>();
        itemDataReplace.setDataType(0);
        itemDataReplace.setData(new SingleCharPasswordRuleModel(PasswordRuleDefault.RULE_REPLACE,"替换","替换某个位置的密码"));
        recyclerViewItemArray.add(itemDataReplace);
        ItemData<SingleCharPasswordRuleModel> itemDataReverse = new ItemData<>();
        itemDataReverse.setDataType(0);
        itemDataReverse.setData(new SingleCharPasswordRuleModel(PasswordRuleDefault.RULE_REPLACE,"反转","反转某个位置的密码"));
        recyclerViewItemArray.add(itemDataReplace);
        return recyclerViewItemArray;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
