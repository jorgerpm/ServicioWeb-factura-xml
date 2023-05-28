/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.Parametro;
import com.idebsystems.serviciosweb.util.Persistencia;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author israe
 */
public class ParametroDAO extends Persistencia {
    private static final Logger LOGGER = Logger.getLogger(ParametroDAO.class.getName());
    
    public List<Parametro> listarParametros() throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Parametro p order by p.nombre");

            List<Parametro> listaParametro = query.getResultList();

            return listaParametro;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public Parametro guardarParametro(Parametro parametro) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(parametro.getId()) && parametro.getId() > 0) {
                em.merge(parametro); //update
            } else {
                em.persist(parametro); //insert
            }
            em.flush(); //Confirmar el insert o update

            em.getTransaction().commit();

            return parametro;

        } catch (SQLException exc) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } catch (Exception exc) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    public Parametro buscarParametroPorNombre(String nombre) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Parametro p WHERE p.nombre = :nombre ");
            query.setParameter("nombre", nombre);

            return (Parametro)query.getSingleResult();

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
}
