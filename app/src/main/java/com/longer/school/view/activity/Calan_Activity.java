package com.longer.school.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.longer.school.Application;
import com.longer.school.R;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

/**
 * 校历界面
 */

public class Calan_Activity extends AppCompatActivity {

    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_calan);

        init();
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("校历");
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        }
        imageView = (ImageView) findViewById(R.id.piv_calan);
        imageView.setImageResource(R.drawable.calan16_17_2);
    }

    /**
     * 三点菜单
     *
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_16_17A) {
            imageView.setImageResource(R.drawable.calan16_17_1);
            return true;
        }else if(id == R.id.action_16_17B){
            imageView.setImageResource(R.drawable.calan16_17_2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "校历");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
