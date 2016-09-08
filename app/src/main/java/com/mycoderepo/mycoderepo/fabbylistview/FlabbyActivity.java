package com.mycoderepo.mycoderepo.fabbylistview;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mycoderepo.mycoderepo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2016/9/8.
 */
public class FlabbyActivity extends ListActivity{
    private static final int NUM_LIST_ITEM = 500;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flabby_listview);
        List<String> items = getListItems();
        mAdapter = new ListAdapter(this, items);
        setListAdapter(mAdapter);
        getListView().setSelection(items.size()/2);
    }

    private List<String> getListItems() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < NUM_LIST_ITEM; i++) {
            list.add("Item"+i);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
