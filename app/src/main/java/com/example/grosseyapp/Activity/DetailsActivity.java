package com.example.grosseyapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.grosseyapp.Data.DatabaseHandler;
import com.example.grosseyapp.Model.Grocery;
import com.example.grosseyapp.R;
import com.example.grosseyapp.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private TextView ItemName,Quantity,DateAdded;
    private Button Edit,Delete,Update,yes,no;
    private EditText updateItem,updateQuantity;
    private List<Grocery> listItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ItemName=(TextView) findViewById(R.id.ItemNameDetails);
        Quantity=(TextView)findViewById(R.id.QuantityDetails);
        DateAdded=(TextView)findViewById(R.id.DateAddedDetails);

        listItem=new ArrayList<>();

        Bundle bundle=getIntent().getExtras();

        final int adapterPos=bundle.getInt("pos");
        final int idPos=(bundle.getInt("id"));

        if(bundle!=null){
            ItemName.setText(bundle.getString("name"));
            Quantity.setText(bundle.getString("Qty"));
            DateAdded.setText(bundle.getString("DateAdded"));
        }

        final DatabaseHandler db=new DatabaseHandler(DetailsActivity.this);

        Edit=(Button)findViewById(R.id.EditButtonDetails);
        Delete=(Button)findViewById(R.id.DeleteButtonDetails);

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(DetailsActivity.this);
                dialog.setContentView(R.layout.editresource);
                dialog.show();
                updateItem=(EditText)dialog.findViewById(R.id.item);
                updateQuantity=(EditText)dialog.findViewById(R.id.Quantity);
                Update=(Button)dialog.findViewById(R.id.save);


                Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Grocery updategrocery=new Grocery();
                        if(updateItem.getText().length()!=0 && updateQuantity.getText().length()!=0){
                        updategrocery.setName(updateItem.getText().toString());
                        updategrocery.setQuantity(updateQuantity.getText().toString());
                        updategrocery.setId(idPos);

                        db.updateGrocery(updategrocery);

//                        Log.d("name",db.getGrocery(idPos).getName());
                        ItemName.setText(db.getGrocery(idPos).getName());
                        Quantity.setText("Qty :"+db.getGrocery(idPos).getQuantity());
                        DateAdded.setText("Added On: "+db.getGrocery(idPos).getDateItemAdded());
                        dialog.dismiss();
                        Intent refresh = new Intent(DetailsActivity.this, ListActivity.class);
                        startActivity(refresh);
                        DetailsActivity.this.finish();
                        }
                    }
                });
            }
        });


        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(DetailsActivity.this);
                dialog.setContentView(R.layout.deleteconfirmation);
                dialog.show();
                yes=(Button) dialog.findViewById(R.id.yes);
                no=(Button) dialog.findViewById(R.id.no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.deleteGrocery(idPos);

                        listItem=db.getAllGroceries();
                        if(listItem.size()==0){
                            Intent refresh = new Intent(DetailsActivity.this, MainActivity.class);
                            startActivity(refresh);
                            DetailsActivity.this.finish();
                        }
                        else {
                            Intent refresh = new Intent(DetailsActivity.this, ListActivity.class);
                            startActivity(refresh);
                            DetailsActivity.this.finish();
                        }
                        dialog.dismiss();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}