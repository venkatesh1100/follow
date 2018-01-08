package app.map.com.frontview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;

import java.util.concurrent.TimeUnit;

import static android.R.attr.phoneNumber;

/**
 * Created by venkatesh jose on 6/21/2017.
 */

public class First extends AppCompatActivity {
Button b1;
    EditText t1,t2;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        t1 = (EditText) findViewById(R.id.edittext1);
        t2 = (EditText) findViewById(R.id.edittext2);
        b1 = (Button) findViewById(R.id.firstbtn);

    }
      /* SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }

*/





    void tomain(View v)
    {
       String name=t1.getText().toString();
        String ph1=t2.getText().toString();


Intent intent=new Intent(First.this,MapsActivity.class);
intent.putExtra("name",name);
        intent.putExtra("phn",ph1);
        startActivity(intent);
    }


}
