package com.demotxt.myapp.Quickmart.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.demotxt.myapp.Quickmart.R;
import com.demotxt.myapp.Quickmart.ownmodels.DetailModel;
import com.demotxt.myapp.Quickmart.ownmodels.SignInModel;
import com.demotxt.myapp.Quickmart.ownmodels.StringResponceFromWeb;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.demotxt.myapp.Quickmart.activity.MainActivity2.hostinglink;


public class Login extends AppCompatActivity {
    TextView signup, ForgotPass;
    Button signin;
    private EditText phone, pass;
    AwesomeValidation awesomeValidation;
    String ph, password;
    private SharedPreferences rememberMepref, loginpref;
    private SharedPreferences.Editor rememberMePrefsEditor, loginprefeditor;
    private CheckBox checkBoxremember;
    private Boolean saveLogin;
    private SharedPreferences cartlistpref;
    private SharedPreferences.Editor cartlistprefeditor;
    private int proid;
    public Set<String> cartids;
    StringResponceFromWeb result;
    View layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogin);
        result = new StringResponceFromWeb();
        awesomeValidation = new AwesomeValidation(BASIC);
        awesomeValidation.addValidation(Login.this, R.id.PH, "^0(?=3)[0-9]{10}$", R.string.error_contact);
        awesomeValidation.addValidation(Login.this, R.id.PASS, RegexTemplate.NOT_EMPTY, R.string.error_password);


        LayoutInflater inflater = getLayoutInflater();
        try {
            layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_root));//for product added :to make custom toast with tick mark

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("Loginactivity", "error" + e.getMessage());

        }


        cartids = new HashSet<>();

        signin = (Button) findViewById(R.id.signin1);
        signup = (TextView) findViewById(R.id.signup);
        checkBoxremember = findViewById(R.id.checkboxremember);

        phone = (EditText) findViewById(R.id.PH);
        pass = (EditText) findViewById(R.id.PASS);
        ForgotPass = findViewById(R.id.forgotpass);

        //adding TextWatchers
        phone.addTextChangedListener(LoginTextWatcher);
        pass.addTextChangedListener(LoginTextWatcher);

        //Forget Password
        ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetIntent = new Intent(Login.this, Forgot_Pass_Activity.class);
                startActivity(forgetIntent);
            }
        });

        cartlistpref = getSharedPreferences("cartprefs", MODE_PRIVATE);//get cartpreferences that contains cartitemlist

        loginpref = getSharedPreferences("loginpref", MODE_PRIVATE);
        rememberMepref = getSharedPreferences("remembermepref", MODE_PRIVATE);

        cartlistprefeditor = cartlistpref.edit();
        rememberMePrefsEditor = rememberMepref.edit();
        loginprefeditor = loginpref.edit();

        cartids = cartlistpref.getStringSet("cartids", cartids);
        saveLogin = rememberMepref.getBoolean("saveLogin", false);

        if (saveLogin == true) {

            phone.setText(rememberMepref.getString("username", ""));
            pass.setText(rememberMepref.getString("password", ""));
            checkBoxremember.setChecked(true);

        }


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Checkcheckbox()==true) {
                    if (awesomeValidation.validate()) {

                        ph = phone.getText().toString();
                        password = pass.getText().toString();
                        SignInModel m = new SignInModel(ph, password);

                        try {

                            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            // String url = "http:// 192.168.10.13:64077/api/login";
                            //String url="https://api.myjson.com/bins/kp9wz";
                            String url = hostinglink + "/Home/login";


                            StringRequest rRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {


                                                JSONObject user = new JSONObject(response);
                                                String strResult = user.getString("result");
                                                int userid = user.getInt("userid");

                                                // response
                                                //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                                //  int size=response.toString().length();
                                                // String result=response.toString().substring(1,size-1);


                                                if (strResult.equals("success")) {

                                                    saveloginPrefference(userid);

                                                    Intent i = getIntent();
                                                    int pid = -1;
                                                    boolean login_from_profile = false;
                                                    try {
                                                        pid = Objects.requireNonNull(i.getExtras()).getInt("proid");
                                                    } catch (Exception e) {
                                                        Log.i("Came from Profile", "login through profile fragment");
                                                        Log.i("Exception", Objects.requireNonNull(e.getMessage()));
                                                    }
                                                    try {
                                                        login_from_profile = i.getExtras().getBoolean("loginfromprofile");
                                                    } catch (Exception e) {
                                                        Log.i("ProdActivity", "login through ProdActivity");
                                                        Log.i("Exception", Objects.requireNonNull(e.getMessage()));
                                                    }

                                                    if (pid != -1) {
                                                        String struserid = String.valueOf(loginpref.getInt("userid", 0));
                                                        String strpid = String.valueOf(pid);
                                                        AddToCart(struserid, strpid);
                                                        // finish();
                                                        Intent mainactivity2 = new Intent(Login.this, MainActivity2.class);
                                                        //  mainactivity2.putExtra("code", 5);
                                                        startActivity(mainactivity2);
                                                    } else if (login_from_profile) {
                                                        Intent mainactivity2 = new Intent(Login.this, MainActivity2.class);
                                                        startActivity(mainactivity2);
                                                    }

                                                    //   Intent intent = new Intent(Login.this, ShoppyProductListActivity.class);        //this is for searching ....product list
                                                    //  intent.putExtra("email", email.getText().toString());
                                                    //    intent.putExtra("password", pass.getText().toString());
                                                    //    startActivity(intent);
                                                    // getconnection("http://ahmedishtiaqbutt-001-site1.atempurl.com/Home/getproducts/");


                                                    // finish();
                                                    //  Intent intent2=new Intent(Login.this,MainActivity2.class);

                                                    //  startActivity(intent2);
                                                } else if (strResult.equals("sellersuccess")) {

                                                    finish();
                                                    Intent intent2 = new Intent(Login.this, MainActivity2.class);

                                                    startActivity(intent2);

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Error:" + response, Toast.LENGTH_SHORT).show();
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();

                                                Toast.makeText(getApplicationContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }


                                        }

                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(@NonNull VolleyError error) {
                                            // error
                                            Log.i("APIERROR", Objects.requireNonNull(error.getMessage()));
                                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("phoneNo", phone.getText().toString());
                                    params.put("password", pass.getText().toString());
                                    return params;
                                }

                                public Map<String, String> getHeaders() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("Content-Type", "application/x-www-form-urlencoded");
                                    return params;
                                }
                            };

                            requestQueue.add(rRequest);


                        } catch (Exception E) {
                            Toast.makeText(getApplicationContext(), "Error: " + E.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }


        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Login.this, Signup.class);
                try {
                    Intent i = getIntent();
                    int pid = Objects.requireNonNull(i.getExtras()).getInt("proid");
                    it.putExtra("proid", pid);
                } catch (Exception e) {

                    Log.i("Signupfromprofile", "intent in login for proid:" + e.getMessage());
                }
                startActivity(it);

            }
        });

    }
    //Text Watcher
    private final TextWatcher LoginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ph = phone.getText().toString().trim();
            password = pass.getText().toString().trim();

            signin.setEnabled(!ph.isEmpty() && !password.isEmpty());
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };



    private boolean Checkcheckbox() {
        if (checkBoxremember.isChecked()) {
            return true;
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
            builder1.setTitle("Please Agree");
            builder1.setMessage("Tick remember me!");
            builder1.setIcon(R.drawable.exclamationmarkresize);
            // builder1.show();
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return false;

        }
    }


    public void AddToCart(final String struserid, final String strpid) {
        try {

            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            // String url = "http:// 192.168.10.13:64077/api/login";
            //String url="https://api.myjson.com/bins/kp9wz";
            String url = hostinglink + "/Home/AddtoCart";

            StringRequest rRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            result = gson.fromJson(response, StringResponceFromWeb.class);
                            if (result.getresult().equals("Added")) {


                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();


                                // Toast.makeText(getApplicationContext(), "Product Added to Cart" , Toast.LENGTH_SHORT).show();


                            } else if (result.getresult().equals("AllreadyAdded")) {
                                // Toast.makeText(getApplicationContext(), "Product is Already Added", Toast.LENGTH_LONG).show();
                                // response
                                try {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                                    builder1.setTitle("Already Added");
                                    builder1.setMessage("Your product is Already Added to Cart!");

                                    builder1.setIcon(R.drawable.exclamationmarkresize);
                                    // builder1.show();
                                    AlertDialog alert11 = builder1.create();

                                    alert11.show();
                                    Thread.sleep(3000);
                                    //   Intent main=new Intent(Login.this,MainActivity2.class);
                                    // startActivity(main);

                                } catch (Exception e) {
                                    Log.i("error:", Objects.requireNonNull(e.getMessage()));
                                    Toast.makeText(getApplicationContext(), "error" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }


                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(@NonNull VolleyError error) {
                            // error
                            Log.i("APIERROR", Objects.requireNonNull(error.getMessage()));
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("productId", strpid);
                    params.put("userId", struserid);

                    return params;
                }

                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };

            requestQueue.add(rRequest);


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


/*
    public void AddToCart(int id) {
        String strid = String.valueOf(id);
        cartids.add(strid);
        cartlistprefeditor.remove("cartids");
        cartlistprefeditor.commit();
        cartlistprefeditor.putStringSet("cartids", cartids);
        cartlistprefeditor.commit();
    }*/

    public void saveloginPrefference(int uid) {

        loginprefeditor.putBoolean("loggedin", true);
        loginprefeditor.putInt("userid", uid);
        loginprefeditor.commit();
        if (checkBoxremember.isChecked()) {
            rememberMePrefsEditor.putString("username", phone.getText().toString());
            rememberMePrefsEditor.putString("password", pass.getText().toString());
            rememberMePrefsEditor.commit();

            DetailModel model = new DetailModel(getApplicationContext());
            model.setYourPhone(phone.getText().toString());

        } else {
            rememberMePrefsEditor.clear();
            rememberMePrefsEditor.commit();
        }
    }

}
