package com.hh.ptpen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    PTDrawView mDrawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawView = this.findViewById(R.id.dv_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.option_menu , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_clear:
                mDrawView.reset();
                break;
            case R.id.menu_standard:
                mDrawView.setPenType(PTBasePen.PTPenType.standard);
                break;
            case R.id.menu_mark:
                mDrawView.setPenType(PTBasePen.PTPenType.mark);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
