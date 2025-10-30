/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo_jvl;

import java.sql.Timestamp;

/**
 *
 * @author HP
 */
public class ClsReclamo_jvl {
    
    private int idReclamo;
    private int idUsuario;
    private int idCategoria;
    private String descripcion;
    private java.sql.Timestamp fechaRegistro;
    private String estado; // "Pendiente", "En atención", "Resuelto"

    public ClsReclamo_jvl() {
    }

    public ClsReclamo_jvl(int idReclamo, int idUsuario, int idCategoria, String descripcion, Timestamp fechaRegistro, String estado) {
        this.idReclamo = idReclamo;
        this.idUsuario = idUsuario;
        this.idCategoria = idCategoria;
        this.descripcion = descripcion;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
    }

    public int getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(int idReclamo) {
        this.idReclamo = idReclamo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    
    
}
