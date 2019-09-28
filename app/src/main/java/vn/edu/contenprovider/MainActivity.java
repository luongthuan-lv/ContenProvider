package vn.edu.contenprovider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public TextView textView;
    private ListView lvList;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvList = findViewById(R.id.lvList);
        imageView = findViewById(R.id.imageView);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 888) {
            Toast.makeText(MainActivity.this, "Người dùng đã tương tác xin quyền truy cập danh bạ" + grantResults[0], Toast.LENGTH_LONG).show();
        } else if (requestCode == 889) {
            Toast.makeText(MainActivity.this, "Người dùng đã tương tác xin quyền truy cập nhật ký" + grantResults[0], Toast.LENGTH_LONG).show();
        }
    }

    public void loadContacts(View view) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    888);
        } else {
            run();
        }
    }

    public void loadContacts2(View view) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 889);
        } else {
            calllog();
        }
    }

    private void calllog() {
        String[] projection = new String[]{
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DURATION
        };
        ArrayList<String> list = new ArrayList<>();
        Cursor c = getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                CallLog.Calls.DURATION + "<?", new String[]{"30"},
                CallLog.Calls.DATE + " Asc");
        c.moveToFirst();
        String s = "";
        while (c.isAfterLast() == false) {

            Long date = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
            Timestamp now=new Timestamp(date);
            Date date1=new Date(now.getTime());
            String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
            s = date1 + " - " + number + " - " + name + " - " + " - " + duration + "\n";
            c.moveToNext();
            list.add(s);
        }
        c.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        lvList.setAdapter(adapter);
    }


    public void run() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;//hoặc sử dụng đường dẫn ContactsContacts.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String data = "";
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));


                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    data += id + " - " + name + " - " + number;

                    cursor.moveToNext();
                    list.add(data);
                }
                cursor.close();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
                lvList.setAdapter(adapter);

            }
        }


    }

    public void loadContacts3(View view) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 887);
        } else {
            getRealPathByUri(MainActivity.this,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
    }

//    private void media() {
//
//        ContentResolver contentResolver = MainActivity.this.getContentResolver();
//        String[] projection = {
//                MediaStore.Images.Media._ID
//        };
//        Uri ext_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        String where = "(" + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?)" + " and " + MediaStore.Images.Media.SIZE + ">=?";
//        ArrayList<String> list = new ArrayList<>();
//
//        Cursor c = MediaStore.Images.Media.query(
//                contentResolver,
//                ext_uri,
//                projection,
//                where,
//                new String[]{"image/jpeg", "image/png", "102400"},
//                MediaStore.Images.Media.DATE_MODIFIED + " desc"
//        );
//        //int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//        int i = 0;
//        while (c.moveToNext() && i < c.getCount()) {
//            String picture=Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null+"").toString();
//            Log.e("url",picture);
//            File file=new File(picture);
//            if(file.exists()) {
//                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//
//                this.imageView.setImageBitmap(myBitmap);
//
//            }
//        }
//
//        c.close()
//         }

//    public void image(){
//        ContentResolver contentResolver = MainActivity.this.getContentResolver();
//        String[] projection = {
//                MediaStore.Images.Media._ID
//        };
//        Uri ext_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        String where = "(" + MediaStore.Images.Media.MIME_TYPE
//                + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?)"
//                + " and " + MediaStore.Images.Media.SIZE + ">=?";
//        ArrayList<Picture> listpic = new ArrayList<>();
//        Cursor c = MediaStore.Images.Media.query(
//                contentResolver,
//                ext_uri,
//                projection,
//                where,
//                new String[] { "image/jpeg", "image/png", "102400" },
//                MediaStore.Images.Media.DATE_MODIFIED + " desc");
//        int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//        int i = 0;
//        while (c.moveToNext() && i < c.getCount()) {
//            long origId = c.getLong(columnIndex);
//            Picture picture = new Picture();
//            picture.uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, origId + "").toString();
//            listpic.add(picture);
//            i++;
//        }
//        c.close();
//        return null;
//    }

    public String getRealPathByUri(Context context, Uri uri) {
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return uri.getPath();
        }

            ContentResolver resolver = context.getContentResolver();
            String[] proj = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = MediaStore.Images.Media.query(resolver, uri, proj);
            String realPath = null;
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    realPath = cursor.getString(columnIndex);
                    File file=new File(realPath);
            if(file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                MainActivity.this.imageView.setImageBitmap(myBitmap);

            }
                }

            }
            return realPath;

    }

}



