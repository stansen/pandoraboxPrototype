package me.onionpie.pandorabox.UI.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Temp.RecyclerViewItemArray;

/**
 * Created by Gstansen on 2016/5/2.
 */
public class RuleSettingGridAdapter extends RecyclerView.Adapter {
    private char[] mLists;
    public RuleSettingGridAdapter (char[] itemDatas){
        mLists = itemDatas;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rule_grid_item,parent,false);
        RuleGridViewHolder viewHolder = new RuleGridViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RuleGridViewHolder ruleGridViewHolder = (RuleGridViewHolder)holder;
        ruleGridViewHolder.mGridItemButton.setText(String.valueOf(mLists[position]));
    }

    @Override
    public int getItemCount() {
        return mLists.length;
    }
    class  RuleGridViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.grid_item_button)
        Button mGridItemButton;
        public RuleGridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
