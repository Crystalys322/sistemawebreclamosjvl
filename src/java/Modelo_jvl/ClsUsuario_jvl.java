/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo_jvl;

/**
 *
 * @author HP
 */
public class ClsUsuario_jvl {
    
    private int idUsuario;
    private String nombre;
    private String correo;
    private String password;
    private int idRol;
    private String ipAutorizada;

    public ClsUsuario_jvl() {
    }

    public ClsUsuario_jvl(int idUsuario, String nombre, String correo, String password, int idRol, String ipAutorizada) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.idRol = idRol;
        this.ipAutorizada = ipAutorizada;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getIpAutorizada() {
        return ipAutorizada;
    }

    public void setIpAutorizada(String ipAutorizada) {
        this.ipAutorizada = ipAutorizada;
    }

    
    
    
}
