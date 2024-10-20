package com.example.tp1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyContactRecyclerAdapter extends RecyclerView.Adapter<MyContactRecyclerAdapter.MyViewHolder> {
    Context con;
    ArrayList<Contact> data;
    ContactManager manager;

    public MyContactRecyclerAdapter(Context con, ArrayList<Contact> data, ContactManager manager) {
        this.con = con;
        this.data = data;
        this.manager = manager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // creation des view holders selon la taille de l'ecran + 2


        //creation d'un view
        LayoutInflater inflater =LayoutInflater.from(con);
        View v= inflater.inflate(R.layout.view_contact,null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contact c = data.get(position);
        holder.tvnom.setText(c.nom);
        holder.tvpseudo.setText(c.pseudo);
        holder.tvnum.setText(c.numero);

        // Set click listeners for the call, delete, and edit icons

        //set the clicklistner for the phone call
        holder.callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = holder.getAdapterPosition();//indice de l'element selectionné
                Contact c = data.get(index);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + c.numero));
                con.startActivity(intent);
            }
        });

        //set the click listner to delete contact
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = holder.getAdapterPosition(); //indice de l'element selectionné
                Contact c = data.get(index);
                data.remove(index);
                manager.supprimer(c.numero,c.pseudo,c.nom);
                Toast.makeText(con, "Contact deleted", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();

            }
        });


        // Edit contact
        holder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(con);
                alert.setTitle("Edit Contact");
                alert.setMessage("Are you sure you want to edit this contact?");

                LayoutInflater inflater = LayoutInflater.from(con);
                View vd = inflater.inflate(R.layout.view_dialog, null);
                alert.setView(vd);

                // Recuperation of the inputs and the buttons
                EditText editPseudoName = vd.findViewById(R.id.dialog_edit_pseudoname);
                EditText editPhone = vd.findViewById(R.id.dialog_edit_phone);
                Button saveEdit = vd.findViewById(R.id.dialog_edit_save);
                Button cancelEdit = vd.findViewById(R.id.dialog_edit_cancel);

                editPseudoName.setText(c.pseudo);
                editPhone.setText(c.numero);

                // Create and show the dialog
                AlertDialog dialog = alert.create();
                dialog.show();

                // Save edit
                saveEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newPseudoName = editPseudoName.getText().toString();
                        String newPhone = editPhone.getText().toString();

                        // Update contact details
                        c.pseudo = newPseudoName;
                        c.numero = newPhone;
                        manager.modifier(c.nom, newPseudoName, newPhone);
                        notifyDataSetChanged(); // Update the list
                        Toast.makeText(con, "Contact updated", Toast.LENGTH_SHORT).show();

                        dialog.dismiss(); // Close the dialog after saving
                    }
                });

                // Cancel edit
                cancelEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); // Close the dialog when canceled
                    }
                });
            }
        });    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvnom;
        TextView tvpseudo;
        TextView tvnum;
        ImageView callIcon,deleteIcon,editIcon;
        public MyViewHolder(@NonNull View v) {
            super(v);
            this.tvnom = v.findViewById(R.id.tv_nom_contact);
            this.tvpseudo = v.findViewById(R.id.tv_pseudo_contact);
            this.tvnum = v.findViewById(R.id.tv_num_contact);
            this.callIcon = v.findViewById(R.id.imgview_call_contact); // Assuming this is the ID of your call icon
            this.editIcon = v.findViewById(R.id.imgview_edit_contact);
            this.deleteIcon = v.findViewById(R.id.imgview_delete_contact);
        }

    }
}
