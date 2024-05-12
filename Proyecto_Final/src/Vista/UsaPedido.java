package Vista;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JOptionPane;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import modelo.Cliente;
import modelo.ItemProducto;
import modelo.Pedido;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import modelo.ItemProductoCanastaFamiliar;
import modelo.ItemProductoFarmacia;

public class UsaPedido {
    private static final LinkedList<Pedido> losPedidos = new LinkedList<>();
    private static final String ARCHIVO_PEDIDOS = "pedidos.txt";
    


        /**
         *
         * @param args
         */

    public static void main(String[] args) {
        // Lista de opciones para el menú
        String[] opciones = {
            "Crear Pedido",
            "Actualizar Estado de Pedido",
            "Consultar Todos los Pedidos",
            "Consultar Pedido por Número",
            "Consultar Último Pedido",
            "Eliminar Todos los Pedidos",
            "Eliminar un Pedido",
            "Eliminar Último Pedido",
            "Generar archivo de texto de los pedidos",
            "Recuperar pedidos desde archivo de texto",
            "Salir"
        };

        int opcion = -1; // Valor predeterminado para evitar errores

        do {
            // Construye un mensaje con saltos de línea para mostrar las opciones verticalmente
            //tambien sirve para seleccionaruna opcion
            StringBuilder menuMensaje = new StringBuilder("Seleccione una opción:\n");
            for (int i = 0; i < opciones.length; i++) {
                menuMensaje.append(i).append(". ").append(opciones[i]).append("\n");
            }

            // Obtener la entrada del usuario
            String input = JOptionPane.showInputDialog(
                null,
                menuMensaje.toString(),
                "Gestión de Pedidos",
                JOptionPane.PLAIN_MESSAGE
            );

            if (input != null) { // Si el usuario no canceló
                try {
                    opcion = Integer.parseInt(input); // Convertir a entero
                } catch (NumberFormatException e) {
                    System.err.println("Entrada no válida. Ingrese un número.");
                    continue; // Volver a pedir la entrada tambien se puede utlizar un bucle 
                }

                // Switch para las diferentes opciones
                switch (opcion) {
                    case 0 -> crearPedido();
                    case 1 -> actualizarEstadoPedido();
                    case 2 -> consultarTodosPedidos();
                    case 3 -> consultarPedidoNumero();
                    case 4 -> consultarUltimoPedido();
                    case 5 -> eliminarTodosPedidos();
                    case 6 -> eliminarUnPedido();
                    case 7 -> eliminarUltimoPedido();
                    case 8 -> generarArchivoTextoDePedidos();
                    case 9 -> recuperarPedidosDesdeArchivoTexto();
                    case 10 -> {
                        JOptionPane.showMessageDialog(null, "Saliendo del programa.");
                        System.exit(0);
                    }
                    default -> System.err.println("Opción no válida."); //da error si digita mal 
                }
            } else {
                System.out.println("Operación cancelada por el usuario.");
                break; // Salir del bucle si el usuario cancela
            }

        } while (opcion != 10); // Continuar hasta que se seleccione "Salir"
    }
//se crea el pedido para que el usuario digite los datos correspondientes 
private static void crearPedido() {
   try {
       String idCliente = JOptionPane.showInputDialog("Ingrese la identificación del cliente:");
       String nombreCliente = JOptionPane.showInputDialog("Ingrese el nombre del cliente:");
       String direccionCliente = JOptionPane.showInputDialog("Ingrese la dirección del cliente:");
       String estadoPedido = JOptionPane.showInputDialog("Ingrese el estado del pedido,A: abierto, D: despachado, C: cancelado");
       String observacion = JOptionPane.showInputDialog("ingrese la observacion de el pedido : ");

       
               

       LocalDateTime fechaHora = null;
        boolean formatoValido = false; // si no hay fecha y hora da error porq el formato no es valido 

        while (!formatoValido) {
            try {
                LocalDateTime FechaHora = LocalDateTime.parse(JOptionPane.showInputDialog("Ingrese la fecha y hora (formato: yyyy-MM-dd T HH:mm):"));

                formatoValido = true;
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de fecha y hora inválido. Por favor, ingrese en el formato correcto (yyyy-MM-dd HH:mm).");
            }
        }

       char estado = estadoPedido.charAt(0);

       if (estado == 'A' || estado == 'D' || estado == 'C') {
           Cliente cliente = new Cliente(idCliente, nombreCliente, direccionCliente);

           LinkedList<ItemProducto> productos = new LinkedList<>();
           boolean agregarMas = true;

           while (agregarMas) {
               String codigoProducto = JOptionPane.showInputDialog("Ingrese el código del producto:");
               String nombreProducto = JOptionPane.showInputDialog("Ingrese el nombre del producto:");
               String cantidadProductoStr = JOptionPane.showInputDialog("Ingrese la cantidad del producto:");
               String precioProductoStr = JOptionPane.showInputDialog("Ingrese el precio del producto:");
               String presentacion = JOptionPane.showInputDialog(" ingrese la presentacion del producto");
               String tipo = JOptionPane.showInputDialog(" ingrese el tipo de producto ");
               
               try {
                   double cantidadProducto = Double.parseDouble(cantidadProductoStr);
                   double precioProducto = Double.parseDouble(precioProductoStr);
                   int codigoProductoInt = Integer.parseInt(codigoProducto);

                   productos.add(new ItemProducto (codigoProductoInt, nombreProducto, cantidadProducto,
                           presentacion, tipo, precioProducto));
               } catch (NumberFormatException e) {
                   JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, ingrese valores numéricos válidos.");
                   continue; // Salta a la siguiente iteración del bucle
               }

               int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea agregar otro producto?", "Agregar Producto", JOptionPane.YES_NO_OPTION);
               agregarMas = respuesta == JOptionPane.YES_OPTION;
           }

           Pedido pedido = new Pedido(losPedidos.size() + 1, fechaHora, cliente, productos, observacion, true, estado);
           losPedidos.add(pedido);

           guardarPedidoEnArchivo(pedido);

           JOptionPane.showMessageDialog(null, "Pedido creado y guardado con éxito!");
       } else {
           System.out.println("Estado de pedido inválido.");
       }
   } catch (HeadlessException e) {
       JOptionPane.showMessageDialog(null, "Error al crear el pedido: " + e.getMessage());
   }
}
    private static void guardarPedidoEnArchivo(Pedido pedido) {
    try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_PEDIDOS))) {
        for (Pedido p : losPedidos) {
            out.println("Pedido #" + p.getNumero());
            out.println("Cliente: " + p.getSuCliente().getNombre() + " - ID: " + p.getSuCliente().getIdentificacion() + " - Dirección: " + p.getSuCliente().getDireccion());
            out.println("Estado: " + p.getEstado());
            out.println("Productos:");
            for (ItemProducto item : p.getSusItemsProductos()) {
                out.println("   - " + item.getNombreP() + ", Código: " + item.getCodigo() + ", Cantidad: " + item.getCantidad() + ", Precio: $" + item.getPrecio());
            }
            out.println("Observaciones: " + p.getObservacion());
            out.println("--------------------------------------------------");
        }
        out.flush();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar los pedidos en el archivo: " + e.getMessage());
    }
}

private static void actualizarEstadoPedido() {
    try {
        // Solicita al usuario que ingrese el número del pedido que desea actualizar
        int numeroPedido = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número del pedido que desea actualizar:"));

        // Inicializa la variable pedidoEncontrado como null
        Pedido pedidoEncontrado = null;

        // Itera sobre la lista de pedidos
        for (Pedido pedido : losPedidos) {
            // Compara el número del pedido actual con el número ingresado por el usuario
            if (pedido.getNumero() == numeroPedido) {
                // Si coinciden, asigna el pedido actual a la variable pedidoEncontrado
                pedidoEncontrado = pedido;
                // Sale del bucle for al encontrar el pedido
                break;
            }
        }

        // Verifica si se encontró un pedido con el número ingresado
        if (pedidoEncontrado != null) {
            // Solicita al usuario que ingrese el nuevo estado del pedido
            String nuevoEstado = JOptionPane.showInputDialog(null,
                    "Ingrese el nuevo estado del pedido (D: Despachado, C: Cancelado):",
                    "Actualizar Estado del Pedido",
                    JOptionPane.QUESTION_MESSAGE);

            // Obtiene el primer carácter del estado ingresado por el usuario
            char estado = nuevoEstado.charAt(0);

            // Verifica si el estado ingresado es válido (D o C)
            if (estado == 'D' || estado == 'C') {
                // Actualiza el estado del pedido
                pedidoEncontrado.setEstado(estado);
                // Muestra un mensaje indicando que el estado se ha actualizado
                JOptionPane.showMessageDialog(null, "El estado del pedido ha sido actualizado.");
                // Llama al método actualizarArchivoPedido para actualizar el archivo del pedido
                actualizarArchivoPedido(pedidoEncontrado);
            } else {
                // Si el estado ingresado no es válido, muestra un mensaje de error
                JOptionPane.showMessageDialog(null, "Estado no válido. Ingrese A, D o C.");
            }
        } else {
            // Si no se encontró un pedido con el número ingresado, muestra un mensaje de error
            JOptionPane.showMessageDialog(null, "No se encontró el pedido con número " + numeroPedido);
        }
    } catch (NumberFormatException e) {
        // Si el usuario ingresa un valor no numérico para el número de pedido, muestra un mensaje de error
        JOptionPane.showMessageDialog(null, "Número de pedido no válido.");
    } catch (HeadlessException e) {
        // Captura una excepción específica relacionada con la interfaz gráfica de usuario
        JOptionPane.showMessageDialog(null, "Error al actualizar el estado del pedido: " + e.getMessage());
    }
}

    private static void actualizarArchivoPedido(Pedido pedido) {
    // Sobrescribe el archivo completo para actualizar el estado de un pedido específico
    try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_PEDIDOS))) {
        for (Pedido p : losPedidos) {
            out.println("Pedido #" + p.getNumero() + " - Fecha: " + p.getFechaHora());
            out.println("Cliente: " + p.getSuCliente().getNombre() + " - ID: " + p.getSuCliente
        ().getIdentificacion() + " - Dirección: " + p.getSuCliente().getDireccion());
            out.println("Estado: " + p.getEstado());
            for (ItemProducto item : p.getSusItemsProductos()) {
                out.println("Producto: " + item.getNombreP() + " - Código: " +
                        item.getCodigo() + " - Cantidad: " + item.getCantidad() + " - Precio: $" + item.getPrecio());
            }
            out.println("--------------------------------------------------");
        }
        out.flush();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar el archivo de pedidos: " + e.getMessage());
    }
}


    private static void consultarTodosPedidos() {
    if (losPedidos.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay pedidos para mostrar.");
        return;
    }
    
    StringBuilder sb = new StringBuilder();
    for (Pedido p : losPedidos) {
        sb.append("Pedido #").append(p.getNumero()).append("\n");
        sb.append("Fecha/Hora: ").append(p.getFechaHora()).append("\n");
        sb.append("Cliente: ").append(p.getSuCliente().getNombre()).append(" - ID: ").append(p.getSuCliente().getIdentificacion()).append("\n");
        sb.append("Dirección: ").append(p.getSuCliente().getDireccion()).append("\n");
        sb.append("Estado: ").append(p.getEstado()).append("\n");
        sb.append("Productos:\n");
        for (ItemProducto item : p.getSusItemsProductos()) {
            sb.append(" - ").append(item.getNombreP()).append(", Código: ").append(item.getCodigo())
              .append(", Cantidad: ").append(item.getCantidad()).append(", Precio: $").append(item.getPrecio()).append("\n");
        }
        sb.append("Observaciones: ").append(p.getObservacion()).append("\n");
        sb.append("--------------------------------------------------\n\n");
    }

    JTextArea textArea = new JTextArea(sb.toString());
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(500, 400));
    JOptionPane.showMessageDialog(null, scrollPane, "Todos los Pedidos", JOptionPane.INFORMATION_MESSAGE);
}


private static void consultarPedidoNumero() {
    // Solicita al usuario que ingrese el número del pedido que desea consultar
 int numeroPedido;
        try {
            numeroPedido = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número del pedido que desea consultar:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.");
            return;
        }

    try {
        // Convierte la cadena ingresada por el usuario en un número entero

        Pedido pedidoEncontrado = null;

        // Itera sobre la lista de pedidos
        for (Pedido p : losPedidos) {
            // Compara el número del pedido actual con el número ingresado por el usuario
            if (p.getNumero() == numeroPedido) {
                // Si coinciden, asigna el pedido actual a la variable pedidoEncontrado
                pedidoEncontrado = p;
                // Sale del bucle for al encontrar el pedido
                break;
            }
        }

        // Verifica si se encontró un pedido con el número ingresado
        if (pedidoEncontrado != null) {
            // Crea un objeto StringBuilder para construir la cadena de texto con la información del pedido
            StringBuilder sb = new StringBuilder();

            // Agrega la información básica del pedido al StringBuilder
            sb.append("Pedido #").append(pedidoEncontrado.getNumero()).append("\n");
            sb.append("Fecha/Hora: ").append(pedidoEncontrado.getFechaHora()).append("\n");
            sb.append("Cliente: ").append(pedidoEncontrado.getSuCliente().getNombre()).append(" - ID: ").append(pedidoEncontrado.getSuCliente().getIdentificacion()).append("\n");
            sb.append("Dirección: ").append(pedidoEncontrado.getSuCliente().getDireccion()).append("\n");
            sb.append("Estado: ").append(pedidoEncontrado.getEstado()).append("\n");
            sb.append("Observaciones: ").append(pedidoEncontrado.getObservacion()).append("\n");
            sb.append("Productos:\n");

            // Inicializa la variable valorTotalItems para almacenar el valor total de los ítems
            double valorTotalItems = 0;

            // Itera sobre los ítems del pedido
            for (ItemProducto item : pedidoEncontrado.getSusItemsProductos()) {
                // Agrega la información básica del ItemProducto al StringBuilder
                sb.append(" - ").append(item.getNombreP()).append(", Código: ").append(item.getCodigo())
                        .append(", Cantidad: ").append(item.getCantidad()).append(", Precio: $").append(item.getPrecio()).append("\n");

                // Determina el dato específico (presentación, tipo o porcentajeIva) del ItemProducto
                String datoEspecifico = "";
                if (item instanceof ItemProductoFarmacia) {
                    datoEspecifico = "Presentación: " + ((ItemProductoFarmacia) item).getPresentacion();
                } else if (item instanceof ItemProductoCanastaFamiliar) {
                    datoEspecifico = "Tipo: " + ((ItemProductoCanastaFamiliar) item).getTipo();
                } else if (item.getPorcentajeIva() != 0) {
                    datoEspecifico = "Porcentaje IVA: " + item.getPorcentajeIva() + "%";
                }

                // Si hay un dato específico, lo agrega al StringBuilder
                if (!datoEspecifico.isEmpty()) {
                    sb.append("   ").append(datoEspecifico).append("\n");
                }

                // Calcula el valor total del ItemProducto y lo suma al valor total de los ítems
                double valorTotalItem = item.getCantidad() * item.getPrecio();
                valorTotalItems += valorTotalItem;
            }

            // Agrega la cantidad de ítems del pedido, el valor total de los ítems y el valor total a pagar al StringBuilder
            sb.append("Cantidad de ítems de pedido: ").append(pedidoEncontrado.getSusItemsProductos().size()).append("\n");
            sb.append("Valor total de los ítems: $").append(valorTotalItems).append("\n");
            sb.append("Valor total a pagar: $").append(valorTotalItems); // Suponiendo que no hay descuentos

            // Crea un JTextArea con el texto construido en el StringBuilder
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false); // Hace que el área de texto no sea editable

            // Crea un JScrollPane para hacer que el área de texto sea desplazable
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300)); // Establece un tamaño preferido para el JScrollPane

            // Muestra el JScrollPane en un cuadro de diálogo con un título y un icono de información
            JOptionPane.showMessageDialog(null, scrollPane, "Consulta de Pedido", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Si no se encontró un pedido con el número ingresado, muestra un mensaje de advertencia
            JOptionPane.showMessageDialog(null, "No se encontró un pedido con el número " + numeroPedido);
        }
    } catch (NumberFormatException e) {
        // Si el usuario ingresa un valor no numérico, muestra un mensaje de error
        JOptionPane.showMessageDialog(null, "Número de pedido no válido. Por favor, ingrese un número entero.");
    }
}


private static void consultarUltimoPedido() {
    // Primero, verificamos si la lista de pedidos está vacía
    if (losPedidos.isEmpty()) {
        // Si está vacía, mostramos un mensaje de advertencia y salimos del método
        JOptionPane.showMessageDialog(null, "No hay pedidos para mostrar.");
        return;
    }

    // Si hay pedidos, obtenemos el último pedido de la lista
    Pedido ultimoPedido = losPedidos.getLast();

    // Creamos un objeto StringBuilder para construir una cadena de texto
    StringBuilder sb = new StringBuilder();

    // Agregamos la información básica del pedido al StringBuilder
    sb.append("Último Pedido:\n");
    sb.append("Pedido #").append(ultimoPedido.getNumero()).append("\n");
    sb.append("Fecha/Hora: ").append(ultimoPedido.getFechaHora()).append("\n");
    sb.append("Cliente: ").append(ultimoPedido.getSuCliente().getNombre())
            .append(" - ID: ").append(ultimoPedido.getSuCliente().getIdentificacion())
            .append(" - Dirección: ").append(ultimoPedido.getSuCliente().getDireccion()).append("\n");
    sb.append("Estado: ").append(ultimoPedido.getEstado()).append("\n");
    sb.append("Observaciones: ").append(ultimoPedido.getObservacion()).append("\n");

    // Agregamos la información de los productos del pedido
    sb.append("Productos:\n");
    double valorTotalItems = 0; // Variable para almacenar el valor total de los ítems

    // Iteramos sobre cada ItemProducto del pedido
    for (ItemProducto item : ultimoPedido.getSusItemsProductos()) {
        // Agregamos la información básica del ItemProducto al StringBuilder
        sb.append(" - ").append(item.getNombreP()).append(", Código: ").append(item.getCodigo())
                .append(", Cantidad: ").append(item.getCantidad()).append(", Precio: $").append(item.getPrecio()).append("\n");

        // Determinamos el dato específico (presentación, tipo o porcentajeIva) del ItemProducto
        String datoEspecifico = "";
        if (item instanceof ItemProductoFarmacia) {
            datoEspecifico = "Presentación: " + ((ItemProductoFarmacia) item).getPresentacion();
        } else if (item instanceof ItemProductoCanastaFamiliar) {
            datoEspecifico = "Tipo: " + ((ItemProductoCanastaFamiliar) item).getTipo();
        } else if (item.getPorcentajeIva() != 0) {
            datoEspecifico = "Porcentaje IVA: " + item.getPorcentajeIva() + "%";
        }

        // Si hay un dato específico, lo agregamos al StringBuilder
        if (!datoEspecifico.isEmpty()) {
            sb.append("   ").append(datoEspecifico).append("\n");
        }

        // Calculamos el valor total del ItemProducto y lo sumamos al valor total de los ítems
        double valorTotalItem = item.getCantidad() * item.getPrecio();
        valorTotalItems += valorTotalItem;
    }

    // Agregamos la cantidad de ítems del pedido y el valor total de los ítems al StringBuilder
    sb.append("Cantidad de ítems de pedido: ").append(ultimoPedido.getSusItemsProductos().size()).append("\n");
    sb.append("Valor total de los ítems: $").append(valorTotalItems).append("\n");
    sb.append("Valor total a pagar: $").append(valorTotalItems); // Suponiendo que no hay descuentos

    // Creamos un JTextArea con el texto construido en el StringBuilder
    JTextArea textArea = new JTextArea(sb.toString());
    textArea.setEditable(false); // Hacemos que el área de texto no sea editable

    // Creamos un JScrollPane para hacer que el área de texto sea desplazable
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(500, 300)); // Establecemos un tamaño preferido para el JScrollPane

    // Mostramos el JScrollPane en un cuadro de diálogo con un título y un icono de información
    JOptionPane.showMessageDialog(null, scrollPane, "Consulta del Último Pedido", JOptionPane.INFORMATION_MESSAGE);
}


    private static void eliminarTodosPedidos() {
    int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar todos los pedidos?",
                                                "Eliminar Todos los Pedidos", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (confirm == JOptionPane.YES_OPTION) {
        losPedidos.clear();  // Limpia la lista de todos los pedidos
        eliminarArchivoPedidos();  // Opcional: Eliminar o limpiar el archivo de pedidos
        JOptionPane.showMessageDialog(null, "Todos los pedidos han sido eliminados.");
    } else {
        JOptionPane.showMessageDialog(null, "Operación cancelada.");
    }
}

    private static void eliminarArchivoPedidos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_PEDIDOS))) {
            writer.print("");  // Sobrescribe el archivo con una cadena vacía, efectivamente limpiándolo
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar el archivo de pedidos: " + e.getMessage());
        }
    }


    private static void eliminarUnPedido() {
    String numeroPedidoStr = JOptionPane.showInputDialog("Ingrese el número del pedido que desea eliminar:");
    if (numeroPedidoStr == null || numeroPedidoStr.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "No se ingresó ningún número de pedido.");
        return;
    }

    try {
        int numeroPedido = Integer.parseInt(numeroPedidoStr);
        boolean encontrado = false;

        for (int i = 0; i < losPedidos.size(); i++) {
            if (losPedidos.get(i).getNumero() == numeroPedido) {
                int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar el pedido número " + numeroPedido + "?",
                                                            "Eliminar Pedido", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    losPedidos.remove(i);
                    actualizarArchivoTrasEliminar();  // Actualiza el archivo después de eliminar un pedido
                    JOptionPane.showMessageDialog(null, "El pedido número " + numeroPedido + " ha sido eliminado.");
                } else {
                    JOptionPane.showMessageDialog(null, "Operación cancelada.");
                }
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró un pedido con el número " + numeroPedido);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Número de pedido no válido. Por favor, ingrese un número entero.");
    }
    }
    private static void actualizarArchivoTrasEliminar() {
        // Reescribir el archivo completo para actualizar los cambios
        try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_PEDIDOS))) {
            for (Pedido p : losPedidos) {
                out.println("Pedido #" + p.getNumero() + " - Fecha: " + p.getFechaHora());
                out.println("Cliente: " + p.getSuCliente().getNombre() + " - ID: " + p.getSuCliente().getIdentificacion() + " - Dirección: " + p.getSuCliente().getDireccion());
                out.println("Estado: " + p.getEstado());
                for (ItemProducto item : p.getSusItemsProductos()) {
                    out.println("Producto: " + item.getNombreP() + " - Código: " + item.getCodigo() + " - Cantidad: " + item.getCantidad() + " - Precio: $" + item.getPrecio());
                }
                out.println("--------------------------------------------------");
            }
            out.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el archivo de pedidos: " + e.getMessage());
        }
}


    private static void eliminarUltimoPedido() {
    if (losPedidos.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay pedidos para eliminar.");
        return;
    }

    Pedido ultimoPedido = losPedidos.getLast(); // Obtener el último pedido
    int confirm = JOptionPane.showConfirmDialog(null, 
        "¿Está seguro de que desea eliminar el último pedido realizado (Pedido #" + ultimoPedido.getNumero() + ")?",
        "Eliminar Último Pedido", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
        losPedidos.removeLast(); // Elimina el último pedido de la lista
        actualizarArchivoTrasEliminar();  // Actualiza el archivo después de eliminar un pedido
        JOptionPane.showMessageDialog(null, "El último pedido ha sido eliminado.");
    } else {
        JOptionPane.showMessageDialog(null, "Operación cancelada.");
    }
}

private static void generarArchivoTextoDePedidos() {
    if (losPedidos.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay pedidos para exportar.");
        return;
    }

    try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_PEDIDOS, false))) {
        for (Pedido p : losPedidos) {
            out.println("Pedido #" + p.getNumero());
            out.println("Fecha/Hora: " + p.getFechaHora());

            // Datos del cliente
            Cliente cliente = p.getSuCliente();
            out.println("Cliente: " + cliente.getNombre() + " - ID: " + " - Dirección: " + cliente.getDireccion());

            // Estado del pedido
            out.println("Estado: " + p.getEstado());

            // Listado de productos del pedido
            out.println("Productos:");
            for (ItemProducto item : p.getSusItemsProductos()) {
                out.println("   - " + item.getNombreP() + ", Código: " + item.getCodigo() + ", Cantidad: " + item.getCantidad() + ", Precio: $" + item.getPrecio());
            }

            // Observaciones y separación entre pedidos
            out.println("Observaciones: " + p.getObservacion());
            out.println("--------------------------------------------------");
        }

        JOptionPane.showMessageDialog(null, "Archivo de pedidos generado con éxito.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al escribir el archivo de pedidos: " + e.getMessage());
    }
}
//faltan las observaciones 

//ser mas especifico con lasdirecciones 
    

//malo
private static void recuperarPedidosDesdeArchivoTexto() {
    // Limpiar la lista actual para evitar duplicados
    losPedidos.clear();

    // Manejo de recursos con try-with-resources
    try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_PEDIDOS))) {
        String line; // Variable para almacenar cada línea del archivo
        Pedido pedido = null; // Variable para el pedido actual
        LinkedList<ItemProducto> productos = new LinkedList<>(); // Lista de productos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Formato de fecha/hora

        // Leer cada línea del archivo y procesar datos
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Pedido #")) { // Indica el inicio de un nuevo pedido
                // Guardar el pedido anterior si existe
                if (pedido != null) {
                    pedido.setSusItemsProductos(productos); // Asignar la lista de productos al pedido
                    losPedidos.add(pedido); // Añadir a la lista de pedidos
                }

                // Crear un nuevo pedido
                int numeroPedido = Integer.parseInt(line.split("#")[1].trim());
                pedido = new Pedido(numeroPedido, null, null, null, null, false, ' '); // Nuevo pedido
                productos = new LinkedList<>(); // Resetear la lista de productos
            } else if (line.startsWith("Fecha/Hora:")) { // Información de fecha y hora
                LocalDateTime fechaHora = LocalDateTime.parse(line.split(":")[1].trim(), formatter);
                pedido.setFechaHora(fechaHora); // Asignar la fecha/hora
            } else if (line.startsWith("Cliente:")) { // Información del cliente
                String[] partes = line.split(":")[1].split("-");
                String nombreCliente = partes[0].trim();
                String numeroIdentificacion = partes[1].trim();
                String direccionCliente = partes[2].trim();
                Cliente cliente = new Cliente(numeroIdentificacion, nombreCliente, direccionCliente); // Crear cliente
                pedido.setSuCliente(cliente); // Asignar el cliente al pedido
            } else if (line.startsWith("Estado:")) { // Estado del pedido
                char estado = line.split(":")[1].trim().charAt(0); // Obtener el primer carácter
                pedido.setEstado(estado); // Asignar el estado
            } else if (line.startsWith("Observaciones:")) { // Observaciones del pedido
                String observacion = line.split(":")[1].trim();
                pedido.setObservacion(observacion); // Asignar observación
            } else if (line.startsWith("Productos:")) { // Sección de productos
                // Indica el inicio de la sección de productos, no es necesario hacer nada
            } else if (line.startsWith("   -")) { // Línea de producto
                String[] partes = line.substring(4).split(","); // Eliminar espacios iniciales
                String nombreProducto = partes[0].trim();
                String codigoProducto = partes[1].split(":")[1].trim();
                int cantidadProducto = Integer.parseInt(partes[2].split(":")[1].trim());
                double precioProducto = Double.parseDouble(partes[3].split(":")[1].trim().substring(1)); // Quitar el símbolo de dólar
                productos.add(new ItemProducto(codigoProducto, nombreProducto, cantidadProducto, precioProducto)); // Añadir el producto
            }
        }

        // Guardar el último pedido leído
        if (pedido != null) {
            pedido.setSusItemsProductos(productos);
            losPedidos.add(pedido); // Añadir el pedido a la lista
        }

        JOptionPane.showMessageDialog(null, "Pedidos cargados desde el archivo con éxito.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al leer el archivo de pedidos: " + e.getMessage());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al procesar el archivo de pedidos: " + e.getMessage());
    }
}
}








