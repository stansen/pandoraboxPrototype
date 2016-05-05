package me.onionpie.pandorabox.UI.Adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.Model.SingleCharPasswordRuleModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Temp.RecyclerViewItemArray;

/**
 * Created by Gstansen on 2016/4/30.
 */
public class PasswordRuleListAdapter extends RecyclerView.Adapter {
    private RecyclerViewItemArray mRecyclerViewItemArray = new RecyclerViewItemArray();

    public PasswordRuleListAdapter(RecyclerViewItemArray recyclerViewItemArray) {
        this.mRecyclerViewItemArray = recyclerViewItemArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.password_rule_item_view, parent, false);
        return new RuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RuleViewHolder ruleViewHolder = (RuleViewHolder)holder;
        ruleViewHolder.mItem = (SingleCharPasswordRuleModel)mRecyclerViewItemArray.get(position).getData();
        ruleViewHolder.mRuleName.setText(ruleViewHolder.mItem.mRuleName);
        ruleViewHolder.mRuleDescription.setText(ruleViewHolder.mItem.mRuleDescription);
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItemArray.size();
    }

    class RuleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rule_name)
        public TextView mRuleName;
        @Bind(R.id.rule_description)
        public TextView mRuleDescription;
        public SingleCharPasswordRuleModel mItem;
        public RuleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
