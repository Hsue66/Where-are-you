package com.hsue.sue.pagelayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class SearchActivity extends Activity {

    Button folder_button, search_button, register_button;

    AutoCompleteTextView sEdit;
    Button sBtn;
    ArrayAdapter<String> adapter, autoCompleteAdapter,latelyAdapter;;
    ListView listView;
    PopupMenu popupMenu;
    DBFunction dbF;
    Intent i;
    String[] dataSet;

    ArrayList<String> lately = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> autoList = new ArrayList<String>();

    // Dialog 띄우는 변수
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        folder_button = (Button)findViewById(R.id.folder);
        search_button = (Button)findViewById(R.id.search);
        register_button = (Button)findViewById(R.id.register);

        folder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(SearchActivity.this, MainActivity.class);
                startActivityForResult(i,0);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(SearchActivity.this, RegisterActivity.class);
                startActivityForResult(i,0);
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////


        builder = new AlertDialog.Builder(this);

        dbF = new DBFunction(SearchActivity.this);

        sBtn = (Button)findViewById(R.id.searchButton);

        /* 검색어 자동완성 */
        sEdit = (AutoCompleteTextView)findViewById(R.id.searchWord);
        autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autoList);
        sEdit.setAdapter(autoCompleteAdapter);

        listView = (ListView)findViewById(R.id.list);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        /**************************************************/
        dataSet = dbF.Recentselect().split("\n");
        for(int i=0; i < dataSet.length ; i++){
            if(!dataSet[i].equals("empty"))
                lately.add(dataSet[i]);
        }

        latelyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lately);
        listView.setAdapter(latelyAdapter);
        /**************************************************/


        dataSet = dbF.DBselect(sEdit.getText().toString()).split("\n");
        for(int i=0; i < dataSet.length ; i++){
            if(!dataSet[i].equals("empty"))
                autoList.add(dataSet[i]);
        }

        /**************************************************/
        Collections.sort(autoList);
        autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autoList);
        sEdit.setAdapter(autoCompleteAdapter);
        /**************************************************/

        sEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();


                /**************************************************/
                if (!sEdit.getText().toString().equals(""))
                    dbF.Recentinsert(sEdit.getText().toString());
                /**************************************************/

                dataSet = dbF.DBselect(sEdit.getText().toString()).split("\n");
                for(int i=0; i < dataSet.length ; i++){
                    if(!dataSet[i].equals("empty"))
                        list.add(dataSet[i]);
                }
                if (list.size() > 0)
                {

                    /**************************************************/
                    Collections.sort(list);
                    listView.setAdapter(adapter);
                    /**************************************************/
                }
                else {
                    String errMessage = "검색 결과가 없습니다.";
                    Toast.makeText(SearchActivity.this, errMessage, Toast.LENGTH_SHORT).show();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sEdit.getWindowToken(), 0);
                //adapter.clear();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                builder.setTitle("물건 찾기")
                        .setMessage("물건 찾기를 실행 할까요?")
                        .setCancelable(false)
                        .setPositiveButton("찾기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(SearchActivity.this, "수정", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /* 길게 터치시 PopupMenu 호출 */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                popupMenu = new PopupMenu(SearchActivity.this, view);
                getMenuInflater().inflate(R.menu.select_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.setting:
                                i= new Intent(SearchActivity.this, EditActivity.class);
                                i.putExtra("Product_name", list.get(position));
                                startActivityForResult(i, 0);
                                break;
                            case R.id.search:
                                //Toast.makeText(MainActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                                builder.setTitle("Delete").setMessage("'"+list.get(position)+"'\n"+"삭제 하시겠습니까?")
                                        .setCancelable(false).setPositiveButton("삭제",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int result = dbF.DBdelete(list.get(position));
                                        dialog.cancel();
                                        if(result == 1) {
                                            /**************************************************/
                                            autoCompleteAdapter.remove(list.get(position));
                                            autoCompleteAdapter.notifyDataSetChanged();

                                            list.remove(position);
                                            adapter.notifyDataSetChanged();
                                            /**************************************************/

                                        }
                                        else
                                            Toast.makeText(SearchActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                        .setNegativeButton("취소",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                                break;
                            case R.id.print:
                                //Toast.makeText(MainActivity.this, "정보보기", Toast.LENGTH_SHORT).show();
                                i = new Intent(SearchActivity.this, InfoActivity.class);
                                i.putExtra("Product_name", list.get(position));
                                startActivityForResult(i, 0);
                                break;
                            case R.id.bookmark:
                                //Toast.makeText(MainActivity.this, "북마크", Toast.LENGTH_SHORT).show();
                                int result = dbF.BKinsert(list.get(position));
                                if(result == 1)
                                    Toast.makeText(SearchActivity.this, "북마크에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(SearchActivity.this, "이미 등록되어 있습니다.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
