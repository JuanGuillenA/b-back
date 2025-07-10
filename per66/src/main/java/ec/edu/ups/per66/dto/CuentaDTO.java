// src/main/java/ec/edu/ups/per66/dto/CuentaDTO.java
package ec.edu.ups.per66.dto;

import java.math.BigDecimal;

public class CuentaDTO {
    private String cedulaPersona;
    private String numeroCuenta;
    private BigDecimal saldo;

    public CuentaDTO() {}

    public CuentaDTO(String cedulaPersona, String numeroCuenta, BigDecimal saldo) {
        this.cedulaPersona = cedulaPersona;
        this.numeroCuenta  = numeroCuenta;
        this.saldo         = saldo;
    }

    public String getCedulaPersona() { return cedulaPersona; }
    public void setCedulaPersona(String cedulaPersona) {
        this.cedulaPersona = cedulaPersona;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}
