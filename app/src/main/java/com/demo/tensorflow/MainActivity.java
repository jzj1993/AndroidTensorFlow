package com.demo.tensorflow;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int REQUEST_CODE_SHOW_ALBUM = 1;

    private TensorFlowModel mModel;
    private TextView mTextView;
    private ImageView mImageView;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);

        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.text);
        mImageView = findViewById(R.id.image);
        mButton = findViewById(R.id.button);

        mModel = new TensorFlowModel(getAssets());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlbum(REQUEST_CODE_SHOW_ALBUM);
            }
        });
    }

    public final void showAlbum(int requestCode) {
        try {
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            onError("系统相册不可用");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CODE_SHOW_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    if (intent != null && intent.getData() != null) {
                        onSuccess(intent.getData());
                    } else {
                        onError("选择图片失败");
                    }
                }
                break;
        }
    }

    private void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void onSuccess(Uri uri) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(parseFilePath(uri));
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, TensorFlowModel.WIDTH, TensorFlowModel.HEIGHT, false);
            bitmap.recycle();

            mImageView.setImageBitmap(scaledBitmap);

            float[] output = mModel.classifyImage(scaledBitmap);
            StringBuilder s = new StringBuilder();

            if(output[0]>output[1])
                s.append("分类结果: ").append("坏的");
            else
                s.append("分类结果: ").append("好的");
            //for (float r : output) {
                //s.append("result: ").append(r).append("\n");
            //}

            mTextView.setText(s);
        } catch (Exception e) {
            mTextView.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    private String parseFilePath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
