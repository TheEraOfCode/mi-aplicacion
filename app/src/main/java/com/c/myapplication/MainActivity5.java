package com.c.myapplication;



import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MainActivity5 extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final String CHANNEL_ID = "idCanal" ;
    Calendar calendar;
    private DatabaseReference miUid,miNombre, miOtraFoto;
    private TextView usuarioNombre, fechaInicio    ;
     private ImageView imagenDeUsuario;
    Integer resultadoFoto= 1;
    Uri imagenFoto =null;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Uri fotoUrl;
    public static String laFoto, fechaActual;
    Calendar calendario;
         private DatePickerDialog.OnDateSetListener mDateSetListener;
    NotificationCompat.Builder notificacion;
    private static final int idUnica=51623;
    private TimePickerDialog.OnTimeSetListener timePickerListener;



    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AeQhyGPNexvKZvbtRILDSjo2LQi_sK9xcE3YMEmfooUGpCo4oW7Y6pJy8zYOWxt-Oee8CLhtFaOVYFCv");

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Network");
        getSupportActionBar().setIcon(R.drawable.logo);*/

        setContentView(R.layout.activity_main5);

        notificacion= new NotificationCompat.Builder(this);
        notificacion.setAutoCancel(true);
         usuarioNombre = findViewById(R.id.nombreUsuario);

         imagenDeUsuario= findViewById(R.id.imageView);
         calendario= Calendar.getInstance();
         fechaActual= DateFormat.getDateInstance(DateFormat.SHORT).format(calendario.getTime());
        storage=  FirebaseStorage.getInstance();
        storageReference= storage.getReference();

        calendar= Calendar.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Button button = (Button) findViewById(R.id.paypal_button);
        createNotificationChannel();
        Bundle extras =getIntent().getExtras();
        if (extras!=null){
        String notificado= extras.getString("Notificacion");
            Toast.makeText(MainActivity5.this,notificado,Toast.LENGTH_SHORT).show();


        }


        if (user != null) {
String usuario= user.getUid();
            miUid= FirebaseDatabase.getInstance().getReference().child("adms").child(usuario).child("personales");

            miUid.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()) {
                        String inicia= snapshot.child("Inicio").getValue().toString();
                       usuarioNombre.setText(snapshot.child("Nombre").getValue().toString());
                       fechaInicio.setText(getString(R.string.miembroDesde)+inicia);
                       Glide.with(getApplicationContext())
                               .load(snapshot.child("Foto").getValue().toString())
                               .placeholder(R.drawable.ic_launcher_background)
                               .error(R.drawable.ic_launcher_foreground)
                               .into(imagenDeUsuario);
                       laFoto=snapshot.child("Foto").getValue().toString();

                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        } else {
       createSignInIntent();}


    }

   public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        Intent signInIntent;
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()


        );

        // Create and launch sign-in intent

        signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_create_intent]
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String usuario= user.getDisplayName();
            String idUsuario= user.getUid();
            String fotoUsuario=user.getPhotoUrl().toString();
            String emailUsuario= user.getEmail();
           miUid= FirebaseDatabase.getInstance().getReference().child("adms").child(idUsuario).child("personales");

           miUid.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists()) {
                  String inicial= snapshot.child("Inicio").getValue().toString();
                  fechaInicio.setText(getString(R.string.miembroDesde)+inicial);
                  usuarioNombre.setText(snapshot.child("Nombre").getValue().toString());
                  Glide.with(getApplicationContext())
                          .load(snapshot.child("Foto").getValue().toString())
                          .placeholder(R.drawable.ic_launcher_background)
                          .error(R.drawable.ic_launcher_foreground)
                          .into(imagenDeUsuario);


              }else {
                  if(usuario!=null & fotoUsuario!= null){
                      Map <String,Object>mapaPersonal = new HashMap<>();
                      mapaPersonal.put("Nombre", usuario);
                      mapaPersonal.put("Foto", fotoUsuario);
                      mapaPersonal.put("Email", emailUsuario);
                      mapaPersonal.put("Inicio", fechaActual);
                      mapaPersonal.put("Balance", 00);

                     miUid.setValue(mapaPersonal).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {

                             fechaInicio.setText(getString(R.string.miembroDesde)+fechaActual);
                             usuarioNombre.setText(usuario);
                             Glide.with(getApplicationContext())
                                     .load(fotoUsuario.toString())
                                     .placeholder(R.drawable.ic_launcher_background)
                                     .error(R.drawable.ic_launcher_foreground)
                                     .into(imagenDeUsuario);
                         }
                     });






                  }else {
                      AlertDialog.Builder nombreDatos= new AlertDialog.Builder(MainActivity5.this);
                      nombreDatos.setTitle(getString(R.string.nombreDeUsuario));
                      nombreDatos.setIcon(R.drawable.logo);
                      final EditText entraNombre= new EditText(MainActivity5.this);
                      entraNombre.setInputType(InputType.TYPE_CLASS_TEXT);
                      nombreDatos.setView(entraNombre);
                      nombreDatos.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              String capturaNombre= entraNombre.getText().toString();
                              AlertDialog.Builder imagenUsuario= new AlertDialog.Builder(MainActivity5.this);
                              final ImageView imagenCargando= new ImageView(MainActivity5.this);
                              imagenCargando.setImageResource(R.drawable.foto);
                              imagenUsuario.setTitle(getString(R.string.agregaimagenPerfil));
                              imagenUsuario.setView(imagenCargando);
                              imagenUsuario.setIcon(R.drawable.logo);
                              imagenCargando.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      Intent intent= new Intent();
                                      intent.setType("image/*");
                                      intent.setAction(Intent.ACTION_GET_CONTENT);
                                      startActivityForResult(intent, resultadoFoto);
                                  }
                              });
                              imagenUsuario.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      Map <String,Object>mapaPersonal = new HashMap<>();
                                      mapaPersonal.put("Nombre", capturaNombre);
                                      mapaPersonal.put("Foto", fotoUrl.toString());
                                      mapaPersonal.put("Email", emailUsuario);
                                      mapaPersonal.put("Balance", 00);
                                      miUid.setValue(mapaPersonal);




                                  }
                              });imagenUsuario.show();



                          }
                      });nombreDatos.show();

                  }



              }



               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });

        }

             else {


            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==resultadoFoto && resultCode==RESULT_OK && data != null && data.getData() != null ) {
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



                            miOtraFoto=FirebaseDatabase.getInstance().getReference().child("adms").child(user.getUid()).child("personales");
                            miOtraFoto.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                    miOtraFoto.child("Foto").setValue(fotoUrl.toString());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            pd.dismiss();





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity5.this, getString(R.string.problemasConUrl), Toast.LENGTH_SHORT).show();
                        }
                    });


                    ;
                    Toast.makeText(MainActivity5.this, getString(R.string.laImagenSeCargo), Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity5.this, getString(R.string.noCargoLaImagen), Toast.LENGTH_SHORT).show();
                }
            });



        }

        if (resultCode == Activity.RESULT_OK){
            PaymentConfirmation confirm = data.getParcelableExtra(
                    PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null){
                try {
                    Log.i("sampleapp", confirm.toJSONObject().toString(4));
// TODO: send 'confirm' to your server for verification
                } catch (JSONException e) {
                    Log.e("sampleapp", "no confirmation data: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("sampleapp", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("sampleapp", "Invalid payment / config set");
        }



        }




    public void VerContactos(View view){
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       if (user != null) {
       String uid = user.getUid();
       Intent intent=new Intent(getApplicationContext(), MainActivity2.class);
       intent.putExtra("identificador",uid);
           intent.putExtra("Imagen",laFoto);
       startActivity(intent); }
       else{
           createSignInIntent();

       }
   }

    public void VerRegistro(View view){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String uid = user.getUid();
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("identificador",uid);
            intent.putExtra("Imagen",laFoto);
            startActivity(intent);

        }
        else{
            createSignInIntent();

        }







    }


    public void mostrarMenuImagen(View v){
        PopupMenu popupo= new PopupMenu(this,v);
        popupo.setOnMenuItemClickListener(this);
        popupo.inflate(R.menu.menu_imagen_usuario);
        popupo.show();
    }





    public void desloguear(View view){
        //FirebaseAuth.getInstance().signOut();

        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        usuarioNombre.setText(getString(R.string.nombreDeUsuario));
                        Glide.with(getApplicationContext())
                                .load(R.drawable.foto)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(imagenDeUsuario);


                        createSignInIntent();
                    }
                });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if ((user) != null){

        switch (item.getItemId()) {
            case R.id.cambio1:
                AlertDialog.Builder cambiaNombre= new AlertDialog.Builder(MainActivity5.this);
                cambiaNombre.setTitle(getString(R.string.introduceNuevoNombreUsuario));
                cambiaNombre.setIcon(R.drawable.logo);
                final EditText entraNombre= new EditText(MainActivity5.this);
                entraNombre.setInputType(InputType.TYPE_CLASS_TEXT);
                cambiaNombre.setView(entraNombre);

                cambiaNombre.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        miNombre= FirebaseDatabase.getInstance().getReference().child("adms").child(user.getUid()).child("personales");
                        miNombre.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    miNombre.child("Nombre").setValue(entraNombre.getText().toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }); cambiaNombre.show();


                            break;

            case R.id.itemo1:

                     String nombreid= user.getDisplayName();
                    String idid= "imegen de id";
                    String id= "tuma";

                    Intent intent=new Intent(getApplicationContext(), MainActivity4.class);
                    intent.putExtra("Imagen",laFoto);
                    intent.putExtra("nombre",nombreid);
                    intent.putExtra("concepto",idid);
                    intent.putExtra("id",id);
                    intent.putExtra("identificador",user.getUid());
                    intent.putExtra("booleano",true);

                    startActivity(intent);


                break;
            case R.id.itemo2:
                Intent intenta= new Intent();
                intenta.setType("image/*");
                intenta.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intenta, resultadoFoto);

            break;


            default:  }

        }

        return true;
        }

    public void mostrarMenuCambiaNombre(View v){
        PopupMenu cambiaso= new PopupMenu(this,v);
        cambiaso.setOnMenuItemClickListener(this);
        cambiaso.inflate(R.menu.menu_cambia_nombre);
        cambiaso.show();
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

                break;

            case R.id.verRegistro:

                if (user != null) {

                String uid = user.getUid();
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("identificador",uid);
                intent.putExtra("Imagen",laFoto);
                startActivity(intent);  }

                break;
            case R.id.verContactos:

                if (user != null) {
                    String uid = user.getUid();
                    Intent intent=new Intent(getApplicationContext(), MainActivity2.class);
                    intent.putExtra("identificador",uid);
                    startActivity(intent); }

               break;

            case R.id.salida:

                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                usuarioNombre.setText(getString(R.string.nombreDeUsuario));
                                Glide.with(getApplicationContext())
                                        .load(R.drawable.foto)
                                        .placeholder(R.drawable.ic_launcher_background)
                                        .error(R.drawable.ic_launcher_foreground)
                                        .into(imagenDeUsuario);


                                createSignInIntent();
                            }
                        });
                break;

                default:

            }
        return super.onOptionsItemSelected(item);


    }

    public void beginPayment(View view){
        Intent serviceConfig = new Intent(this, PayPalService.class);
        serviceConfig.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(serviceConfig);
        PayPalPayment payment = new PayPalPayment(new BigDecimal("1.00"),
                "USD", "My Awesome Item", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent paymentConfig = new Intent(this, PaymentActivity.class);
        paymentConfig.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        paymentConfig.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(paymentConfig, 0);
    }


     public void probarFecha(View view ){
        Calendar cal = Calendar.getInstance();
         int dia = cal.get(Calendar.DAY_OF_MONTH);
         int mes = cal.get(Calendar.MONTH);
         int año= cal.get(Calendar.YEAR);
         mDateSetListener= new DatePickerDialog.OnDateSetListener() {
             @Override
             public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//month= month+1;

                 calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                 calendar.set(Calendar.MONTH,month);
                 calendar.set(Calendar.YEAR,year);


                 int hora= calendario.get(Calendar.HOUR);
                 int minuto= calendario.get(Calendar.MINUTE);

                 timePickerListener= new TimePickerDialog.OnTimeSetListener() {
                     @Override
                     public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                         calendar.set(Calendar.HOUR,hourOfDay);
                         calendar.set(Calendar.MINUTE,minute);
                         calendar.set(Calendar.AM_PM,0);



                       /*  notificacion.setSmallIcon(R.drawable.logo);
                         notificacion.setTicker("Network notification");
                         notificacion.setPriority(Notification.PRIORITY_HIGH);
                         notificacion.setWhen(System.currentTimeMillis());
                         notificacion.setContentTitle("Cliente");
                         notificacion.setContentText("Llamar a este cliente en el dia de hoy");


                         Intent intent = new Intent(MainActivity5.this,MainActivity5.class);

                         PendingIntent pendingIntent= PendingIntent.getActivity(MainActivity5.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                         notificacion.setContentIntent(pendingIntent);

                         NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                         nm.notify(idUnica,notificacion.build());*/




                     }
                 };


                 TimePickerDialog timePickerDialog= new TimePickerDialog(
                         MainActivity5.this,
                         android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                         timePickerListener,
                         hora,minuto,false);
                 timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 timePickerDialog.show();

            }
         };




         DatePickerDialog dialog = new DatePickerDialog(
           MainActivity5.this,
           android.R.style.Theme_Holo_Light_Dialog_MinWidth,
           mDateSetListener,
           año,mes,dia);
         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         dialog.show();
   
    }

    public void probarNotificacion(View view){

        Intent intent = new Intent(this, BroadR.class);
       // intent.putExtra("Notificacion","notificacion" );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT     );

        AlarmManager alarmManager= (AlarmManager)getSystemService(ALARM_SERVICE);
        long timeButtonClick = System.currentTimeMillis();

        long milliesC= calendar.getTimeInMillis();

        long milliD = milliesC - timeButtonClick;

        Long milliP= timeButtonClick + milliD;


long timeMilliSeconds= 5000;
        alarmManager.set(AlarmManager.RTC_WAKEUP,milliP,pendingIntent);


         /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setWhen(calendar.getTimeInMillis())
                .setAutoCancel(true);;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());*/


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




}
