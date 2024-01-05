package com.example.productivityapp.presentacion.social.searchFriends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.User;
import com.example.productivityapp.presentacion.adapters.UserItemAdapter;
import com.example.productivityapp.presentacion.social.TimeTableAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendsFragment extends Fragment {
    public SearchFriendsFragment() { }

    private ImageButton searchBtn;
    private RecyclerView recyclerView;
    private UserItemAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_friends, container, false);

        // Find the ImageButton and EditText by their IDs
        searchBtn = root.findViewById(R.id.imageButton);
        EditText editTxNewFriend = root.findViewById(R.id.editTxNewFriend);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the text from the EditText
                String searchText = editTxNewFriend.getText().toString();

                // Find the user in the database
                User foundUser = AppDatabase.getDatabase().getUserDAO().getUserByEmail(searchText);

                // If the user exists, show a dialog
                if (foundUser.getUserId().equals("NULL")) {
                    showUserNotFoundDialog();
                } else if(foundUser.getUserId().equals("ITSELF")){
                    showUserItselfDialog();
                } else if(foundUser.getUserId().equals("FRIEND")){
                    showUserAlreadyFriendDialog();
                } else {
                    Log.d("User not found", "USER NOT FOUND");
                    showUserFoundDialog(foundUser);
                }
            }
        });

        return root;
    }

    // Function to show the dialog when a user is found
    private void showUserFoundDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Usuario encontrado");
        builder.setMessage("Quieres enviarle peticion de amistad a: " + user.getEmail());

        // Set the positive button (Send Request)
        builder.setPositiveButton("Enviar petición", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the "Send request" button click
                // You can add your code here to perform actions when the button is clicked
                // For example, you can send a friend request to the user
            }
        });


        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUserNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Usuario no encontrado");
        builder.setMessage("El correo ingresado no forma parte del sistema");
        builder.setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUserItselfDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error en la busqueda");
        builder.setMessage("El correo ingresado es el suyo");
        builder.setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showUserAlreadyFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error en la busqueda");
        builder.setMessage("El usuario encontrado ya es su amigo");
        builder.setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}