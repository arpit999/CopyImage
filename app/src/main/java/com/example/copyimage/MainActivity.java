package com.example.copyimage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.os.Build.TYPE;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE_PICTURE_FROM_GALLARY = 22;
    private static final String TAG = MainActivity.class.getSimpleName();
    Button btn_copy;
    private String imagepath;
    private Uri uriImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_copy = findViewById(R.id.btn_copy);


    }

    public void CopyImage(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imagepath = Environment.getExternalStorageDirectory() + "/Images";
        uriImagePath = Uri.fromFile(new File(imagepath));
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());
        photoPickerIntent.putExtra("return-data", true);
        startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_PICTURE_FROM_GALLARY);

        Toast.makeText(this, "Hi hello", Toast.LENGTH_SHORT).show();

    }


    @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case 22:
                    Log.d("onActivityResult", "uriImagePath Gallary :" + data.getData().toString());
                    Log.d("onActivityResult", "imagePath Gallary :" + imagepath);
//                    Intent intentGallary = new Intent(this, ShareInfoActivity.class);
//                    intentGallary.putExtra(IMAGE_DATA, uriImagePath);
//                    intentGallary.putExtra(TYPE, "photo");
                    File f = new File(imagepath);
//                    if (!f.exists()) {
                        try {
                            f.createNewFile();
                            Log.d(TAG, "onActivityResult: " + getRealPathFromURI(data.getData()));
                            copyFile(new File(getRealPathFromURI(data.getData())), f);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
//                        }
                    }

//                    startActivity(intentGallary);
//                    finish();
                    Toast.makeText(this, "Copy Complete", Toast.LENGTH_SHORT).show();
                    break;


            }
        }

    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


    private String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}