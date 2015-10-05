package com.software_force.myinvoice;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TableLayout;

import com.software_force.myinvoice.models.Invoice;

import java.io.File;
import java.io.IOException;
import java.util.List;

// public class MainActivity extends ListActivity {
public class MainActivity extends Activity {

    // private ListView listView;
    private  TableLayout table;
    private InvoicesAdapter customAdapter;
    // private Cursor cursor;
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
//                customAdapter = new InvoicesAdapter(MainActivity.this, cursor, 0);
 //               listView.setAdapter(customAdapter);
                InvoicesTableLoader loader = new InvoicesTableLoader( MainActivity.this, table, data);
                loader.Build();
            }

        });



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
