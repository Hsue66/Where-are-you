package com.hsue.sue.pagelayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FolderActivity extends Activity {

    Button folder_button, search_button, register_button;
    TextView empty;
    String Folder;
    DBFunction dbF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        Folder = (String)getIntent().getExtras().get("Folder");

        folder_button = (Button)findViewById(R.id.folder);
        search_button = (Button)findViewById(R.id.search);
        register_button = (Button)findViewById(R.id.register);
        empty = (TextView)findViewById(R.id.fol_empty);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(FolderActivity.this, SearchActivity.class);
                startActivityForResult(i,0);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(FolderActivity.this, RegisterActivity.class);
                startActivityForResult(i,0);
            }
        });

        dbF = new DBFunction(this);
        String[] result;
        if(Folder.equals("북마크"))
            result = dbF.DBselB().split(",");
        else
            result = dbF.DBselF(Folder).split(",");

        final ListView listView=(ListView)findViewById(R.id.listview);

        ArrayList<Listviewitem> data=new ArrayList<>();
        if(!result[0].equals("")) {
            for (int i = 0; i < result.length; i++) {
                Listviewitem bookmark = new Listviewitem(R.drawable.be, result[i]);
                data.add(bookmark);
            }
        }
        else
        {
            empty.setText("폴더가 비었습니다.");
        }
      //  data.add(appliance);
     //   data.add(electronics);
     //   data.add(clothing);


        ListviewAdapter adapter=new ListviewAdapter(this,R.layout.listviewitem,data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                //Toast.makeText(MainActivity.this, item, Toast.LENGTH_LONG).show();

                Intent i= new Intent(FolderActivity.this, InfoActivity.class);
                i.putExtra("Product_name", item);
                startActivityForResult(i, 0);
            }
        });
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
