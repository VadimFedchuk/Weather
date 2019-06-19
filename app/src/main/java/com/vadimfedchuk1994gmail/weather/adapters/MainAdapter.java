package com.vadimfedchuk1994gmail.weather.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vadimfedchuk1994gmail.weather.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mDataSet;
    private ClickListener clicklistener;

    public MainAdapter(Context context, List<String> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_view, parent,false);
        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        String animal = mDataSet.get(position);
        holder.bind(animal, mContext);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public void setClickListener(ClickListener listener) {
        this.clicklistener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_main) CardView mCardView;
        @BindView(R.id.cityCountryName) TextView mNameTextView;
        @BindView(R.id.icon_current_weather) ImageView mIconImageView;
        @BindView(R.id.description_weather) TextView mDescriptionTextView;
        @BindView(R.id.current_temperature) TextView mTemperatureTextView;
        @BindView(R.id.max_temperature) TextView mTempMaxTextView;
        @BindView(R.id.min_temperature) TextView mTempMinTextView;
        @BindView(R.id.wind_speed_destination) TextView mWindTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        private void bind(String city, Context mContext) {

            mNameTextView.setText(city);
            Resources resources = mContext.getResources();
            final int resourceId = resources.getIdentifier("image" + "", "drawable",
                    mContext.getPackageName());
            mIconImageView.setImageResource(resourceId);
            mDescriptionTextView.setText(city);
            mTemperatureTextView.setText(mContext.getResources().getString(R.string.current_temperature, 30));
            mTempMaxTextView.setText(mContext.getResources().getString(R.string.max_temperature, 30));
            mTempMinTextView.setText(mContext.getResources().getString(R.string.max_temperature, 30));
            mWindTextView.setText(mContext.getResources().getString(R.string.wind_speed, "ВЮВ", 1000));

            mCardView.setOnClickListener(v -> clicklistener.onClick(v, getAdapterPosition()));
            mCardView.setOnLongClickListener(v -> {
                clicklistener.onLongClick(v, getAdapterPosition());
                return true;
            });
        }
    }

    public void setList(List<String> dataSet) {
        this.mDataSet = dataSet;
        notifyDataSetChanged();
    }
}