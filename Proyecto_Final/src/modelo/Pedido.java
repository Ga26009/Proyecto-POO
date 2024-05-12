/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import static modelo.ItemProducto.calcularValorTotal;

/**
 *
 * @author gabag
 */
public class Pedido {
    int numero;
    LocalDateTime fechaHora;
    Cliente suCliente;
    LinkedList<ItemProducto> susItemsProductos;
    String observacion;
    boolean normal;
    char estado;

    public Pedido(int numero, LocalDateTime fechaHora, Cliente suCliente, LinkedList<ItemProducto> susItemsProductos, String observacion, boolean normal, char estado) {
        this.numero = numero;
        this.fechaHora = fechaHora;
        this.suCliente = suCliente;
        this.susItemsProductos = susItemsProductos;
        this.observacion = observacion;
        this.normal = normal;
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Cliente getSuCliente() {
        return suCliente;
    }

    public void setSuCliente(Cliente suCliente) {
        this.suCliente = suCliente;
    }

    public LinkedList<ItemProducto> getSusItemsProductos() {
        return susItemsProductos;
    }

    public void setSusItemsProductos(LinkedList<ItemProducto> susItemsProductos) {
        this.susItemsProductos = susItemsProductos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean isNormal() {
        return normal;
    }

    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido{" + "numero=" + numero + ", fechaHora=" + fechaHora + ", suCliente=" + suCliente + ", susItemsProductos=" + susItemsProductos + ", observacion=" + observacion + ", normal=" + normal + ", estado=" + estado + '}';
    }
public int calcularCantidadItemsPedido() {
        return susItemsProductos.size(); // Devuelve el número de ítems
    }

public double calcularValorTotalItems() {
    double valorTotal = 0.0;

    // Iterar sobre cada ItemProducto en susItemsProductos
    for (ItemProducto item : susItemsProductos) {
        int cantidad = (int) item.getCantidad();
        double precio = item.getPrecio();
        double porcentajeIva = item.getPorcentajeIva();
        String categoria = item.getCategoria();

        double valorItem = ItemProducto.calcularValorTotal(cantidad, precio, porcentajeIva, categoria);

        // Sumar el valor total del ItemProducto al valorTotal
        valorTotal += valorItem;
    }

    return valorTotal;
}

public double calcularValorTotalPagar() {
    return calcularValorTotalItems();
}

// Método para calcular el valor total a pagar, sumando un valor por urgencia
public double calcularValorTotalPagar(double valorUrgencia) {
    return calcularValorTotalItems() + valorUrgencia; // Suma el valor de los ítems y el valor por urgencia
}
}
