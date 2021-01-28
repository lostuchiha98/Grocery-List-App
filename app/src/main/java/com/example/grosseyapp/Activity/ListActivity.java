package com.example.grosseyapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grosseyapp.Data.DatabaseHandler;
import com.example.grosseyapp.Model.Grocery;
import com.example.grosseyapp.R;
import com.example.grosseyapp.UI.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> groceryList,listItem;
    private DatabaseHandler db;
    private TextView item,quantity;
    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);

        recyclerView = (RecyclerView)findViewById(R.id.recycleviewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db=new DatabaseHandler(this);

        FloatingActionButton fab = findViewById(R.id.AddItemID);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ListActivity.this);
                dialog.setContentView(R.layout.popup);
                dialog.show();

                item=(EditText) dialog.findViewById(R.id.item);
                quantity=(EditText) dialog.findViewById(R.id.Quantity);
                save=(Button) dialog.findViewById(R.id.save);


                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.getText().toString().length()!=0 && quantity.getText().length()!=0){
                            Grocery addgrocery=new Grocery();

                            addgrocery.setName(item.getText().toString());
                            addgrocery.setQuantity(quantity.getText().toString());

                            db.AddGrocery(addgrocery);

                            groceryList=new ArrayList<>();
                            listItem=new ArrayList<>();

                            groceryList=db.getAllGroceries();

                            for (Grocery g :groceryList){
                                Grocery groceryAdd=new  Grocery();
                                groceryAdd.setName(g.getName());
                                groceryAdd.setQuantity("Qty : "+g.getQuantity());
                                groceryAdd.setDateItemAdded("Added On : "+g.getDateItemAdded());

                                listItem.add(groceryAdd);
                            }
                            recyclerViewAdapter = new RecyclerViewAdapter(ListActivity.this,listItem);
                            recyclerView.setAdapter(recyclerViewAdapter);
                            recyclerViewAdapter.notifyDataSetChanged();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            },0);
                        }
                        ListActivity.this.recreate();
                    }
                });
            }
        });





        groceryList=new ArrayList<>();
        listItem=new ArrayList<>();

        groceryList=db.getAllGroceries();


        for(Grocery g : groceryList){
//            Log.d("Qty",g.getQuantity());
            Grocery grocery=new Grocery();
            grocery.setId(g.getId());
            grocery.setName(g.getName());
            grocery.setQuantity("Qty : "+ g.getQuantity());
            grocery.setDateItemAdded("Added On : "+ g.getDateItemAdded());

            listItem.add(grocery);
        }
        if(listItem.size()==0) ListActivity.this.finish();
        recyclerViewAdapter = new RecyclerViewAdapter(this,listItem);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}