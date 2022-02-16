package by.bsuir.feedthegarfieldcat;

import android.content.Context;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {
    Context context;
    List<UserInfo.Achievement> achievementList;

    public AchievementAdapter(Context context, List<UserInfo.Achievement> list) {
        this.context = context;
        this.achievementList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.achievement_item_layoutx, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList mainList = Database.achievements;
        if (mainList != null && !mainList.isEmpty()) {
            if (position < achievementList.size()){
                holder.desc_it.setBackgroundResource(R.color.orange_100);
                holder.title_it.setBackgroundResource(R.color.orange_100);
            }
            UserInfo.Achievement uniModel = (UserInfo.Achievement) mainList.get(position);
            //UserInfo.Achievement userModel = achievementList.get(position);

            holder.title_it.setText(uniModel.title);
            holder.desc_it.setText("Get " + String.valueOf(uniModel.beatScore) + " score or higher");

        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return Database.achievements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_it, desc_it;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title_it = itemView.findViewById(R.id.title_it);
            desc_it = itemView.findViewById(R.id.desc_it);
        }
    }
}