package com.software_force.myinvoice;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import com.software_force.myinvoice.models.Invoice;

import java.io.File;
import java.io.IOException;
import java.util.List;

// public class MainActivity extends ListActivity {
public class MainActivity extends AppCompatActivity {

    private TableLayout table;
    private List<Invoice> data;
    private InvoicesDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // listView = getListView();
        this.table =  (TableLayout)this.findViewById(R.id.invoices_table);

        this.dataSource = new InvoicesDataSource(this);
        this.dataSource.open();
        // cursor =  dataSource.getAllInvoicesAsCursor();
        this.data = dataSource.getAllInvoices();

        if (this.dataSource.numberOfRows() == 0){
            this.dataSource.CreateSample();
        }

        String targetPath = "/mnt/extSdCard/Android/data/com.software_force.myinvoice";

        // Copy internal database files to SD Card for checking
        try {

            File targetLocation= new File(targetPath);
            Utility.copyDirectoryOneLocationToAnotherLocation(
                    new File("/data/data/com.software_force.myinvoice/databases"),
                    targetLocation);

            String[] children = targetLocation.list();
            for (int i = 0; i < targetLocation.listFiles().length; i++) {
                MediaScannerConnection.scanFile(this, new String[]{ new File(targetPath, children[i]).getAbsolutePath() }, null, null);
            }
        }
        catch (IOException e){
            Log.e( "main_activity",  e.getMessage());
        }

        new Handler().post(new Runnable() {

            @Override
            public void run() {
                InvoicesTableLoader loader = new InvoicesTableLoader( MainActivity.this, table, data);
                loader.Build();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        int id = item.getItemId();
        if (id == R.id.action_search)
        {
            Toast.makeText(getApplicationContext(), "SEARCH", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_add) {
            // Toast.makeText(getApplicationContext(), "ADD", Toast.LENGTH_SHORT).show();
            showAddInvoice();
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "DEFAULT", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    private void  showAddInvoice() {
        Intent intent = new Intent(this, InvoiceEditActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        this.dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.dataSource.close();
        super.onPause();
    }

}
