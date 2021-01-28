package com.example.grosseyapp.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.grosseyapp.Activity.DetailsActivity;
import com.example.grosseyapp.Activity.ListActivity;
import com.example.grosseyapp.Activity.MainActivity;
import com.example.grosseyapp.Data.DatabaseHandler;
import com.example.grosseyapp.Model.Grocery;
import com.example.grosseyapp.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private Grocery grocery;
    private List<Grocery> groceryItem;
    private Button yes,no,update;
    private EditText updateItem,updateQuantity;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryItem) {
        this.context = context;
        this.groceryItem = groceryItem;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            Grocery grocery=groceryItem.get(position);
            
            holder.ItemName.setText(grocery.getName());
            holder.quantity.setText(grocery.getQuantity());
            holder.dateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return groceryItem.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ItemName,quantity,dateAdded;
        public Button Edit,Delete;
        public ViewHolder(View view,Context ctx) {
            super(view);
            context=ctx;

            ItemName =(TextView) view.findViewById(R.id.ItemID);
            quantity = (TextView) view.findViewById(R.id.Quantity);
            dateAdded=(TextView) view.findViewById(R.id.date);

            Edit=(Button) view.findViewById(R.id.Edit);
            Delete=(Button) view.findViewById(R.id.Delete);

            Edit.setOnClickListener(this);
            Delete.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    Grocery grocery= groceryItem.get(pos);
                    Intent intent=new Intent(context, DetailsActivity.class);
                    intent.putExtra("name",grocery.getName());
                    intent.putExtra("Qty",grocery.getQuantity());
                    intent.putExtra("DateAdded",grocery.getDateItemAdded());
                    intent.putExtra("id",grocery.getId());
                    intent.putExtra("pos",getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            Grocery grocery= groceryItem.get(pos);
                switch (v.getId()){
                    case R.id.Edit :
                        UpdateItem(grocery,pos);
                        break;
                    case R.id.Delete :
                        DeleteItem(grocery.getId(),pos);
                        break;
                }
        }
    }

    void UpdateItem(final Grocery grocery,final int pos){



        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.editresource);
        dialog.show();


        updateItem=(EditText) dialog.findViewById(R.id.item);
        updateQuantity=(EditText) dialog.findViewById(R.id.Quantity);
        update=(Button) dialog.findViewById(R.id.save);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseHandler db =new DatabaseHandler(context);
                grocery.setName(updateItem.getText().toString());
                grocery.setQuantity(updateQuantity.getText().toString());

                if(grocery.getName().length()!=0 && grocery.getQuantity().length()!=0){
                    db.updateGrocery(grocery);
                    notifyItemChanged(pos,grocery);
                }
                dialog.dismiss();
            }
        });


    }

    void DeleteItem(final int id,final int pos){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.deleteconfirmation);
        dialog.show();
        yes=(Button)dialog.findViewById(R.id.yes);
        no=(Button) dialog.findViewById(R.id.no);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db=new DatabaseHandler(context);
                db.deleteGrocery(id);
                groceryItem.remove(pos);
                notifyItemRemoved(pos);
                dialog.dismiss();
                if(groceryItem.size()==0) {
                    Intent intent=new Intent(context,MainActivity.class);
                    context.startActivity(intent);
                }
            }
        });


    }

}
