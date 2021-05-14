package com.example.chetana.kitchenmantra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPreparation extends AppCompatActivity {
    private Context parent;
    Button btnsavpreptn;
    EditText edtprep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preparation);

        getSupportActionBar().setTitle(" Add Preparation ");

        init();

        setListener();
    }

    public void init(){
        parent = AddPreparation.this;

        edtprep = (EditText)findViewById(R.id.edtprep);
        btnsavpreptn = (Button)findViewById(R.id.btnsavpreptn);

    }

    public void setListener(){

        btnsavpreptn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                String preparation = edtprep.getText().toString();
                i.putExtra("Preparation", preparation);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

    }
}
