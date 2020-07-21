package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    ListView listView ;
    ArrayList<String> StoreName ;
    ArrayList<String> StoreNumber ;

    ArrayAdapter<String> arrayAdapter ;
    Cursor cursor ;
    String name, phonenumber ;
    public  static final int RequestPermissionCode  = 1 ;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        StoreName = new ArrayList<String >();
        StoreNumber = new ArrayList<String >();

        listView = (ListView)findViewById(R.id.listview1);
        EnableRuntimePermission();

        GetContactsIntoArrayList();

        MyAdapter adapter = new MyAdapter(this,StoreName,StoreNumber);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

//        button = (Button)findViewById(R.id.button1);
//
//        StoreContacts = new ArrayList<String>();
//
//        EnableRuntimePermission();
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                GetContactsIntoArrayList();
//
//                arrayAdapter = new ArrayAdapter<String>(
//                        MainActivity.this,
//                        R.layout.contact_items_listview,
//                        R.id.textView1, StoreContacts
//                );
//
//                listView.setAdapter(arrayAdapter);
//
//
//            }
//        });

    }

    public void GetContactsIntoArrayList(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC" );

        String nameCheck = " ",numberCheck = " ";
        while (cursor.moveToNext()) {



            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            if(!(nameCheck.equals(name) && numberCheck.replaceAll("\\s+","").equals(phonenumber))){
                StoreName.add(name+" ");
                StoreNumber.add(phonenumber+" ");
            }
            nameCheck = name;
            numberCheck = phonenumber;
        }

        cursor.close();

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(MainActivity.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    class MyAdapter extends ArrayAdapter{

        Context mContext;
        ArrayList<String> rName;
        ArrayList<String> rNumber;

        MyAdapter(Context c,ArrayList<String >name,ArrayList<String>number){
            super(c,R.layout.contact_items_listview,R.id.textView1,name);
            this.mContext = c;
            this.rName = name;
            this.rNumber = number;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.contact_items_listview,parent,false);
            TextView myname = row.findViewById(R.id.textView1);
            TextView mynumber = row.findViewById(R.id.textView2);

            myname.setText(rName.get(position));
            mynumber.setText(rNumber.get(position));




            return row;
        }
    }


}