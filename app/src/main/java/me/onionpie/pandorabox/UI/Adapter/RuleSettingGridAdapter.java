package me.onionpie.pandorabox.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.R;

/**
 * Created by Gstansen on 2016/5/2.
 */
public class RuleSettingGridAdapter extends RecyclerView.Adapter {
    private char[] mLists;

    public RuleSettingGridAdapter(char[] itemDatas, onGridItemClickListener listener) {
        mLists = itemDatas;
        mOnGridItemClickListener = listener;
    }

    private onGridItemClickListener mOnGridItemClickListener;
    private Context mContext;

    public interface onGridItemClickListener {
        void onItemClicked(View view, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rule_grid_item, parent, false);
        RuleGridViewHolder viewHolder = new RuleGridViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final RuleGridViewHolder ruleGridViewHolder = (RuleGridViewHolder) holder;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 15, mContext.getResources().getDisplayMetrics());

        if (position == mLists.length - 1) {
            layoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 15, mContext.getResources().getDisplayMetrics());
        }
        ruleGridViewHolder.mLinearLayout.setLayoutParams(layoutParams);
        ruleGridViewHolder.mGridItemButton.setText(String.valueOf(mLists[position]));
        ruleGridViewHolder.mNum.setText(String.valueOf(position + 1));
        ruleGridViewHolder.mGridItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnGridItemClickListener.onItemClicked(ruleGridViewHolder.mItemCheckedImageView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists.length;
    }

    class RuleGridViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.num)
        TextView mNum;
        @Bind(R.id.grid_item_button)
        TextView mGridItemButton;
        @Bind(R.id.is_item_checked)
        ImageView mItemCheckedImageView;
        @Bind(R.id.grid_item_container)
        LinearLayout mLinearLayout;

        public RuleGridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
