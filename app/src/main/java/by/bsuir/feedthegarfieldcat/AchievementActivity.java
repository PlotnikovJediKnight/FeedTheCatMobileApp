package by.bsuir.feedthegarfieldcat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AchievementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        recyclerView = findViewById(R.id.recycler_view_achiev);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AchievementAdapter(this, getList());
        recyclerView.setAdapter(adapter);
    }

    private List<UserInfo.Achievement> getList() {
        return MainActivity.curUser.GetArrayListAchievement();
    }
}