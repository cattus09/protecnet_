package edu.escuelaing.arem.ASE.app.controller;

import static spark.Spark.*;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.google.gson.Gson;

import edu.escuelaing.arem.ASE.app.Ticket;
import edu.escuelaing.arem.ASE.app.config.MongoDBConfig;
import edu.escuelaing.arem.ASE.app.services.CorreoService;

public class CrearTicketController {

    private static List<Ticket> tickets = new ArrayList<>();
    private static int ticketId = 0;
    public static void main(String[] args) {
        port(getPort());
        staticFiles.location("/public");

        get("/ingresarDatos", (req, res) -> {
            res.redirect("index.html");
            return null;
        });

        get("/crearTicket", (req, res) -> {
            String datos = req.queryParams("datos");
            String[] lineas = datos.split("\n");
            for (String linea : lineas) {
                String url = linea.trim(); // Eliminar espacios al inicio y final
                if (!url.isEmpty()) { // Verificar si la URL no está vacía
                    Ticket ticket = new Ticket();
                    ticket.setId(++ticketId); // Incrementar y asignar el nuevo ID único
                    ticket.setDuenoMarca("Bancolombia"); // Establecer proveedor
                    ticket.setURL(url); // Establecer URL 
                    ticket.setEstado("Nuevo"); // Estado por defecto: Nuevo
                    ticket.setCorreoProveedor("sergio.gv.9@hotmail.com"); // Establecer correo del proveedor
                    ticket.setProveedor("cloudflare"); // Establecer dueño de marca
                    ticket.setCorreoDuenoMarca("sergio.gonzalez-v@mail.escuelaing.edu.co");
                    MongoDBConfig.guardarTicketEnBaseDeDatos(ticket); // Guardar en la base de datos
                    eliminarTicketLocal(ticket); // Eliminar de la lista local de tickets (si aplica)
                }
            }
            res.redirect("index.html");
            return null;
        });

        get("/mostrarTickets", (req, res) -> {
            res.redirect("tickets.html");
            return null;
        });

        get("/obtenerTickets", (req, res) -> {
            List<Document> tickets = MongoDBConfig.obtenerTicketsDeBaseDeDatos();
            Gson gson = new Gson();
            return gson.toJson(tickets); 
        });

        get("/cambiarEstado", (req, res) -> {
            String id = req.queryParams("id");
            String nuevoEstado = req.queryParams("estado");
            String url = req.queryParams("url");
            actualizarEstadoDelTicket(id, nuevoEstado, url);
            return null;
        });

    }

    /**
     * Elimina un ticket de la lista local de tickets.
     * @param ticket el ticket a eliminar
     */
    public static void eliminarTicketLocal(Ticket ticket) {
        tickets.removeIf(t -> t.getId() == ticket.getId());
    }

    public static void actualizarEstadoDelTicket(String id, String nuevoEstado, String url) {
    try {
        MongoDBConfig.actualizarEstadoDelTicket(id, nuevoEstado);
        if (nuevoEstado.equals("Espera de confirmacion")) {
            String correoDuenoMarca = "sergio.gv.9@hotmail.com";
            String mensaje = "Saludo, \nEl contenido de esta URL \n"+ url +"\nNos pareció de carácter sospechoso, ¿procedemos con la eliminación de la página?";
            CorreoService.enviarCorreo(correoDuenoMarca, "Encontramos una pagina maliciosa", mensaje);
        }
        else if (nuevoEstado.equals("Aceptado")) {
            String correoProveedor = "sergio.gv.9@hotmail.com";
            String mensaje = "Saludo, \nEncontramos una página de phishing la cual tiene a su empresa como servidor de hosting, esta página se encuentra es esta URL\n"+ url + 
                                "\nPor favor elimina esta pagina \nCordialmente,\n Proteger";
            CorreoService.enviarCorreo(correoProveedor, "Cambio de estado de ticket", mensaje);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

}