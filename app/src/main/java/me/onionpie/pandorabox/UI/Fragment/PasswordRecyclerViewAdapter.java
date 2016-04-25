package me.onionpie.pandorabox.UI.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import me.onionpie.pandorabox.Animation.RecyclerView.GoogleAnimationRecyclerAdapter;
import me.onionpie.pandorabox.Model.PasswordInfoModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Temp.RecyclerViewItemArray;

import java.util.List;

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

        final ViewHolder holder1 = (ViewHolder)holder;
        holder1.mItem = (PasswordInfoModel) mRecyclerViewItemArray.get(position).getData();
        holder1.mIdView.setText(holder1.mItem.password);
        holder1.mContentView.setText(position + "");

        holder1.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder1.mItem);
                }
            }
        });
        holder1.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerViewItemArray.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());
//                notifyItemRangeRemoved(position,mRecyclerViewItemArray.);
            }
        });
        super.onBindViewHolder(holder,position);
    }


    @Override
    public int getItemCount() {
        return mRecyclerViewItemArray.size();
    }

    public class ViewHolder extends GoogleAnimationViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button mDelete;
        public PasswordInfoModel mItem;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);

            mDelete = (Button) view.findViewById(R.id.delete_btn);
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
