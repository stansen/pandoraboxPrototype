package me.onionpie.pandorabox.UI.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import me.onionpie.greendao.DBHelper;
import me.onionpie.pandorabox.Animation.RecyclerView.GoogleAnimationRecyclerAdapter;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Temp.RecyclerViewItemArray;
import me.onionpie.pandorabox.UI.Fragment.PasswordListFragment;
import me.onionpie.pandorabox.Widget.SwipeItemView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link me.onionpie.pandorabox.UI.Fragment.PasswordListFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PasswordRecyclerViewAdapter extends GoogleAnimationRecyclerAdapter {

    //    private final List<DummyItem> mValues;
    private RecyclerViewItemArray mRecyclerViewItemArray = new RecyclerViewItemArray();
    private final PasswordListFragment.OnListFragmentInteractionListener mListener;

    public PasswordRecyclerViewAdapter(RecyclerViewItemArray items, PasswordListFragment.OnListFragmentInteractionListener listener) {
        mRecyclerViewItemArray = items;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_password_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;
        if (mRecyclerViewItemArray.get(position).getDataType() == 1){

            viewHolder.mItem = (PasswordTextInfoModel) mRecyclerViewItemArray.get(position).getData();
//        viewHolder.mIdView.setText(viewHolder.mItem.id + "");
            viewHolder.mPreviewPassword.setText("预览：" + viewHolder.mItem.passwordPreview);
            if (TextUtils.isEmpty(viewHolder.mItem.ruleName))
                viewHolder.mRuleNameTV.setText("规则名：   无");
            else
                viewHolder.mRuleNameTV.setText("规则名：" + viewHolder.mItem.ruleName);
            viewHolder.mDescriptionTV.setText("密码描述：" + viewHolder.mItem.description);
            viewHolder.mTime.setText(viewHolder.mItem.time);
            viewHolder.mSwipeItemView.setOnSingleClickListener(new SwipeItemView.SingleClickListener() {
                @Override
                public void onClick(SwipeItemView view) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(viewHolder.mItem,viewHolder.getAdapterPosition());
                    }
                }
            });
            viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper.getInstance().getPasswordTextItemDao().deleteByKey(viewHolder.mItem.id);
                    mRecyclerViewItemArray.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getLayoutPosition());
//                notifyItemRangeRemoved(position,mRecyclerViewItemArray.);
                }
            });
        }else if (mRecyclerViewItemArray.get(position).getDataType()==2){

        }

        super.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemCount() {
        return mRecyclerViewItemArray.size();
    }

    public class ViewHolder extends GoogleAnimationViewHolder {
        public final View mView;
        //        public final TextView mIdView;
        public final TextView mPreviewPassword;
        public final Button mDelete;
        public final TextView mRuleNameTV;
        public final TextView mDescriptionTV;
        public final SwipeItemView mSwipeItemView;
        public final TextView mTime;
        public PasswordTextInfoModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.id);
            mPreviewPassword = (TextView) view.findViewById(R.id.preview);
            mRuleNameTV = (TextView) view.findViewById(R.id.rule_name);
            mSwipeItemView = (SwipeItemView) view.findViewById(R.id.swipe_item);
            mDescriptionTV = (TextView) view.findViewById(R.id.rule_description);
            mTime = (TextView)view.findViewById(R.id.time);
            mDelete = (Button) view.findViewById(R.id.delete_btn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPreviewPassword.getText() + "'";
        }
    }
}
