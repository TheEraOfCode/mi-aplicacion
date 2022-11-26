package com.c.myapplication;

public class Transaccion {
  String Concepto;
  String Documento;
  String Fecha;
  String Monto;
  String Tipo;

  public Transaccion(String concepto, String documento, String fecha, String monto, String tipo) {
    Concepto = concepto;
    Documento = documento;
    Fecha = fecha;
    Monto = monto;
    Tipo = tipo;
  }





  public String getConcepto() {
    return Concepto;
  }

  public void setConcepto(String concepto) {
    Concepto = concepto;
  }

  public String getDocumento() {
    return Documento;
  }

  public void setDocumento(String documento) {
    Documento = documento;
  }

  public String getFecha() {
    return Fecha;
  }

  public void setFecha(String fecha) {
    Fecha = fecha;
  }

  public String getMonto() {
    return Monto;
  }

  public void setMonto(String monto) {
    Monto = monto;
  }

  public String getTipo() {
    return Tipo;
  }

  public void setTipo(String tipo) {
    Tipo = tipo;
  }
}
