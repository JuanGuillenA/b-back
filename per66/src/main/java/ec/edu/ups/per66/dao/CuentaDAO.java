// src/main/java/ec/edu/ups/per66/dao/CuentaDAO.java
package ec.edu.ups.per66.dao;

import java.util.List;
import ec.edu.ups.per66.dto.CuentaDTO;
import ec.edu.ups.per66.modelo.Cuenta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class CuentaDAO {
    @PersistenceContext
    private EntityManager em;

    // --- tus métodos actuales ---
    public void insert(Cuenta c) { em.persist(c); }
    public void update(Cuenta c) { em.merge(c); }
    public Cuenta read(Long id) { return em.find(Cuenta.class, id); }
    public Cuenta findByNumero(String numero) {
        TypedQuery<Cuenta> q = em.createQuery(
            "SELECT c FROM Cuenta c WHERE c.numeroCuenta = :num", Cuenta.class);
        q.setParameter("num", numero);
        return q.getResultStream().findFirst().orElse(null);
    }
    public List<Cuenta> getAll() {
        return em.createQuery(
            "SELECT c FROM Cuenta c JOIN FETCH c.titular", Cuenta.class)
          .getResultList();
    }

    // --- nuevos métodos que devuelven DTO ---
    public List<CuentaDTO> listarTodasComoDTO() {
        return em.createQuery(
            "SELECT new ec.edu.ups.per66.dto.CuentaDTO(c.numeroCuenta, c.titular.cedula) "
          + "FROM Cuenta c",
            CuentaDTO.class
        ).getResultList();
    }

    public CuentaDTO buscarPorNumeroComoDTO(String numero) {
        return em.createQuery(
            "SELECT new ec.edu.ups.per66.dto.CuentaDTO(c.numeroCuenta, c.titular.cedula) "
          + "FROM Cuenta c WHERE c.numeroCuenta = :num",
            CuentaDTO.class
        )
        .setParameter("num", numero)
        .getSingleResult();
    }
}
