// src/main/java/ec/edu/ups/per66/dao/TransferenciaDAO.java
package ec.edu.ups.per66.dao;

import java.util.List;
import ec.edu.ups.per66.modelo.Transferencia;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class TransferenciaDAO {

    @PersistenceContext
    private EntityManager em;

    public void insert(Transferencia t) {
        em.persist(t);
    }

    public List<Transferencia> getAll() {
        return em.createQuery("SELECT t FROM Transferencia t", Transferencia.class)
                 .getResultList();
    }


    public List<Transferencia> findByCuenta(String numeroCuenta) {
        return em.createQuery(
            "SELECT t FROM Transferencia t " +
            "  JOIN FETCH t.cuentaOrigen " +
            "  JOIN FETCH t.cuentaOrigen.titular " +
            "  JOIN FETCH t.cuentaDestino " +
            "  JOIN FETCH t.cuentaDestino.titular " +
            " WHERE t.cuentaOrigen.numeroCuenta = :num " +
            "    OR t.cuentaDestino.numeroCuenta = :num",
            Transferencia.class
        )
        .setParameter("num", numeroCuenta)
        .getResultList();
    }
}
