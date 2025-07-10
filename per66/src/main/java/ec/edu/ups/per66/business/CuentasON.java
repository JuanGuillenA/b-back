package ec.edu.ups.per66.business;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import ec.edu.ups.per66.dao.CuentaDAO;
import ec.edu.ups.per66.dao.PersonaDAO;
import ec.edu.ups.per66.dao.TransferenciaDAO;
import ec.edu.ups.per66.dto.TransferenciaDTO;
import ec.edu.ups.per66.modelo.Cuenta;
import ec.edu.ups.per66.modelo.Persona;
import ec.edu.ups.per66.modelo.Transferencia;

@Stateless
public class CuentasON {

    @Inject private CuentaDAO cuentaDAO;
    @Inject private PersonaDAO personaDAO;
    @Inject private TransferenciaDAO transferenciaDAO;

    public Cuenta crearCuenta(String cedulaTitular, String numeroCuenta) throws Exception {
        Persona titular = personaDAO.read(cedulaTitular);
        if (titular == null) throw new Exception("Titular no encontrado");
        if (cuentaDAO.findByNumero(numeroCuenta) != null)
            throw new Exception("Número de cuenta ya existe");
        Cuenta c = new Cuenta();
        c.setNumeroCuenta(numeroCuenta);
        c.setTitular(titular);
        cuentaDAO.insert(c);
        return c;
    }

    public void depositar(String numeroCuenta, BigDecimal monto) throws Exception {
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Monto debe ser positivo");
        Cuenta c = cuentaDAO.findByNumero(numeroCuenta);
        if (c == null) throw new Exception("Cuenta origen no existe");
        c.setSaldo(c.getSaldo().add(monto));
        cuentaDAO.update(c);
    }

    public void retirar(String numeroCuenta, BigDecimal monto) throws Exception {
        Cuenta c = cuentaDAO.findByNumero(numeroCuenta);
        if (c == null) 
            throw new Exception("Cuenta no existe");
        if (c.getSaldo().compareTo(BigDecimal.ZERO) == 0)
            throw new Exception("Saldo en cero, no se puede realizar la transacción");
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Monto debe ser positivo");
        if (c.getSaldo().compareTo(monto) < 0)
            throw new Exception("Fondos insuficientes");
        c.setSaldo(c.getSaldo().subtract(monto));
        cuentaDAO.update(c);
    }

    public Transferencia transferir(String origen, String destino, BigDecimal monto) throws Exception {
        Cuenta cOrigen = cuentaDAO.findByNumero(origen);
        if (cOrigen == null) 
            throw new Exception("Cuenta origen no existe");
        if (cOrigen.getSaldo().compareTo(BigDecimal.ZERO) == 0)
            throw new Exception("Saldo en cero, no se puede realizar la transacción");
        // reutilizamos retirar/depositar para validaciones y actualización de saldo
        this.retirar(origen, monto);
        this.depositar(destino, monto);

        Cuenta cDestino = cuentaDAO.findByNumero(destino);
        Transferencia t = new Transferencia();
        t.setCuentaOrigen(cOrigen);
        t.setCuentaDestino(cDestino);
        t.setMonto(monto);
        transferenciaDAO.insert(t);
        return t;
    }

    public List<TransferenciaDTO> listarTransferenciasDTOPorCuenta(String numeroCuenta) {
        return transferenciaDAO.findByCuenta(numeroCuenta)
            .stream()
            .map(t -> new TransferenciaDTO(
                t.getId(),
                t.getCuentaOrigen().getNumeroCuenta(),
                t.getCuentaDestino().getNumeroCuenta(),
                t.getMonto(),
                t.getFecha()
            ))
            .collect(Collectors.toList());
    }

    public List<Cuenta> listarCuentas() {
        return cuentaDAO.getAll();
    }

    /**
     * Recupera una única cuenta por número.
     * Este método es el que llama tu BancoService en @Path("/cuentas/{numero}")
     */
    public Cuenta getCuenta(String numeroCuenta) {
        return cuentaDAO.findByNumero(numeroCuenta);
    }
}
