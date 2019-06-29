package com.vadimfedchuk1994gmail.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.pojo.TypeWeather;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.PictureHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private ClickListener clicklistener;
    private List<Weather> mDataSet;


    public MainAdapter(Context context, List<Weather> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_view, parent, false);
        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        Weather data = mDataSet.get(position);
        holder.bind(data, mContext, getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setList(List<Weather> dataSet) {
        this.mDataSet = dataSet;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return PictureHelper.choosePicture(mDataSet.get(position).icon);
    }

    public interface ClickListener {
        void onClick(String name);

        void onLongClick(Weather data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_main)
        CardView mCardView;
        @BindView(R.id.text_cityName)
        TextView mNameTextView;
        @BindView(R.id.image_weather_icon)
        ImageView mIconImageView;
        @BindView(R.id.text_description_weather)
        TextView mDescriptionTextView;
        @BindView(R.id.text_current_temperature)
        TextView mCurrentTemperatureTextView;
        @BindView(R.id.text_max_min_temperature)
        TextView mMaxMinTemperatureTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> clicklistener.onClick(mDataSet.get(getAdapterPosition()).name));
            itemView.setOnLongClickListener(v -> {
                clicklistener.onLongClick(mDataSet.get(getAdapterPosition()));
                return true;
            });
        }

        private void bind(Weather data, Context mContext, int viewType) {
            TypeWeather obj = PictureHelper.generateObject(mContext, viewType);
            mCardView.setCardBackgroundColor(obj.getResourceIdBackground());
            mNameTextView.setText(data.name);
            mIconImageView.setImageResource(obj.getResourceIdIcon());
            mDescriptionTextView.setText(obj.getDescription());
            mCurrentTemperatureTextView.setText(mContext.getResources().getString(R.string.current_temperature, (int) data.temperature));
            mMaxMinTemperatureTextView.setText(mContext.getResources().getString(R.string.max_min_temperature, (int) data.max_temp, (int) data.min_temp));

        }
    }
}