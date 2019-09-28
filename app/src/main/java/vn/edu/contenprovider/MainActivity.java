package vn.edu.contenprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public TextView textView;
    private ListView lvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvList = findViewById(R.id.lvList);

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
            for (int i = 0; i < c.getColumnCount(); i++) {
                s += c.getString(i) + " - ";
            }
            s += "\n";
            c.moveToNext();
            list.add(s);
        }
        c.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        lvList.setAdapter(adapter);
    }


    public void run() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;//hoặc sử dụng đường dẫn ContactsContacts.CONTENT_URI;
//        String[] projection={
//                ContactsContract.CommonDataKinds.Phone._ID,
//        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                ContactsContract.CommonDataKinds.Phone.NUMBER
//        };
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String data = "";
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    //String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    data =  name + "-" + number + "\n";

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
            media();
        }
    }

    private void media() {
        String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.MIME_TYPE
        };
        ArrayList<String> list = new ArrayList<>();
        CursorLoader loader = new CursorLoader
                (this, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, null);
        Cursor c = loader.loadInBackground();
        c.moveToFirst();
        String media = "";
        while (!c.isAfterLast()) {
            for (int i = 0; i < c.getColumnCount(); i++) {
                media += c.getString(i) + " - ";
            }
            media += "\n";
            c.moveToNext();
            list.add(media);
        }
        c.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        lvList.setAdapter(adapter);

    }


}



