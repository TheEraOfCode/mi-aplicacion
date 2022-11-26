package com.c.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MainActivity4 extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ImageView documentoGrande, imagenMenu;
    TextView quienEs, queEs;
    String infoImagen,nombre,concepto,id;
    RecyclerView recyclerImagenes;
    ArrayList<Imagenes> ListaDeImagenes;
    Context ct;
    AdaptadorImagenes adaptero;
    private DatabaseReference lasImagenes;
    private String identificador, laFoto, tituloImgen, fecha;
    Integer resultadoDeImagen= 1;
    Calendar calendar;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri fotoUrl;
    private  Uri imagenFoto =null;
    private DatabaseReference miOtraFoto;
     boolean booleano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        documentoGrande= findViewById(R.id.documentoLarge);
        imagenMenu= findViewById(R.id.menuImagen);
        imagenMenu.setImageResource(R.drawable.ic_baseline_menu_24);
        quienEs =findViewById(R.id.quienEs);
        queEs=findViewById(R.id.queEs);

        storage=  FirebaseStorage.getInstance();
        storageReference= storage.getReference();

        calendar= Calendar.getInstance();
        fecha= DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

     /*   MainActivity4.requestPermissions(MainActivity4.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        MainActivity4.requestPermissions(MainActivity4.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);*/

        ListaDeImagenes= new ArrayList<Imagenes>();
        recyclerImagenes= findViewById(R.id.recyclerimagenes);
        recyclerImagenes.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerImagenes.setHasFixedSize(true);
        ct= getApplicationContext();


        Bundle extras=getIntent().getExtras();
        if (extras!=null) {
            identificador= extras.getString("identificador");
            id= extras.getString("id");
         infoImagen= extras.getString("Imagen");
            nombre= extras.getString("nombre");
           concepto= extras.getString("concepto");
           laFoto=extras.getString("laImagen");
           booleano= extras.getBoolean("booleano");

           if (booleano==true){
               recyclerImagenes.setVisibility(View.GONE);
               imagenMenu.setVisibility(View.GONE);

           }


           quienEs.setText(nombre);
           queEs.setText(concepto);
            Glide.with(getApplicationContext())
                    .load(infoImagen)
                    .placeholder(R.drawable.foto)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(documentoGrande);
            lasImagenes= FirebaseDatabase.getInstance().getReference().child("adms")
                    .child(identificador).child("Contactos").child(id).child("Imagenes");
            lasImagenes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        ListaDeImagenes.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String key= dataSnapshot.getKey().toString();
                            String DESCRIPCION= dataSnapshot.child("Descripcion").getValue().toString();
                            String IMAGEN= dataSnapshot.child("Imagen").getValue().toString();
                            String FECHA= dataSnapshot.child("fecha").getValue().toString();


                            ListaDeImagenes.add(new Imagenes(DESCRIPCION,IMAGEN,FECHA,key));

                        }
                        Collections.reverse(ListaDeImagenes);
                        adaptero.notifyDataSetChanged();

                    }
                    else{

                        ListaDeImagenes.add(new Imagenes("%%%",null,"%%","%%"));
                        adaptero.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            adaptero= new AdaptadorImagenes(ct,ListaDeImagenes);
            recyclerImagenes.setAdapter(adaptero);



        }

        adaptero.setOnItemClickListener(new AdaptadorImagenes.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                String descript= ListaDeImagenes.get(position).getDescription().toString();
                String fechoria= ListaDeImagenes.get(position).getFecha().toString();
                String fotilla= ListaDeImagenes.get(position).getImage().toString();

                queEs.setText(descript);
                Glide.with(getApplicationContext())
                        .load(fotilla)
                        .placeholder(R.drawable.foto)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(documentoGrande);

            }

            @Override
            public void OnItemClickDelete(int position) {
                lasImagenes.child(ListaDeImagenes.get(position).getKey().toString()).removeValue();
                adaptero.notifyDataSetChanged();
            }
        });


    }
    public void mostrarMenu(View v){
        PopupMenu popupo= new PopupMenu(this,v);
        popupo.setOnMenuItemClickListener(this);
        popupo.inflate(R.menu.menu_imagen);
        popupo.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId())

        {





            case R.id.agregaimagen:

                if (id!= identificador){


                AlertDialog.Builder titulo= new AlertDialog.Builder(MainActivity4.this);
                titulo.setTitle(getString(R.string.entredecripcionImagen));
                final EditText tituloDeImagen= new EditText(MainActivity4.this);
                tituloDeImagen.setInputType(InputType.TYPE_CLASS_TEXT);
                titulo.setView(tituloDeImagen);
                titulo.setIcon(R.drawable.logo);

                titulo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tituloImgen= tituloDeImagen.getText().toString();
                        Intent intent= new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, resultadoDeImagen);

                    }
                });
               titulo.show();

                return true; }


           default:

                   return false;
        }

    }





    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_barra,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.verPerfil:
                Intent intent=new Intent(getApplicationContext(), MainActivity5.class);
                startActivity(intent);

                break;


            case R.id.verRegistro:


                    Intent intentalo=new Intent(getApplicationContext(), MainActivity.class);
                    intentalo.putExtra("identificador",identificador);
                    intentalo.putExtra("Imagen",laFoto);
                    startActivity(intentalo);

                break;


            case R.id.verContactos:


                    Intent intento=new Intent(getApplicationContext(), MainActivity2.class);
                    intento.putExtra("identificador",identificador);
                    intento.putExtra("laImagen", laFoto);
                intento.putExtra("Imagen", laFoto);

                    startActivity(intento);
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

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==resultadoDeImagen && resultCode==RESULT_OK && data != null && data.getData() != null ) {
            final ProgressDialog pd= new ProgressDialog(this);
            pd.setTitle(getString(R.string.cargandoImagen));
            pd.show();
            imagenFoto= data.getData();


            final String randomKey= UUID.randomUUID().toString();
            StorageReference riversRef= storageReference.child("images/"+ randomKey);
            riversRef.putFile(imagenFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            fotoUrl = uri;

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



                            miOtraFoto=FirebaseDatabase.getInstance().getReference().child("adms").child(identificador).child("Contactos").child(id).child("Imagenes");
                            Map<String,Object> NuevaImagen = new HashMap<>();
                            NuevaImagen.put("Descripcion",tituloImgen);
                            NuevaImagen.put("Imagen",fotoUrl.toString());
                            NuevaImagen.put("fecha",fecha);

                            miOtraFoto.push().setValue(NuevaImagen);

                            queEs.setText(tituloImgen);
                            Glide.with(getApplicationContext())
                                    .load(fotoUrl.toString())
                                    .placeholder(R.drawable.foto)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .into(documentoGrande);

                            /*miOtraFoto.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        miOtraFoto.child("Foto").setValue(fotoUrl.toString());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });*/

                            pd.dismiss();





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity4.this, getString(R.string.problemasConUrl), Toast.LENGTH_SHORT).show();
                        }
                    });


                    ;
                    Toast.makeText(MainActivity4.this, getString(R.string.laImagenSeCargo), Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity4.this, getString(R.string.noCargoLaImagen), Toast.LENGTH_SHORT).show();
                }
            });



        }


    }
}