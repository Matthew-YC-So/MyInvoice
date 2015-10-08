package com.software_force.myinvoice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

import com.software_force.myinvoice.models.Invoice;
import com.software_force.myinvoice.models.Item;

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

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ItemsTableLoader loader = new ItemsTableLoader(ItemsActivity.this, table, data);
                loader.Build();
            }

        });
    }
}
