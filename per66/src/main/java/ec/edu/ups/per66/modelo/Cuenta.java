package ec.edu.ups.per66.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cuenta_id")
    private Long id;

    @Column(name="numero_cuenta", unique = true, nullable = false)
    private String numeroCuenta;

    @ManyToOne
    @JoinColumn(name="persona_cedula")
    private Persona titular;

    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cuentaOrigen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transferencia> transferenciasOrigen = new ArrayList<>();

    @OneToMany(mappedBy = "cuentaDestino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transferencia> transferenciasDestino = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public Persona getTitular() {
		return titular;
	}

	public void setTitular(Persona titular) {
		this.titular = titular;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public List<Transferencia> getTransferenciasOrigen() {
		return transferenciasOrigen;
	}

	public void setTransferenciasOrigen(List<Transferencia> transferenciasOrigen) {
		this.transferenciasOrigen = transferenciasOrigen;
	}

	public List<Transferencia> getTransferenciasDestino() {
		return transferenciasDestino;
	}

	public void setTransferenciasDestino(List<Transferencia> transferenciasDestino) {
		this.transferenciasDestino = transferenciasDestino;
	}
    
    
}
