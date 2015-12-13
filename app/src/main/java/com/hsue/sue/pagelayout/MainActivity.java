package com.hsue.sue.pagelayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    Button folder_button, search_button, register_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        folder_button = (Button)findViewById(R.id.folder);
        search_button = (Button)findViewById(R.id.search);
        register_button = (Button)findViewById(R.id.register);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(i,0);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(i,0);
            }
        });

         //////////////////////////////////////////////////////////////////////////
        final ListView listView=(ListView)findViewById(R.id.listview);

        ArrayList<Listviewitem> data=new ArrayList<>();
        Listviewitem bookmark=new Listviewitem(R.drawable.folder,"북마크");
        Listviewitem appliance=new Listviewitem(R.drawable.folder,"가전제품");
        Listviewitem electronics=new Listviewitem(R.drawable.folder,"전자기기");
        Listviewitem clothing=new Listviewitem(R.drawable.folder,"의류");
        Listviewitem extra =new Listviewitem(R.drawable.folder,"기타");

        data.add(bookmark);
        data.add(appliance);
        data.add(electronics);
        data.add(clothing);
        data.add(extra);

        ListviewAdapter adapter=new ListviewAdapter(this,R.layout.listviewitem,data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                Intent i= new Intent(MainActivity.this, FolderActivity.class);
                i.putExtra("Folder", item);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
