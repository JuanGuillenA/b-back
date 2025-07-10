package ec.edu.ups.per66.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "Transferencia")
public class Transferencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transferencia_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="cuenta_origen_id")
    private Cuenta cuentaOrigen;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="cuenta_destino_id")
    private Cuenta cuentaDestino;

    @Column(nullable = false)
    private BigDecimal monto;

    // Guardamos fecha+hora, sólo en insert (no se sobreescribe)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date fecha;

    public Transferencia() {}

    // Antes del INSERT fijamos fecha = ahora
    @PrePersist
    private void onPrePersist() {
        this.fecha = new Date();
    }

    // ——— Getters & Setters ———

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

}
