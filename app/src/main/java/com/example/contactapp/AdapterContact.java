package com.example.contactapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

import java.util.ArrayList;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ContactViewHolder> {

    private final Context context;
    private final ArrayList<ModelContact> contactList;
    private final DbHelper dbHelper;

    // Ajouter le constructeur
    public AdapterContact(Context context, ArrayList<ModelContact> contactList) {
        this.context = context;
        this.contactList = contactList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_contact_item, parent, false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        ModelContact modelContact = contactList.get(position);

        // Obtenir les données
        String id = modelContact.getId();
        String image = modelContact.getImage();
        String name = modelContact.getName();
        String phone = modelContact.getPhone();
        String email = modelContact.getEmail();
        String note = modelContact.getNote();

        // Définir les données dans la vue
        holder.contactName.setText(name);
        holder.contactPhone.setText(phone);
        if (image.equals("null")) {
            holder.contactImage.setImageResource(R.drawable.ic_baseline_person_24);
        } else {
            holder.contactImage.setImageURI(Uri.parse(image));
        }

        // Gérer le clic pour lancer l'application d'appel
        holder.contactDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phone));
                context.startActivity(dialIntent);
            }
        });

        // Gérer le clic pour lancer l'application de messagerie

        holder.contactMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent pour l'application de messagerie avec le numéro de téléphone du contact
                Intent messageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone));
                context.startActivity(messageIntent);
            }
        });


        // Gérer le clic sur l'élément pour afficher les détails du contact
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une intention pour passer à l'activité ContactDetails avec l'ID du contact en tant que référence
                Intent intent = new Intent(context, ContactDetails.class);
                intent.putExtra("contactId", id);
                context.startActivity(intent); // maintenant récupérer les données de l'activité de détails
            }
        });

        // Gérer le clic sur le bouton d'édition
        holder.contactEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une intention pour passer à AddEditActivity pour mettre à jour les données
                Intent intent = new Intent(context, AddEditContact.class);
                // Passez la valeur de la position actuelle
                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("PHONE", phone);
                intent.putExtra("EMAIL", email);
                intent.putExtra("NOTE", note);
                intent.putExtra("IMAGE", image);

                // Passez un booléen pour définir s'il s'agit d'un mode d'édition
                intent.putExtra("isEditMode", true);

                // Commencer l'intention
                context.startActivity(intent);
            }
        });

        // Gérer le clic sur le bouton de suppression
        holder.contactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nous avons besoin de la référence de la classe d'aide à la base de données
                dbHelper.deleteContact(id);

                // Rafraîchir les données en appelant la méthode onResume de MainActivity
                ((MainActivity) context).onResume();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        // Vue pour row_contact_item
        ImageView contactImage, contactDial, contactMess;
        TextView contactName, contactEdit, contactDelete, contactPhone;
        RelativeLayout relativeLayout;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialiser la vue
            contactImage = itemView.findViewById(R.id.contact_image);
            contactDial = itemView.findViewById(R.id.contact_number_dial);
            contactMess = itemView.findViewById(R.id.contact_message_dial);
            contactName = itemView.findViewById(R.id.contact_name);
            contactPhone = itemView.findViewById(R.id.contact_phone);
            contactDelete = itemView.findViewById(R.id.contact_delete);
            contactEdit = itemView.findViewById(R.id.contact_edit);
            relativeLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
