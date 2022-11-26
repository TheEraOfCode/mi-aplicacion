package com.c.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


public class MainActivity2 extends AppCompatActivity {
    RecyclerView recyclerClientes;
    ArrayList<Cliente> ListaClientes;
    Context ct;
    Adaptador adapter;
    private DatabaseReference datos;
    private DatabaseReference borrar;
  private String laFoto;
    SearchView Buscar;
     String identifica;
     Button fab;
     boolean masivo= false;
    ArrayList<Cliente> nuevaLista;
    ArrayList<String> emails;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            identifica = extras.getString("identificador");
            laFoto= extras.getString("Imagen");


        }else {
            Intent intent=new Intent(getApplicationContext(), MainActivity5.class);
            startActivity(intent);
        }

        Buscar= findViewById(R.id.buscar);

        ListaClientes= new ArrayList<Cliente>();
        recyclerClientes= findViewById(R.id.recycler);
        recyclerClientes.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                //(new GridLayoutManager(this, 3));

        recyclerClientes.setHasFixedSize(true);




        datos= FirebaseDatabase.getInstance().getReference().child("adms").child(identifica).child("Contactos");
        borrar= FirebaseDatabase.getInstance().getReference().child("adms").child(identifica);

        datos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    ListaClientes.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String key= dataSnapshot.getKey().toString();
                    String nombre= dataSnapshot.child("Datos").child("Nombre").getValue().toString();
                    String cedula= dataSnapshot.child("Datos").child("Id").getValue().toString();
                    String ocupacion= dataSnapshot.child("Datos").child("Ocupacion").getValue().toString();
                    String foto=  dataSnapshot.child("Datos").child("Imagen de Id").getValue().toString();
                    String fechado= dataSnapshot.child("Datos").child("Creado").getValue().toString();
                    int tipado = Integer.parseInt(dataSnapshot.child("Datos").child("Tipo").getValue().toString()) ;
                    String email = dataSnapshot.child("Datos").child("Email").getValue().toString();

                    String[] yourArray = getResources().getStringArray(R.array.tipos);
                    String tipo= yourArray[tipado];



                    ListaClientes.add(new Cliente(cedula,nombre, key,foto,ocupacion,fechado, tipo, email));

                                    }
                Collections.sort(ListaClientes,Cliente.organizaClientes);

                adapter.notifyDataSetChanged();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ct= getApplicationContext();
         adapter= new Adaptador(ct,ListaClientes);
        recyclerClientes.setAdapter(adapter);







      adapter.setOnItemClickListener(new Adaptador.OnItemClickListener() {
          @Override
          public Void OnItemClick(int position) {
              String claveLista= ListaClientes.get(position).getClave().toString();
              String nombreLista= ListaClientes.get(position).getNombre().toString();
              String idLista=  ListaClientes.get(position).getId().toString();
              String fotoId= ListaClientes.get(position).getFoto().toString();
              String ocuLista= ListaClientes.get(position).getOcupacion().toString();
              String fechaRegistro= ListaClientes.get(position).getFecha().toString();

              Intent intent=new Intent(getApplicationContext(), MainActivity3.class);
              intent.putExtra("clave",claveLista);
              intent.putExtra("nombre",nombreLista);
              intent.putExtra("id",idLista);
              intent.putExtra("Id",fotoId);
              intent.putExtra("identificador",identifica);
              intent.putExtra("ocupacion",ocuLista);
              intent.putExtra("fecha",fechaRegistro);

              startActivity(intent);




              return null;
          }

          @Override
          public void OnitemClickForDelete(int position) {


              borrar.child(ListaClientes.get(position).getClave().toString()).removeValue();
              adapter.notifyDataSetChanged();

          }
      });

        Buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                nuevaLista= new ArrayList<>();
                for (Cliente obj: ListaClientes){
                    if (obj.getNombre().toLowerCase().contains(newText.toLowerCase())||
                            obj.getOcupacion().toLowerCase().contains(newText.toLowerCase())
                            || obj.getTipo().toLowerCase().contains(newText.toLowerCase()) ){

                        nuevaLista.add(obj);



                        Collections.sort(nuevaLista,Cliente.comparadorCliente);
                    }
                }


                Adaptador nuevoAdaptador = new Adaptador(ct,nuevaLista);
                recyclerClientes.setAdapter(nuevoAdaptador);

               // Collections.sort(nuevaLista,Cliente.comparadorCliente);

               //nuevoAdaptador.notifyDataSetChanged();

             nuevoAdaptador.setOnItemClickListener(new Adaptador.OnItemClickListener() {
                 @Override
                 public Void OnItemClick(int position) {
                     String claveLista= nuevaLista.get(position).getClave().toString();
                     String nombreLista= nuevaLista.get(position).getNombre().toString();
                     String idLista=  nuevaLista.get(position).getId().toString();
                     String fotoId= nuevaLista.get(position).getFoto().toString();
                     String ocuLista= nuevaLista.get(position).getOcupacion().toString();
                     String fechaRegistro= nuevaLista.get(position).getFecha().toString();

                     Intent intent=new Intent(getApplicationContext(), MainActivity3.class);
                     intent.putExtra("clave",claveLista);
                     intent.putExtra("nombre",nombreLista);
                     intent.putExtra("id",idLista);
                     intent.putExtra("Id",fotoId);
                     intent.putExtra("identificador",identifica);
                     intent.putExtra("ocupacion",ocuLista);
                     intent.putExtra("fecha",fechaRegistro);

                     startActivity(intent);

                     return null;
                 }

                 @Override
                 public void OnitemClickForDelete(int position) {
                     borrar.child(nuevaLista.get(position).getClave().toString()).removeValue();
                     nuevoAdaptador.notifyDataSetChanged();
                     adapter.notifyDataSetChanged();
                 }
             });


                return true;
            }
        });

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
        switch (item.getItemId()) {
            case R.id.verPerfil:
                Intent intent=new Intent(getApplicationContext(), MainActivity5.class);
                startActivity(intent);

            break;
            case R.id.verRegistro:
                if (user != null) {

                    String uid = user.getUid();
                    Intent intenta=new Intent(getApplicationContext(), MainActivity.class);
                    intenta.putExtra("identificador",uid);
                    intenta.putExtra("Imagen", laFoto);
                    startActivity(intenta);  }

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
    public void emailMasivo(View view) {


           String email;
           emails= new ArrayList<>();

         if(nuevaLista!= null){
           for( Cliente obj : nuevaLista ) {

               email = obj.getEmail();

               emails.add(email);
            }
        Intent intent= new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0)});

           switch(emails.toArray().length){

              case 1:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0)});
                    break;

               case 2:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),emails.get(1)});
                   startActivity(intent);
                   break;

               case 3:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),emails.get(1),emails.get(2)});
                   startActivity(intent);
                   break;

               case 4:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0)});
                   startActivity(intent);
                   break;

               case 5:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),emails.get(1),emails.get(2),emails.get(3),emails.get(4)});
                   startActivity(intent);
                   break;

               case 6:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),emails.get(1),emails.get(2),emails.get(3),emails.get(4),emails.get(5)});
                   startActivity(intent);
                   break;

               case 7:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6)});
                   startActivity(intent);
                   break;

               case 8:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7)});
                   startActivity(intent);
                   break;

               case 9:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8)});
                   startActivity(intent);
                   break;
               case 10:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9)});
                   startActivity(intent);
                   break;

               case 11:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                            emails.get(10)});
                   startActivity(intent);
                   break;

               case 12:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                           emails.get(10),emails.get(11)});
                   break;

               case 13:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                           emails.get(10),emails.get(11),emails.get(12)});
                   startActivity(intent);
                   break;
               case 14:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                           emails.get(10),emails.get(11),emails.get(12),emails.get(13)});
                   startActivity(intent);
                   break;

               case 15:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                           emails.get(10),emails.get(11),emails.get(12),emails.get(13),emails.get(14)});
                   startActivity(intent);
break;
               case 16:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                           emails.get(10),emails.get(11),emails.get(12),emails.get(13),emails.get(14),
                   emails.get(15)});
                   startActivity(intent);
                   break;
               case 17:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                           emails.get(10),emails.get(11),emails.get(12),emails.get(13),emails.get(14),
                           emails.get(15),emails.get(16)});
                   startActivity(intent);
                   break;
               case 18:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                           emails.get(10),emails.get(11),emails.get(12),emails.get(13),emails.get(14),
                           emails.get(15),emails.get(16),emails.get(17)});
                   startActivity(intent);

                   break;
               case 19:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                           emails.get(10),emails.get(11),emails.get(12),emails.get(13),emails.get(14),
                           emails.get(15),emails.get(16),emails.get(17),emails.get(18)});
                   startActivity(intent);
                   break;
               case 20:
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emails.get(0),
                           emails.get(1),emails.get(2),emails.get(3),emails.get(4),
                           emails.get(5),emails.get(6),emails.get(7),emails.get(8),emails.get(9),
                           emails.get(10),emails.get(11),emails.get(12),emails.get(13),emails.get(14),
                           emails.get(15),emails.get(16),emails.get(17),emails.get(18),emails.get(19)});
                   startActivity(intent);
                   break;

               default:
                   AlertDialog.Builder mydialogo= new AlertDialog.Builder(MainActivity2.this);
                   mydialogo.setTitle(getString(R.string.maximoDeContactos));
                   mydialogo.setIcon(R.drawable.ic_launcher_foreground);
                   mydialogo.setIcon(R.drawable.logo);

                   mydialogo.setPositiveButton("Ok" ,new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           Snackbar.make(Buscar,getString(R.string.tipoContactoBarra),BaseTransientBottomBar.LENGTH_LONG)
                                   .show();
                       }
                   });
                   mydialogo.show();


           }







    } else {
            Snackbar.make(Buscar,getString(R.string.tipoContactoBarra),BaseTransientBottomBar.LENGTH_LONG)
                     .show();




         }








    }



}