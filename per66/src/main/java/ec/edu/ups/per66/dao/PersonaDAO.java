package ec.edu.ups.per66.dao;

import java.util.List;

import ec.edu.ups.per66.modelo.Persona;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class PersonaDAO {

    @PersistenceContext
    private EntityManager em;

    public void insert(Persona p) {
        em.persist(p);
    }

    public void update(Persona p) {
        em.merge(p);
    }

    public Persona read(String cedula) {
        return em.find(Persona.class, cedula);
    }

    public void delete(String cedula) {
        Persona p = read(cedula);
        if (p != null) {
            em.remove(p);
        }
    }

    public List<Persona> getAll() {
        return em.createQuery("SELECT p FROM Persona p", Persona.class)
                 .getResultList();
    }

    // Nuevo: buscar por email
    public List<Persona> findByEmail(String email) {
        TypedQuery<Persona> q = em.createQuery(
            "SELECT p FROM Persona p WHERE p.email = :email", Persona.class);
        q.setParameter("email", email);
        return q.getResultList();
    }
}