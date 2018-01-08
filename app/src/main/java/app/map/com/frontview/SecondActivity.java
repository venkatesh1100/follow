package app.map.com.frontview;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SecondActivity extends Activity {
    DatabaseReference database;
    String status="";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<List_View>cycler;
    private  int counter=0;

    private List<String>li=new ArrayList<>();


    private Map<String,String>contacts=new HashMap<String, String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.chatpage);
        database= FirebaseDatabase.getInstance().getReference("MapBeans");


        //recycler view
        recyclerView=(RecyclerView)findViewById(R.id.recyclecontact);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cycler=new ArrayList<>();

        //contact
        ContentResolver resolver=getContentResolver();
        Cursor cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        String check="";

        while(cursor.moveToNext())
        {
            String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor ph=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"= ?",new String[]{id},null);
            while(ph.moveToNext())
            {
                String phonenumber=ph.getString(ph.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String dis=ph.getString(ph.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if(phonenumber.substring(0,1).equals("9")||phonenumber.substring(0,1).equals("8")||phonenumber.substring(0,1).equals("7")||phonenumber.substring(0,1).equals("+"))
                {
                    phonenumber=filter(phonenumber);
                    if(!check.equals(phonenumber)) {
                        check=phonenumber;
                        if(!phonenumber.substring(0,3).equals("+91"))
                             phonenumber="+91"+phonenumber;
                             contacts.put(phonenumber,dis);
                             counter++;
                    }
                }
            }
        }
         for(Map.Entry m:contacts.entrySet())
        {


            List_View list=new List_View(m.getValue().toString(),m.getKey().toString(),status);
            cycler.add(list);

        }

        adapter=new MyAdapter(cycler,this);
        recyclerView.setAdapter(adapter);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    MapBeans ob=data.getValue(MapBeans.class);
                    if(contacts.containsKey(ob.getPh())&&ob.getCheck().equals("online"))
                    {
                        Log.d("2",ob.getPh()+" "+ob.getCheck());
                        li.add(ob.getPh().toString());


                    }

                }

                 call(li);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }

        });



    }

        public void call(List lii)

        {
            cycler.clear();
            List_View list;
            for(Map.Entry m:contacts.entrySet())
            {


                if(lii.contains(m.getKey()))
                {

                    list=new List_View(m.getValue().toString(),m.getKey().toString(),"online");

                }
                else
                {

                    list=new List_View(m.getValue().toString(),m.getKey().toString(),"offline");


                }


                cycler.add(list);

            }

            adapter=new MyAdapter(cycler,this);
            recyclerView.setAdapter(adapter);


         }

    /*
    private void putOnDb()
    {
         String msg=text.getText().toString().trim();
        if(!TextUtils.isEmpty(msg))
        {
            String id=database.push().getKey();
            MapBean obj=new MapBean(msg);
            database.child(id).setValue(obj);
            Toast.makeText(getApplicationContext(),"Saved!!!",Toast.LENGTH_LONG).show();
        }
         else
            Toast.makeText(this,"Please Enter something!!!",Toast.LENGTH_LONG).show();
    }

    public void chat(View view)
    {

        m=text.getText().toString();
        Intent intent=new Intent(view.getContext(),MapsActivity.class);
        intent.putExtra("KEY",m);
        startActivity(intent);

    }

    */

    public String filter(String filterstr)
    {
        String phone = "";
        String[] output = filterstr.split("\\s");
         for(int i=0;i<output.length;i++)
         {
             phone+=output[i];
         }
        return phone;


    }




}
