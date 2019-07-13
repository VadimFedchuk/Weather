package com.vadimfedchuk1994gmail.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vadimfedchuk1994gmail.weather.R;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DialogFragmentAdapter  extends RecyclerView.Adapter<DialogFragmentAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mDataSet;
    private  SingleDialogClickListener sClickListener;
    private  int isSelected = -1;

    public DialogFragmentAdapter(Context context, List<String> list){
        mContext = context;
        mDataSet = list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        String city = mDataSet.get(position);
        holder.bind(city);
        if(position == isSelected) {
            holder.mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.mTextViewLabel.setTextColor(mContext.getResources().getColor(android.R.color.white));
            holder.imageLocation.setImageResource(R.drawable.ic_add_location_white_24dp);
        } else {
            holder.mCardView.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            holder.mTextViewLabel.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.imageLocation.setImageResource(R.drawable.ic_add_location_blue_24dp);
        }

    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(SingleDialogClickListener clickListener) {
        sClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cities_list, parent, false);
        return new ViewHolder(view);
    }

    public void setList(List<String> dataSet) {
        mDataSet.clear();
        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return mDataSet.size();
    }

    public interface SingleDialogClickListener {
        void onItemClickListener(int position, View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.card_view_fragment_dialog)
        CardView mCardView;
        @BindView(R.id.city_country_text_view)
        TextView mTextViewLabel;
        @BindView(R.id.image_location)
        ImageView imageLocation;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            isSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), v);
        }

        private void bind(String city) {

            mTextViewLabel.setText(city);
        }
    }
}
