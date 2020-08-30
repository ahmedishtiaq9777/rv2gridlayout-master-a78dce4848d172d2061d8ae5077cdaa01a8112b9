package com.demotxt.myapp.recyclerview.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demotxt.myapp.recyclerview.R;
import com.demotxt.myapp.recyclerview.ownmodels.CustomInternetDialog;

//import com.demotxt.myapp.recyclerview.fragment.FragmentConfirmation;
//import com.demotxt.myapp.recyclerview.ownmodels.CheckConnection;

public class Error_Screen_Activity extends AppCompatActivity {

    private Button reload;
    private int Check;
    CustomInternetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error__screen_);
        try{

            dialog=new CustomInternetDialog(Error_Screen_Activity.this);
            dialog.showCustomDialog();


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        reload = findViewById(R.id.Reload_button);

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckConnection();
                //Value check
                if (Check == 1){
                    finish();
                    Intent start = new Intent(Error_Screen_Activity.this, MainActivity2.class);
                    startActivity(start);
                   /* Fragment fragment = new HomeFragment();
                    //--p
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentcontainer,fragment)
                            .addToBackStack(null)
                            .commit();
                            */

                }
                else if (Check == 0){

                    dialog.showCustomDialog();

                    //Toast.makeText(getApplicationContext(),"No Internet",Toast.LENGTH_SHORT).show();
                    // recreate();
                }

            }
        });

    }




    public void CheckConnection(){

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null != activeNetwork){
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this,"Wifi On",Toast.LENGTH_SHORT).show();
                Check = 1;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this,"Mobile Data On",Toast.LENGTH_SHORT).show();
                Check = 1;
            }
            else{
                Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                Check = 0;
            }

        }
    }

}