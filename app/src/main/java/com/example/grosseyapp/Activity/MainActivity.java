package com.example.grosseyapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grosseyapp.Data.DatabaseHandler;
import com.example.grosseyapp.Model.Grocery;
import com.example.grosseyapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private EditText item,quantity;
    private Button save;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DatabaseHandler(this);
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 popupDialogue();

            }
        });



        if(db.getGroceryCount()>0){
            Intent intent=new Intent(MainActivity.this,ListActivity.class);
            startActivity(intent);
        }
    }


    public void popupDialogue(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup);
        item=(EditText) dialog.findViewById(R.id.item);
        quantity=(EditText) dialog.findViewById(R.id.Quantity);
        save=(Button) dialog.findViewById(R.id.save);
        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item.getText().toString().length()!=0 && quantity.getText().length()!=0){
                    Grocery grocery=new Grocery();
                    grocery.setName(item.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    db.AddGrocery(grocery);

                    Toast.makeText(getApplicationContext(),"Saved!",Toast.LENGTH_SHORT).show();
//                    Log.d("Item added :",String.valueOf(db.getGroceryCount()));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            startActivity(new Intent(MainActivity.this,ListActivity.class));
                        }
                    },500);
                }
            }
        });
    }
}