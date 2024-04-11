package edu.escuelaing.arem.ASE.app;

import java.util.Date;

public class Ticket {
    private int id;
    private String duenoMarca;
    private String url;
    private String estado;
    private Date fechaCreacion;
    private String proveedor;
    private String correoProveedor;
    private String correoDuenoMarca;

    public Ticket() {
        this.fechaCreacion = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuenoMarca() {
        return duenoMarca;
    }

    public void setDuenoMarca(String duenoMarca) {
        this.duenoMarca = duenoMarca;
    }

    public String getCorreoProveedor() {
        return correoProveedor;
    }

    public void setCorreoProveedor(String correoProveedor) {
        this.correoProveedor = correoProveedor;
    }

    public String getCorreoDuenoMarca() {
        return correoDuenoMarca;
    }

    public void setCorreoDuenoMarca(String correoDuenoMarca) {
        this.correoDuenoMarca = correoDuenoMarca;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
