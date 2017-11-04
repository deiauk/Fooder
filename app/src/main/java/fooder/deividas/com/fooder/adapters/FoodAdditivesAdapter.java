package fooder.deividas.com.fooder.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fooder.deividas.com.fooder.ColorHandler;
import fooder.deividas.com.fooder.R;
import fooder.deividas.com.fooder.Utils;
import fooder.deividas.com.fooder.database.models.FoodAdditive;
import fooder.deividas.com.fooder.events.AdditiveClickEvent;

/**
 * Created by Deividas on 2017-10-28.
 */

public class FoodAdditivesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {

    private List<FoodAdditive> dataList;

    public FoodAdditivesAdapter(List<FoodAdditive> dataList) {
        this.dataList = dataList;
    }

    public void setList(List<FoodAdditive> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_additive_item, parent, false);
        return new FoodAdditiveViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FoodAdditive data = dataList.get(position);
        FoodAdditiveViewHolder listViewHolder = (FoodAdditiveViewHolder) holder;
        listViewHolder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    protected class FoodAdditiveViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.number)
        TextView number;

        @BindView(R.id.name)
        TextView name;

        private FoodAdditiveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.foodAdditiveItem)
        public void onItemClick() {
            Log.d("ASDASGUIDSAD", "sdfsdf");
            EventBus.getDefault().post(new AdditiveClickEvent(dataList.get(getAdapterPosition()).getId()));
        }

        private void bind(FoodAdditive item) {

            number.setText(item.getNumber());
            number.setTextColor(ColorHandler.getColor(item.getDanger().getDangerLevel()));
            name.setText(item.getName());
        }
    }
}
