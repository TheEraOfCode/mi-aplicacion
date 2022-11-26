package com.c.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity3 extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
   LinearLayout lay3;
    RecyclerView recyclerTransacciones;
    ArrayList<Transaccion> ListaTransacciones;
    Context ct;
    adaptadorT adaptert;
    private DatabaseReference datos;
    private DatabaseReference balanciado;

    private DatabaseReference pago;
    private DatabaseReference cargoDt;
    private DatabaseReference nuevaImagen;
public static String laFoto;
    private DatabaseReference transaLista;

    private DatabaseReference Prueba;
    public static String clavePrincipal,pagoNuevo, nombre, id,foto1,cargoNuevo, ocupar,fechaIngreso, textoNotificacion;


    private DatabaseReference clientePrincipal;
    TextView Id,Telefono, Email,Balance,Type,ocupacion,fechaText;
    static TextView Nombre;
    ImageView imagen3, imagenFoto, imagenMenu, notificacion;
     ;
    ArrayList<Integer> values;

    Button nuevoPago, nuevoCargo;

    int pagoNumerico,cargoNumerico, viejoBalanceNumerico, nuevoBalanceNumerico,viejoBalanceParaCargos,nuevoBalanceParaCargos;

    Integer resultadoImagen= 3 ;


    Uri imagenUri3 =null;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener timePickerListener;

    private FirebaseStorage storage3;
    private StorageReference storageReference3;

    Uri imagenUrl;

    LinearLayout ly;
    Calendar calendar, calendario;
    String fechaFija;
 public  static String identificar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        storage3=  FirebaseStorage.getInstance();
        storageReference3= storage3.getReference();

        lay3=findViewById(R.id.lay3);
        balanciado=FirebaseDatabase.getInstance().getReference();
        ListaTransacciones= new ArrayList<Transaccion>();
        recyclerTransacciones= findViewById(R.id.recyclerTarteja);
        recyclerTransacciones.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerTransacciones.setHasFixedSize(true);
        imagenMenu= findViewById(R.id.imageMenu);
        imagenFoto=findViewById(R.id.imagexl3);
        Type= findViewById(R.id.type);
        Nombre= findViewById(R.id.nombreTarjeta);
        Id= findViewById(R.id.IdTarjeta);
       Telefono= findViewById(R.id.telefonoTarjeta);
       Email= findViewById(R.id.emailTarjeta);
        Balance= findViewById(R.id.balanceTarjeta);
        nuevoPago = findViewById(R.id.NuevoPago);
        nuevoCargo= findViewById(R.id.NuevoCargo);
        ocupacion= findViewById(R.id.Ocupacion);
        fechaText =findViewById(R.id.textViewFecha);
        ly= findViewById(R.id.layo);
       //imagenMenu.setImageResource(R.drawable.foto);
        calendar = Calendar.getInstance();
        fechaFija = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        Prueba= FirebaseDatabase.getInstance().getReference();

        notificacion= findViewById(R.id.notificacion);
        calendario = Calendar.getInstance();

        //createNotificationChannel();


      notificacion.setOnClickListener(new View.OnClickListener()   {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder textN= new AlertDialog.Builder(MainActivity3.this);
                textN.setTitle(getString(R.string.textoNotificacion));
                textN.setIcon(R.drawable.ic_launcher_foreground);


                final EditText textoNoT= new EditText(MainActivity3.this);
                textoNoT.setInputType(InputType.TYPE_CLASS_TEXT);
                textN.setView(textoNoT);
                textN.setIcon(R.drawable.logo);

                textN.setPositiveButton(getString(R.string.programarFecha), new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textoNotificacion= textoNoT.getText().toString();
                        Calendar cal = Calendar.getInstance();
                        int dia = cal.get(Calendar.DAY_OF_MONTH);
                        int mes = cal.get(Calendar.MONTH);
                        int año= cal.get(Calendar.YEAR);
                        mDateSetListener= new DatePickerDialog.OnDateSetListener() {


                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                                calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                calendario.set(Calendar.MONTH,month);
                                calendario.set(Calendar.YEAR,year);


                                int hora= cal.get(Calendar.HOUR);
                                int minuto= cal.get(Calendar.MINUTE);

                                timePickerListener= new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        calendario.set(Calendar.HOUR,hourOfDay);
                                        calendario.set(Calendar.MINUTE,minute);
                                        calendario.set(Calendar.AM_PM,0);

                                        notificar();

                                        Toast.makeText(MainActivity3.this, getString(R.string.notificacionProgramada)+calendario.getTime().toString(), Toast.LENGTH_LONG).show();







                                    }
                                };


                                TimePickerDialog timePickerDialog= new TimePickerDialog(
                                        MainActivity3.this,
                                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                        timePickerListener,
                                        hora,minuto,false);
                                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                timePickerDialog.show();

                            }
                        };




                        DatePickerDialog dialogo = new DatePickerDialog(
                                MainActivity3.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener,
                                año,mes,dia);
                        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogo.show();





                    }
                });


           textN.show();






            }
        });






        nuevoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                pago= FirebaseDatabase.getInstance().getReference();



                AlertDialog.Builder mydialog1= new AlertDialog.Builder(MainActivity3.this);
                mydialog1.setTitle(getString(R.string.montoPago));
                mydialog1.setIcon(R.drawable.ic_launcher_foreground);


                final EditText entraDato1= new EditText(MainActivity3.this);
                entraDato1.setInputType(InputType.TYPE_CLASS_NUMBER);
                mydialog1.setView(entraDato1);
                mydialog1.setIcon(R.drawable.logo);
                mydialog1.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pagoNuevo= entraDato1.getText().toString();
                        pagoNumerico=Integer.parseInt(pagoNuevo);
                        viejoBalanceParaCargos= Integer.parseInt(Balance.getText().toString());


                        AlertDialog.Builder nuevoDialogo= new AlertDialog.Builder(MainActivity3.this);
                        nuevoDialogo.setTitle(getString(R.string.conceptoPago));
                        final EditText concepto= new EditText(MainActivity3.this);
                        concepto.setInputType(InputType.TYPE_CLASS_TEXT);
                        nuevoDialogo.setView(concepto);
                        nuevoDialogo.setIcon(R.drawable.logo);

                        nuevoDialogo.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override



                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder pagoDialogo= new AlertDialog.Builder(MainActivity3.this);
                                final ImageView imagenPagango= new ImageView(MainActivity3.this);
                                imagenPagango.setImageResource(R.drawable.foto);
                                pagoDialogo.setTitle(getString(R.string.cargaImagen));
                                pagoDialogo.setView(imagenPagango);
                                pagoDialogo.setIcon(R.drawable.logo);
                                imagenPagango.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent= new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(intent, resultadoImagen);



                                    }
                                });
                                pagoDialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Map<String,Object> mapaPago = new HashMap<>();
                                        mapaPago.put("Monto", Integer.parseInt(pagoNuevo));
                                        mapaPago.put("Concepto", concepto.getText().toString());
                                        mapaPago.put("Fecha", currentDate);
                                        mapaPago.put("Documento",imagenUrl.toString());
                                        mapaPago.put("Tipo","1");

                                        pago.child("adms").child(identificar).child("Contactos").child(Id.getText().toString()).child("Transacciones").push().setValue(mapaPago);
                                        nuevoBalanceParaCargos= pagoNumerico+viejoBalanceParaCargos;
                                        Map <String,Object>mapaBalance = new HashMap<>();
                                        mapaBalance.put("Balance", nuevoBalanceParaCargos);

                                        balanciado.child("adms").child(identificar).child("Contactos").child(Id.getText().toString()).child("BALANCE").setValue(mapaBalance);


                                    }
                                });

                                pagoDialogo.show();

                                   }
                        });




                        nuevoDialogo.show();

                    }
                });

                mydialog1.show();



                //Map<String,Object> mapa = new HashMap<>();
                //mapa.put("Monto", Integer.parseInt(nuevoMonto));
               // mapa.put("Concepto", concepto.getText().toString());
               // mapa.put("Fecha", currentDate);


            }
        });

       nuevoCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendario = Calendar.getInstance();
                String fechaActual = DateFormat.getDateInstance(DateFormat.FULL).format(calendario.getTime());
                cargoDt= FirebaseDatabase.getInstance().getReference();


                AlertDialog.Builder cargo= new AlertDialog.Builder(MainActivity3.this);
                cargo.setTitle(getString(R.string.montoCargo));
                cargo.setIcon(R.drawable.logo);
                final EditText entraMonto= new EditText(MainActivity3.this);
                entraMonto.setInputType(InputType.TYPE_CLASS_NUMBER);
                cargo.setView(entraMonto);
                cargo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cargoNuevo= entraMonto.getText().toString();
                        cargoNumerico=Integer.parseInt(cargoNuevo);
                        viejoBalanceNumerico= Integer.parseInt(Balance.getText().toString());


                       AlertDialog.Builder concar= new AlertDialog.Builder(MainActivity3.this);
                       concar.setTitle(getString(R.string.conceptoCargo));
                       concar.setIcon(R.drawable.logo);
                       final EditText entraConceptoCargo= new EditText(MainActivity3.this);
                       entraConceptoCargo.setInputType(InputType.TYPE_CLASS_TEXT);
                       concar.setView(entraConceptoCargo);
                       concar.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {

                               AlertDialog.Builder cargoDialogo= new AlertDialog.Builder(MainActivity3.this);
                               final ImageView imagenCargando= new ImageView(MainActivity3.this);
                               imagenCargando.setImageResource(R.drawable.foto);
                               cargoDialogo.setTitle(getString(R.string.cargaImagen));
                               cargoDialogo.setView(imagenCargando);
                               cargoDialogo.setIcon(R.drawable.logo);
                               imagenCargando.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent intent= new Intent();
                                       intent.setType("image/*");
                                       intent.setAction(Intent.ACTION_GET_CONTENT);
                                       startActivityForResult(intent, resultadoImagen);
                                   }
                               });

                               cargoDialogo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       String conceptoCargo= entraConceptoCargo.getText().toString();
                                       Map<String,Object> mapaCargo = new HashMap<>();
                                       mapaCargo.put("Monto", Integer.parseInt(cargoNuevo));
                                       mapaCargo.put("Concepto", conceptoCargo);
                                       mapaCargo.put("Fecha", fechaActual);
                                       mapaCargo.put("Tipo","2");
                                       mapaCargo.put("Documento",imagenUrl.toString());


                                       cargoDt.child("adms").child(identificar).child("Contactos").child(Id.getText().toString()).child("Transacciones").push().setValue(mapaCargo);

                                       nuevoBalanceNumerico= viejoBalanceNumerico-cargoNumerico;
                                       Map <String,Object>mapaBalance = new HashMap<>();
                                       mapaBalance.put("Balance", nuevoBalanceNumerico);

                                       balanciado.child("adms").child(identificar).child("Contactos").child(Id.getText().toString()).child("BALANCE").setValue(mapaBalance);

                                   }
                               });

                               cargoDialogo.show();




                           }
                       });
                       concar.show();


                    }
                });
                cargo.show();

            }
        });

       imagenFoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (foto1!=null){
                   String imagenId= foto1;
                   String nombreid= nombre;
                   String idid= getString(R.string.decripcionImagen);
                   id= Id.getText().toString();
                   Intent intent=new Intent(getApplicationContext(), MainActivity4.class);

                   intent.putExtra("Imagen",imagenId);
                   intent.putExtra("nombre",nombreid);
                   intent.putExtra("concepto",idid);
                   intent.putExtra("id",id);
                   intent.putExtra("identificador",identificar);
                   intent.putExtra("laImagen",laFoto);


                   startActivity(intent);

               }
           }
       });

        Bundle extras =getIntent().getExtras();
        if (extras!=null){
            clavePrincipal = extras.getString("clave");
           identificar = extras.getString("identificador");
           laFoto= extras.getString("Imagen");
           //ocupar= extras.getString("ocupacion");
           fechaIngreso= extras.getString("fecha");

        clientePrincipal= FirebaseDatabase.getInstance().getReference().child("adms").child(identificar).child("Contactos").child(clavePrincipal);
        clientePrincipal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (
                        snapshot.exists()
                ){
                int Tipo =  Integer.parseInt(snapshot.child("Datos").child("Tipo").getValue().toString())  ;
                 nombre = snapshot.child("Datos").child("Nombre").getValue().toString();
                String cedula = snapshot.child("Datos").child("Id").getValue().toString();
                String telefono = snapshot.child("Datos").child("Telefono").getValue().toString();
                String email = snapshot.child("Datos").child("Email").getValue().toString();
                foto1=  snapshot.child("Datos").child("Imagen de Id").getValue().toString();
                ocupar= snapshot.child("Datos").child("Ocupacion").getValue().toString();
               // String balance = snapshot.child("BALANCE").child("Balance").getValue().toString();
                    String balance = snapshot.child("BALANCE").child("Balance").getValue().toString();

                    String[] yourArray = getResources().getStringArray(R.array.tipos);

                                    Type.setText(yourArray[Tipo]);
                Nombre.setText(nombre);
                Id.setText(cedula);
                Telefono.setText(telefono);
                Email.setText(email);
                ocupacion.setText(ocupar);
                fechaText.setText(fechaIngreso);
                Balance.setText(balance);
                    Glide.with(getApplicationContext())
                            .load(foto1)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(imagenFoto);

                    String[] types = getResources().getStringArray(R.array.tipos);
                    String definicion = Type.getText().toString();

                    if (definicion.equals("Cliente")) {
                       lay3.setBackgroundColor(Color.GREEN);
                    }
                    else if (definicion.equals("Suplidor")) {
                        lay3.setBackgroundColor(Color.RED);
                    }
                    else if (definicion.equals("Prospecto")) {
                        lay3.setBackgroundColor(Color.YELLOW);
                    }
                    else   {
                        lay3.setBackgroundColor(Color.BLUE);
                    }


                }






                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            transaLista= FirebaseDatabase.getInstance().getReference().child("adms").child(identificar).child("Contactos").child(clavePrincipal).child("Transacciones");

          transaLista.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (snapshot.exists()) {

                      ListaTransacciones.clear();
                      for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                          String monto= dataSnapshot.child("Monto").getValue().toString();
                          String concepto= dataSnapshot.child("Concepto").getValue().toString();
                          String documento= dataSnapshot.child("Documento").getValue().toString();
                          String fecha= dataSnapshot.child("Fecha").getValue().toString();
                          String tipo= dataSnapshot.child("Tipo").getValue().toString();

                          ListaTransacciones.add(new Transaccion(concepto,documento,fecha,monto,tipo));

                      }
                      Collections.reverse(ListaTransacciones);
                   adaptert.notifyDataSetChanged();

                  }
                  else{
                      ListaTransacciones.add(new Transaccion("%%%",null,"$$","$$","%%"));
                      adaptert.notifyDataSetChanged();
                  }


              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
            ct= getApplicationContext();
            adaptert= new adaptadorT(ct,ListaTransacciones);
            recyclerTransacciones.setAdapter(adaptert);



            }
        else {
            Intent intent=new Intent(getApplicationContext(), MainActivity5.class);
            startActivity(intent);
        }

           adaptert.setOnItemClickListener(new adaptadorT.OnItemClickListener()   {
               @Override
               public void OnItemClick(int position) {
                  id= Id.getText().toString();
                   String documento= ListaTransacciones.get(position).getDocumento().toString();
                   String tipo= ListaTransacciones.get(position).getTipo().toString();
                   Intent intent=new Intent(getApplicationContext(), MainActivity4.class);
                   intent.putExtra("Imagen",documento);
                   intent.putExtra("nombre",nombre);
                   intent.putExtra("concepto",tipo);
                   intent.putExtra("id",id);
                   intent.putExtra("identificador",identificar);
                   intent.putExtra("laImagen",laFoto);



                   startActivity(intent);
               }
           });


        }




        public void llamarOWasap(View view){

    AlertDialog.Builder mydialog= new AlertDialog.Builder(MainActivity3.this);
    mydialog.setTitle(getString(R.string.accion));
    mydialog.setIcon(R.drawable.ic_launcher_foreground);


     mydialog.setIcon(R.drawable.logo);


    mydialog.setPositiveButton("Whatsapp", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {


            String url = "https://api.whatsapp.com/send?phone="+"1"+Telefono.getText().toString();

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }
    });


    mydialog.setNegativeButton(getString(R.string.llamar), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int in) {


            Intent i = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+Telefono.getText().toString()));
            startActivity(i);
        }
    });

    mydialog.show();



}



       public void enviarEmail (View view){

    AlertDialog.Builder mydialogo= new AlertDialog.Builder(MainActivity3.this);
    mydialogo.setTitle(getString(R.string.accion  ));
    mydialogo.setIcon(R.drawable.ic_launcher_foreground);


     mydialogo.setIcon(R.drawable.logo);

    mydialogo.setPositiveButton(getString(R.string.enviarEmail), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent= new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{Email.getText().toString()});
            startActivity(intent);
        }
    });

           mydialogo.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.dismiss();
               }
           });
    mydialogo.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==resultadoImagen && resultCode==RESULT_OK && data != null && data.getData() != null ){
            final ProgressDialog pd= new ProgressDialog(this);
            pd.setTitle(getString(R.string.cargandoImagen));
            pd.show();
            imagenUri3= data.getData();
            //imagen3.setImageURI(imagenUri3);

            final String randomKey= UUID.randomUUID().toString();
            StorageReference riversRef= storageReference3.child("images/"+ randomKey);
            riversRef.putFile(imagenUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imagenUrl = uri;
                            pd.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity3.this, getString(R.string.problemasConUrl), Toast.LENGTH_SHORT).show();
                        }
                    });


                    ;
                    Toast.makeText(MainActivity3.this, getString(R.string.laImagenSeCargo), Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity3.this, getString(R.string.noCargoLaImagen), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void mostrarMenu(View v){
        PopupMenu popup= new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menupop);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String referencia= Id.getText().toString();


            AlertDialog.Builder mydialog= new AlertDialog.Builder(MainActivity3.this);
            mydialog.setTitle(getString(R.string.nuevoValor));
            mydialog.setIcon(R.drawable.ic_launcher_foreground);


            final EditText entraDato= new EditText(MainActivity3.this);
            entraDato.setInputType(InputType.TYPE_CLASS_TEXT);
            mydialog.setView(entraDato);
            mydialog.setIcon(R.drawable.logo);

            mydialog.setPositiveButton(getString(R.string.modificar), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(entraDato.getText().toString()!=null){
                        String  nuevoValor= entraDato.getText().toString();






                        switch (item.getItemId()) {

                            case R.id.item1:

                                clientePrincipal= FirebaseDatabase.getInstance().getReference().child("adms").child(identificar).child("Contactos").child(referencia)
                                        .child("Datos").child("Nombre");

                                clientePrincipal.setValue(nuevoValor);
                                break;

                            case R.id.item2:

                                clientePrincipal= FirebaseDatabase.getInstance().getReference().child("adms").child(identificar).child("Contactos").child(referencia)
                                        .child("Datos").child("Telefono");

                                clientePrincipal.setValue(nuevoValor);
                                break;

                            case R.id.item3:

                                clientePrincipal= FirebaseDatabase.getInstance().getReference().child("adms").child(identificar).child("Contactos").child(referencia)
                                        .child("Datos").child("Email");

                                clientePrincipal.setValue(nuevoValor);
                                break;

                            case R.id.item4:

                                clientePrincipal= FirebaseDatabase.getInstance().getReference().child("adms").child(identificar).child("Contactos").child(referencia)
                                        .child("Datos").child("Ocupacion");

                                clientePrincipal.setValue(nuevoValor);


                                break;


                            default:



                        }





                    } else {
                        entraDato.setError(getString(R.string.nuevoValor));

                    }




                }
            });

            mydialog.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            mydialog.show();



            return true;







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
        switch (item.getItemId()){
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

            case R.id.verContactos:

                if (user != null) {
                    String uid = user.getUid();
                    Intent intento=new Intent(getApplicationContext(), MainActivity2.class);
                    intento.putExtra("identificador",uid);
                    intento.putExtra("Imagen", laFoto);
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



        }
        
        return super.onOptionsItemSelected(item);
    }

    public void notificar(){

  String tag= generateKey();
  Long Alerttime = calendario.getTimeInMillis()-System.currentTimeMillis();
  int random= (int) (Math.random()*50+1);
  Data data = GuardarData("Notificacion workmanager","Soy un detalle", random);

  Workmanagrnoti.GuardarNoti(Alerttime,data,tag);




    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

private String generateKey(){

      return UUID.randomUUID().toString();

}

private Data GuardarData(String titulo, String detalle, int id_noti){

        return new Data.Builder()
                .putString("titulo", getString(R.string.notificacionDe)+" "+Nombre.getText().toString())
                .putString("detalle",  textoNotificacion)

                .putString("clave", clavePrincipal)
                .putString("identificar",  identificar)
                .putString("laFoto", laFoto)
                .putString("fechaIngreso",  fechaIngreso)


                .putInt("id_noti", id_noti).build();


}



}



