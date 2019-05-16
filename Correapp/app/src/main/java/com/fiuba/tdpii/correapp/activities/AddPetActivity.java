package com.fiuba.tdpii.correapp.activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.local.PetLocal;

public class AddPetActivity  extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_pet);
//    }

    private EditText nombre;

    private String nombreStr;

    private ImageView backArrow;

    private Spinner tipo;
    private String tipoStr;

    private Spinner size;
    private String sizeStr;

    private EditText obs;
    private String obsStr;

    private Button confirmBtn;
    private Bundle bundle;
    PetLocal pet1 ;

    PetLocal pet2 ;

    PetLocal pet3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_pet);

        nombre = findViewById(R.id.nombre);


        tipo = findViewById(R.id.tipo_de_mascota);
        size = findViewById(R.id.tamaño_de_mascota);



        obs = findViewById(R.id.obs);

        confirmBtn = findViewById(R.id.confirm);
        
        bundle = getIntent().getParcelableExtra("bundle");

        pet1 = bundle.getParcelable("pet_1");

        pet2 = bundle.getParcelable("pet_2");

        pet3 = bundle.getParcelable("pet_3");

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nombre.getText() == null || nombre.getText().equals("")) {
                    Toast.makeText(AddPetActivity.this, "Te olvidaste el nombre de tu mascota" , Toast.LENGTH_LONG).show();
                    return;
                }

                if (nombre.getText().toString().isEmpty()) {
                    Toast.makeText(AddPetActivity.this, "Te olvidaste el nombre de tu mascota", Toast.LENGTH_LONG).show();
                    return;
                }

                if (tipo.getSelectedItem().toString().compareTo("Tipo de mascota") == 0) {
                    Toast.makeText(AddPetActivity.this, "Te olvidaste el tipo de tu mascota", Toast.LENGTH_LONG).show();
                    return;
                }

                if (size.getSelectedItem().toString().compareTo("Tamaño") == 0) {
                    Toast.makeText(AddPetActivity.this, "Te olvidaste el tamaño", Toast.LENGTH_LONG).show();
                    return;
                }

                if(pet1 == null){
                    pet1 = new PetLocal();
                    pet1.nombre = nombre.getText().toString();
                    pet1.obs = obs.getText().toString();

                    pet1.tipo = tipo.getSelectedItem().toString();
                    pet1.size = size.getSelectedItem().toString();

                } else {
                    if (pet2 == null){

                        pet2 = new PetLocal();
                        pet2.nombre = nombre.getText().toString();
                        pet2.obs = obs.getText().toString();
                        pet2.tipo = tipo.getSelectedItem().toString();
                        pet2.size = size.getSelectedItem().toString();


                    } else{
                        if(pet3 == null){
                            pet3 = new PetLocal();
                            pet3.nombre = nombre.getText().toString();
                            pet3.obs = obs.getText().toString();
                            pet3.tipo = tipo.getSelectedItem().toString();
                            pet3.size = size.getSelectedItem().toString();

                        } else{
                            Toast.makeText(AddPetActivity.this, "No pueden viajar mas de 3 mascotas juntas" , Toast.LENGTH_LONG).show();
                        }
                    }
                }

                Intent navigationIntent = new Intent(AddPetActivity.this, CreateTripActivity.class);

                bundle.putParcelable("pet_1", pet1 );
                bundle.putParcelable("pet_2", pet2 );
                bundle.putParcelable("pet_3", pet3 );


                navigationIntent.putExtra("bundle", bundle );
                startActivity(navigationIntent);


                System.out.print(obs);
            }
        });
    }





}
