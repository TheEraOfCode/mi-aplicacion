package com.c.myapplication;

import java.util.Comparator;
import java.util.Locale;

public class Cliente {
  private String Id;
  private String Nombre;
  private String Clave;
  private String Foto;
  private String Ocupacion;
  private String Fecha;
  private String Tipo;
  private String Email;



  public Cliente(String id, String nombre, String clave, String foto, String ocupacion, String fecha, String tipo, String email) {
    Id = id;
    Nombre = nombre;
    Clave = clave;
    Foto = foto;
    Ocupacion= ocupacion;
    Fecha= fecha;
    Tipo=tipo;
    Email= email;

  }

  public static Comparator<Cliente> comparadorCliente= new Comparator<Cliente>() {
    @Override
    public int compare(Cliente cliente, Cliente t1) {
      return cliente.getNombre().toLowerCase().compareTo(t1.getNombre().toLowerCase())
              & cliente.getOcupacion().toLowerCase().compareTo(t1.getNombre().toLowerCase())
              & cliente.getTipo().toLowerCase().compareTo(t1.getNombre().toLowerCase()) ;
    }
  };

  public static Comparator<Cliente> organizaClientes= new Comparator<Cliente>() {
    @Override
    public int compare(Cliente cliente, Cliente t1) {
      return cliente.getNombre().toLowerCase().compareTo(t1.getNombre().toLowerCase())
              ;
    }
  };








  public String getFoto() {
    return Foto;
  }

  public void setFoto(String foto) {
    Foto = foto;
  }

  public void setClave(String clave) {
    Clave = clave;
  }

  public String getClave() {
    return Clave;
  }

  public String getId() {
    return Id;
  }

  public String getNombre() {
    return Nombre;
  }

  public void setId(String id) {
    Id = id;
  }

  public void setNombre(String nombre) {
    Nombre = nombre;
  }

  public String getOcupacion() {
    return Ocupacion;
  }

  public void setOcupacion(String ocupacion) {
    Ocupacion = ocupacion;
  }

  public String getFecha() {
    return Fecha;
  }
  public void setFecha(String fecha) {
    Fecha = fecha;
  }


  public String getTipo() {
    return Tipo;
  }
  public void setTipo(String tipo) { Tipo = tipo;  }


  public String getEmail() {
    return Email;
  }
  public void setEmail(String email) { Email = email;  }


}

