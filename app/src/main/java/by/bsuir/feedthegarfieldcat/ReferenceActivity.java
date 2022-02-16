package by.bsuir.feedthegarfieldcat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ReferenceActivity extends AppCompatActivity {

    ViewPager viewPager;
    DotsIndicator dots;
    ViewAdapter viewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);

        viewPager = findViewById(R.id.view_pager);
        dots = findViewById(R.id.dots);

        viewAdapter = new ViewAdapter(this);
        viewPager.setAdapter(viewAdapter);
        dots.setViewPager(viewPager);
    }
}