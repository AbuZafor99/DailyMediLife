package com.example.knowledgecheck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private final List<Meal> mealList;
    private OnMealClickListener listener = null;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
        void onAlarmToggle(Meal meal, boolean isOn);
    }

    public MealAdapter(List<Meal> mealList) {
        this.mealList = mealList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_list, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMealIcon;
        private final TextView tvMealType;
        private final TextView tvMealDetails;
        private final ImageButton btnAlarm;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealIcon = itemView.findViewById(R.id.ivMealIcon);
            tvMealType = itemView.findViewById(R.id.tvMealType);
            tvMealDetails = itemView.findViewById(R.id.tvMealDetails);
            btnAlarm = itemView.findViewById(R.id.btnAlarm);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onMealClick(mealList.get(position));
                }
            });
        }

        public void bind(Meal meal) {
            ivMealIcon.setImageResource(meal.getIconRes());
            tvMealType.setText(meal.getMealType());
            tvMealDetails.setText(String.format("%s - %s", meal.getTime(), meal.getDescription()));

            btnAlarm.setImageResource(meal.isAlarmOn()
                    ? R.drawable.alarm_on_icon
                    : R.drawable.alarm_off_icon);

            btnAlarm.setOnClickListener(v -> {
                boolean newAlarmState = !meal.isAlarmOn();
                meal.setAlarmOn(newAlarmState);
                btnAlarm.setImageResource(newAlarmState
                        ? R.drawable.alarm_on_icon
                        : R.drawable.alarm_off_icon);
                listener.onAlarmToggle(meal, newAlarmState);
            });
        }
    }
}