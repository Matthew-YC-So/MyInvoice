package com.software_force.myinvoice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.software_force.myinvoice.models.Item;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {

    private TableLayout table;
    private List<Item> data;
    private ItemsDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        this.table =  (TableLayout)this.findViewById(R.id.items_table);

        this.dataSource = new ItemsDataSource(this);
        this.dataSource.open();
        this.data = dataSource.getAllItems();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ItemsTableLoader loader = new ItemsTableLoader(ItemsActivity.this, table, data);
                loader.Build();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_confirm:

                Intent output = new Intent();
                output.putParcelableArrayListExtra(InvoiceEditActivity.SELECTED_ITEMS, GetSelectedItems());
                setResult(RESULT_OK, output);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Item> GetSelectedItems(){
        ArrayList<Item> list = new ArrayList<Item>();
        for (int i = 1; i < this.table.getChildCount(); ++i) {
            View view = this.table.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                CheckBox cbx = (CheckBox) row.getChildAt(0);
                if (cbx.isChecked()){
                    Item item = new Item();
                    item.setCode( ((TextView)row.getChildAt(1)).getText().toString());
                    item.setDescription(((TextView) row.getChildAt(2)).getText().toString());
                    BigDecimal price = new BigDecimal(((TextView) row.getChildAt(3)).getText().toString());
                    item.setPrice(price);
                    list.add(item);
                }
            }
        }

        return  list;
    }

}
