/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author gabag
 */
public class ItemProducto {
    protected int codigo;
    protected String nombreP;
    protected double cantidad;
    protected double precio;

    public ItemProducto(int codigo, String nombreP, double cantidad, String presentacion, String tipo, double precio) {
        this.codigo = codigo;
        this.nombreP = nombreP;
        this.cantidad = cantidad;
        this.precio = precio;
    }
public static double calcularValorTotal(int cantidad, double precio, double porcentajeIva, String categoria) {
    double valorTotal;
    if (categoria.equals("Farmacia") || categoria.equals("CanastaFamilia")) {
        valorTotal = cantidad * precio;
    } else {
        double precioConIva = precio * (1 + porcentajeIva);
        valorTotal = cantidad * precioConIva;
    }
    return valorTotal;
}

    public ItemProducto(String codigoProducto, String nombreProducto, int cantidadProducto, double precioProducto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }



    public String getCategoria() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public double getPorcentajeIva() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
    
}
