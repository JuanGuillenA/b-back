package ec.edu.ups.per66.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransferenciaDTO {
    private Long id;
    private String origen;
    private String destino;
    private BigDecimal monto;
    private Date fecha;

    // JSON-B necesita este constructor sin argumentos
    public TransferenciaDTO() {}
    
    // Para respuesta: incluye id y fecha
    public TransferenciaDTO(Long id, String origen, String destino,
                            BigDecimal monto, Date fecha) {
        this.id      = id;
        this.origen  = origen;
        this.destino = destino;
        this.monto   = monto;
        this.fecha   = fecha;
    }

    // getters y setters
    public Long getId()             { return id; }
    public void setId(Long id)      { this.id = id; }

    public String getOrigen()       { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino()      { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public BigDecimal getMonto()    { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public Date getFecha()          { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}
