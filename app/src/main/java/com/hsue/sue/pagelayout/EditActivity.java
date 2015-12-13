package com.hsue.sue.pagelayout;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class EditActivity extends Activity {

    String Product_name;
    TextView ProductView;
    EditText FolderView, SerialView, MemoView;
    DBFunction function;
    DBFunction dbF;
    Button edit_button;
    int val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        function = new DBFunction(this);

        Product_name = (String) getIntent().getExtras().get("Product_name");
        String[] result;
        result = function.DBselectall(Product_name).split(",");


        ProductView = (TextView) findViewById(R.id.e_product);
        //FolderView = (EditText)findViewById(R.id.e_folder);
        SerialView = (EditText) findViewById(R.id.e_serial);
        MemoView = (EditText) findViewById(R.id.e_memo);


        final Spinner folderSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter folderAdapter = ArrayAdapter.createFromResource(this, R.array.folder_name,
                android.R.layout.simple_spinner_item);
        folderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        folderSpinner.setAdapter(folderAdapter);
        if (result[1].equals("가전제품"))
            val =0;
         else if (result[1].equals("전자기기"))
            val =1;
        else if (result[1].equals("의류"))
            val =2;
        else
            val =3;

        folderSpinner.setSelection(val);


        ProductView.setText(result[0]);
       // FolderView.setText(result[1]);
        SerialView.setText(result[2]);
        MemoView.setText(result[3]);

        edit_button =(Button) findViewById(R.id.e_save);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbF = new DBFunction(EditActivity.this);
        //        String getFolderName = FolderView.getText().toString();
                String getSerial = SerialView.getText().toString();
                String getMemo = MemoView.getText().toString();
                String getEdit = ProductView.getText().toString();
                String getFolderName = folderSpinner.getSelectedItem().toString();
                getEdit = getEdit.trim();
                if (getEdit.getBytes().length <= 0) {
                    Toast.makeText(getApplicationContext(), "값을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    dbF.Productedit(getEdit,getFolderName,getSerial,getMemo);
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                   // dbF.DBinsert(getEdit,getFolderName,getSerial,getMemo);
                   // Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
