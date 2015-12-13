package com.hsue.sue.pagelayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.provider.MediaStore;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import java.io.File;


public class RegisterActivity extends Activity {


    Button main_button, search_button, register_button;

    EditText product_Name, Serial_Num,Memo;
    Button insert;
    Toast toast;


    private static final String TAG = "MainActivity";

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    DBFunction dbF;

    private Uri mImageCaptureUri;
    private ImageView mPhotoImageView;
    private Button mButton;
    private Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        main_button = (Button)findViewById(R.id.main);
        search_button = (Button)findViewById(R.id.search);
        register_button = (Button)findViewById(R.id.register);

        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(RegisterActivity.this, MainActivity.class);
                startActivityForResult(i,0);
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(RegisterActivity.this, SearchActivity.class);
                startActivityForResult(i,0);
            }
        });

/////////////////////////////////////////////////////////////////////////////////////////
        product_Name = (EditText) findViewById(R.id.editText);
        //Folder_Name = (EditText) Array
        Serial_Num = (EditText) findViewById(R.id.editText2);
        Memo = (EditText) findViewById(R.id.editText4);
        mButton = (Button) findViewById(R.id.imageBtn);
        mPhotoImageView = (ImageView) findViewById(R.id.image);

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        doTakePhotoAction();
                    }
                };

                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        doTakeAlbumAction();
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });


        ///////////////////////////////////////////////
   //Spinner
        final Spinner folderSpinner = (Spinner) findViewById(R.id.spinner_folder);
        ArrayAdapter folderAdapter = ArrayAdapter.createFromResource(this, R.array.folder_name,
                android.R.layout.simple_spinner_item);
        folderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        folderSpinner.setAdapter(folderAdapter);


        //save button
        mButton2 =(Button) findViewById(R.id.saveBtn);
        // tResult = (TextView)findViewById(R.id.textView3);
        // findViewById(R.id.saveBtn).setOnClickListener((myButtonClick));


        mButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dbF = new DBFunction(RegisterActivity.this);
                String getFolderName = folderSpinner.getSelectedItem().toString();
                String getEdit = product_Name.getText().toString();
                String getSerial = Serial_Num.getText().toString();
                String getMemo = Memo.getText().toString();
                getEdit = getEdit.trim();
                if (getEdit.getBytes().length <= 0) {
                    Toast.makeText(getApplicationContext(), "값을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    dbF.DBinsert(getEdit,getFolderName,getSerial,getMemo);
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent i  = new Intent(RegisterActivity.this, RegisterActivity.class);
                    startActivityForResult(i,0);
                }
            }
        });

        // Button saveBtn = (Button) findViewById(R.id.saveBtn);
        //saveBtn.setOnClickListener(myButtonClick()) {
        //   EditText product_Name = (EditText) findViewById(R.id.editText);
        //  EditText Serial_Num = (EditText) findViewById(R.id.editText2);
            /*String p_Name = product_Name.getText().toString();
            p_Name= p_Name.trim();
            String Serial_N;
            String getEdit
            getEdit = getEdit.trim();
*/
        // }else{
        //
        // }
        // if (Serial_N.length())
            /*
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), "Inf Saved", Toast.LENGTH_SHORT).show();

                if (p_Name.length() > 0)

                {
                    p_Name = product_Name.getText().toString();
                    Serial_N = Serial_Num.getText().toString();
                }
            }
            */
    };




    /**
     * 카메라에서 이미지 가져오기
     */
    private void doTakePhotoAction()
    {
    /*
     * 참고 해볼곳
     * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
     * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
     * http://www.damonkohler.com/2009/02/android-recipes.html
     * http://www.firstclown.us/tag/android/
     */

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
        //intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void doTakeAlbumAction()
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK)
        {
            return;
        }

        switch(requestCode)
        {
            case CROP_FROM_CAMERA:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    mPhotoImageView.setImageBitmap(photo);
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                {
                    f.delete();
                }

                break;
            }

            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();
            }

            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 90);
                intent.putExtra("outputY", 90);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }
        }
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
