   package com.c.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;


import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

   public class MainActivity extends AppCompatActivity {

       private FirebaseAuth mAuth;
       private DatabaseReference miCliente;
       private DatabaseReference pagos;
       private DatabaseReference miImagen;
       private  DatabaseReference Balance;
       Integer resultadoImagen1= 1;

       Uri imagenUri1 =null;
       Uri imagenUri2 = null;
       private EditText nombre, cedula, telefono, email,ocupacion;
       private ImageView imagen1;
       private FirebaseStorage storage;
       private StorageReference storageReference;
       Uri imagenUrl1, imagenUrl2;
      private String LaFoto;
       Calendar calendar;
       String currentDate;
       private Spinner spinner;
       String idenfiticador;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            idenfiticador= extras.getString("identificador");
            LaFoto=extras.getString("Imagen");
        }else {
            Intent intent=new Intent(getApplicationContext(), MainActivity5.class);
            startActivity(intent);
        }

        nombre= findViewById(R.id.nombre);
        cedula= findViewById(R.id.cedula);
        telefono= findViewById(R.id.telefono);
        email= findViewById(R.id.email);
        ocupacion= findViewById(R.id.ocupacionTexto);
        imagen1= findViewById(R.id.imageView1);
        storage=  FirebaseStorage.getInstance();
        storageReference= storage.getReference();
        calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());



        imagen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, resultadoImagen1);
            }
        });

spinner = findViewById(R.id.spinner);
ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.tipos, android.R.layout.simple_spinner_item);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spinner.setAdapter(adapter);



        // ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

       @Override
       public void onStart() {
           super.onStart();
           // Check if user is signed in (non-null) and update UI accordingly.
           FirebaseUser currentUser = mAuth.getCurrentUser();
           if(currentUser != null){

           }else {

           }
       }





       @Override
       protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
           super.onActivityResult(requestCode, resultCode, data);
            if (requestCode==resultadoImagen1 && resultCode==RESULT_OK && data != null && data.getData() != null ) {
               final ProgressDialog pd= new ProgressDialog(this);
               pd.setTitle(getString(R.string.cargandoImagen));
               pd.show();
               imagenUri1= data.getData();
               imagen1.setImageURI(imagenUri1);

               final String randomKey= UUID.randomUUID().toString();
               StorageReference riversRef= storageReference.child("images/"+ randomKey);
               riversRef.putFile(imagenUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                       riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               imagenUrl1 = uri;
                               pd.dismiss();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(MainActivity.this, getString(R.string.problemasConUrl), Toast.LENGTH_SHORT).show();
                           }
                       });


                       ;
                       Toast.makeText(MainActivity.this, getString(R.string.laImagenSeCargo), Toast.LENGTH_SHORT).show();

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(MainActivity.this, getString(R.string.noCargoLaImagen), Toast.LENGTH_SHORT).show();
                   }
               });



           }

       }

      public void crearCliente(View view) {

          String[] yourArray = getResources().getStringArray(R.array.tipos);
        int seleccion= spinner.getSelectedItemPosition();

          if (seleccion==0)  {
              Toast.makeText(MainActivity.this,getString(R.string.tipoDeRegistro),Toast.LENGTH_SHORT).show();

            }

         else if (nombre.getText().toString().isEmpty()){
              nombre.setError(getString(R.string.introduceNombre)); }

         else if (cedula.getText().toString().isEmpty()){
              cedula.setError(getString(R.string.introduceId)); }

          else if (telefono.getText().toString().isEmpty()){
              telefono.setError(getString(R.string.introduceTelefono)); }

          else if (email.getText().toString().isEmpty()){
              email.setError(getString(R.string.introduceEmail)); }

          else if (ocupacion.getText().toString().isEmpty()){
              ocupacion.setError(getString(R.string.introduceOcupacion)); }


          else if (imagenUri1==null){
              Toast.makeText(MainActivity.this, getString(R.string.introduceImagen), Toast.LENGTH_SHORT).show(); }

          else {
              miImagen= FirebaseDatabase.getInstance().getReference();
          miCliente= FirebaseDatabase.getInstance().getReference();
          pagos= FirebaseDatabase.getInstance().getReference();
             Balance= FirebaseDatabase.getInstance().getReference();

          Map <String,Object>mapa = new HashMap<>();
          mapa.put("Id", cedula.getText().toString());
          mapa.put("Tipo", seleccion);
          mapa.put("Nombre", nombre.getText().toString());
          mapa.put("Telefono", telefono.getText().toString());
          mapa.put("Email", email.getText().toString());
          mapa.put("Imagen de Id", imagenUrl1.toString());
          mapa.put("Ocupacion", ocupacion.getText().toString());
              mapa.put("Creado", currentDate);


          miCliente.child("adms").child(idenfiticador).child("Contactos").child(cedula.getText().toString()).child("Datos").setValue(mapa).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void unused) {

                  Map <String,Object>mapaBalance = new HashMap<>();
                  mapaBalance.put("Balance", "00");

                  Balance.child("adms").child(idenfiticador).child("Contactos").child(cedula.getText().toString()).child("BALANCE").setValue(mapaBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void unused) {
                          Toast.makeText(MainActivity.this,getString(R.string.registroCorrecto),Toast.LENGTH_SHORT).show();
                          nombre.setText(null);
                          telefono.setText(null);
                          email.setText(null);
                          cedula.setText(null);
                          imagen1.setImageResource(R.drawable.foto);
                          ocupacion.setText(null);
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {

                      }
                  });


                              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(MainActivity.this,getString(R.string.registroIncompleto),Toast.LENGTH_SHORT).show();

              }
          }  );
                                                                                                                                }


      }

    public void VerClientes(View view){
        Intent intent=new Intent(getApplicationContext(), MainActivity2.class);

        intent.putExtra("identificador",idenfiticador);
        intent.putExtra("Imagen",LaFoto);
        startActivity(intent);


    }

       @Override
       public boolean onCreateOptionsMenu(Menu menu) {
           MenuInflater inflater= getMenuInflater();
           inflater.inflate(R.menu.menu_barra,menu);

           return super.onCreateOptionsMenu(menu);
       }



       @Override
       public boolean onOptionsItemSelected(@NonNull MenuItem item) {
           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        switch (item.getItemId())   {

        case R.id.verPerfil:
           Intent intent=new Intent(getApplicationContext(), MainActivity5.class);
           startActivity(intent);
           break;

            case R.id.verContactos:

                if (user != null) {
                    String uid = user.getUid();
                    Intent intento=new Intent(getApplicationContext(), MainActivity2.class);
                    intento.putExtra("identificador",uid);
                    intento.putExtra("Imagen", LaFoto);
                    startActivity(intento); }

                break;

            case R.id.salida:

                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent=new Intent(getApplicationContext(), MainActivity5.class);
                                startActivity(intent);
                            }
                        });
                break;




            default:




            }return super.onOptionsItemSelected(item);
       }
   }