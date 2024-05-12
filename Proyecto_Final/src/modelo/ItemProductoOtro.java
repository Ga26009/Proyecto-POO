/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author gabag
 */
public class ItemProductoOtro extends ItemProducto {
    double porcentajeIva;

    public ItemProductoOtro(int codigo, String nombre, double porcentajeIva, double cantidad, double precio) {
        super(codigo, nombre, cantidad, presentacion, tipo, precio);
        this.porcentajeIva = porcentajeIva;
    }

    public double getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(double porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    @Override
    public String toString() {
        return "ItemProductoOtro{" + "porcentajeIva=" + porcentajeIva + '}';
    }
    
    
}
