package com.hsue.sue.pagelayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class InfoActivity extends Activity {

    String Product_name;
    TextView ProductView, FolderView, SerialView, MemoView;
    DBFunction function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        function = new DBFunction(this);

        Product_name = (String)getIntent().getExtras().get("Product_name");
        String[] result;
        result = function.DBselectall(Product_name).split(",");


        ProductView = (TextView)findViewById(R.id.info_product);
        FolderView = (TextView)findViewById(R.id.info_folder);
        SerialView = (TextView)findViewById(R.id.info_serial);
        MemoView = (TextView)findViewById(R.id.info_memo);



        ProductView.setText(result[0]);
        FolderView.setText(result[1]);
        SerialView.setText(result[2]);
        MemoView.setText(result[3]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
