package ec.edu.ups.per66.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "persona")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "persona_cedula", length = 10, nullable = false, unique = true)
    private String cedula;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "direccion", length = 150)
    private String direccion;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "telefono", length = 15)
    private String telefono;

 // --- Esta es la lista de cuentas que provoca el error ---
    @OneToMany(mappedBy="titular", fetch=FetchType.EAGER)
    private List<Cuenta> cuentas;

    public Persona() { }

    public Persona(String cedula, String nombre, String direccion, String email, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    /**
     * Método auxiliar para mantener la relación bidireccional
     */
    public void addCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
        cuenta.setTitular(this);
    }

    public void removeCuenta(Cuenta cuenta) {
        cuentas.remove(cuenta);
        cuenta.setTitular(null);
    }

    @Override
    public String toString() {
        return "Persona{" +
               "cedula='" + cedula + '\'' +
               ", nombre='" + nombre + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}
