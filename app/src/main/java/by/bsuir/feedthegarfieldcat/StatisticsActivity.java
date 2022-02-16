package by.bsuir.feedthegarfieldcat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GameInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        recyclerView = findViewById(R.id.recycler_view);
        
        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GameInfoAdapter(this, getList());
        recyclerView.setAdapter(adapter);
    }

    private List<UserInfo.GameInfo> getList() {
        return MainActivity.curUser.GetArrayListGameInfo();
    }
}