/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.RazonRechazo;
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
public class RazonRechazoDAO extends Persistencia {
    private static final Logger LOGGER = Logger.getLogger(RazonRechazoDAO.class.getName());
    
    public List<RazonRechazo> listarRazonesRechazo(boolean estado) throws Exception {
        try {
            getEntityManager();

            String sql = "FROM RazonRechazo r ";
            if(estado){
                sql += " WHERE r.idEstado = 1 ";
            }
            sql += " order by r.razon";
            
            Query query = em.createQuery(sql);

            List<RazonRechazo> listaRazonRechazo = query.getResultList();

            return listaRazonRechazo;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public RazonRechazo guardarRazonRechazo(RazonRechazo razonRechazo) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(razonRechazo.getId()) && razonRechazo.getId() > 0) {
                em.merge(razonRechazo); //update
            } else {
                em.persist(razonRechazo); //insert
            }
            em.flush(); //Confirmar el insert o update

            em.getTransaction().commit();

            return razonRechazo;

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
    
    
    public List<RazonRechazo> buscarRechazoActivos() throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM RazonRechazo r WHERE r.idEstado = 1 ");

            return query.getResultList();

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
