package com.demotxt.myapp.recyclerview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.demotxt.myapp.recyclerview.R;
import com.squareup.picasso.Picasso;

public class Prod_Activity extends AppCompatActivity {

    private TextView tvtitle,tvdescription,tvcategory,price;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_);

        tvtitle = (TextView) findViewById(R.id.txttitle);
        tvdescription = (TextView) findViewById(R.id.txtDesc);
       // tvcategory = (TextView) findViewById(R.id.txtCat);
        img = (ImageView) findViewById(R.id.bookthumbnail);
        price= (TextView)findViewById(R.id.Price);


        // Recieve data
        Intent intent = getIntent();
        String Title = intent.getExtras().getString("Title");
        String Description = intent.getExtras().getString("Description");
        String image = intent.getExtras().getString("Thumbnail") ;
        float pRise=intent.getExtras().getFloat("price");
        String strprice=String.valueOf(pRise);

        // Setting values


        tvtitle.setText(Title);
        tvdescription.setText(Description);
        price.setText(strprice);
      //  img.setImageResource(image);
        Picasso.get().load(image).into(img);

    }
}
