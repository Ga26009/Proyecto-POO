/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author gabag
 */
public class ItemProductoFarmacia extends ItemProducto{
    String presentacion;
    
    
    public ItemProductoFarmacia(int codigo, String nombre, double cantidad, double precio, String presentacion1) {
        super(codigo, nombre, cantidad, presentacion, tipo, precio);
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    @Override
    public String toString() {
        return "ItemProductoFarmacia{" + "presentacion=" + presentacion + '}';
    }
    
    
}
