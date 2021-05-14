package com.example.chetana.kitchenmantra;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chetana.kitchenmantra.Classes.BeanUser_LoginCredentials;
import com.example.chetana.kitchenmantra.Database.DatabaseHandler;
import com.example.chetana.kitchenmantra.Utilities.Utility;

public class MainActivity extends AppCompatActivity {
    private Context parent;
    EditText edtusrname,edtmobile,edtpsw;
    Button btnSignUp;

    String username, usrmobile, psw;
    DatabaseHandler dbhandler;
    Utility ut;
    BeanUser_LoginCredentials blogin;
    boolean isUserInserted;
    SQLiteDatabase sqldb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(" Login ");

        init();

        if(dbhandler.IsUserPresent()){

            Intent intent = new Intent(parent, HomeScreen.class);
            startActivity(intent);
            finish();

        }else {

        }

        setListener();
    }

    public void init(){
        parent = MainActivity.this;

        edtusrname = (EditText)findViewById(R.id.edtusrname);
        edtmobile = (EditText)findViewById(R.id.edtmobile);
        edtpsw = (EditText)findViewById(R.id.edtpsw);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        dbhandler = new DatabaseHandler(parent);
        ut = new Utility();
    }

    public void setListener(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtusrname.getText().toString();
                usrmobile = edtmobile.getText().toString();
                psw = edtpsw.getText().toString();

                blogin = new BeanUser_LoginCredentials();
                blogin.setUsername(username);
                blogin.setUsermobile(usrmobile);
                blogin.setPsw(psw);

                sqldb = dbhandler.getWritableDatabase();

                isUserInserted = dbhandler.AddUser(blogin);

                if(isUserInserted){
                    Toast.makeText(parent,"User inserted",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(parent, HomeScreen.class);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(parent,"User not inserted",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
