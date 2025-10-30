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
public class ClsSeguimiento_jvl {
    
    private int idSeguimiento;
    private int idReclamo;
    private int idUsuario;
    private java.sql.Timestamp fecha;
    private String observacion;
    private String nuevoEstado; // "Pendiente", "En atención", "Resuelto"

    public ClsSeguimiento_jvl() {
    }

    public ClsSeguimiento_jvl(int idSeguimiento, int idReclamo, int idUsuario, Timestamp fecha, String observacion, String nuevoEstado) {
        this.idSeguimiento = idSeguimiento;
        this.idReclamo = idReclamo;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.observacion = observacion;
        this.nuevoEstado = nuevoEstado;
    }

    public int getIdSeguimiento() {
        return idSeguimiento;
    }

    public void setIdSeguimiento(int idSeguimiento) {
        this.idSeguimiento = idSeguimiento;
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

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    
    
    
}
