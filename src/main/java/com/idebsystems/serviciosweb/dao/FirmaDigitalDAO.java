/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.FirmaDigital;
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
 * @author jorge
 */
public class FirmaDigitalDAO extends Persistencia {
 
    private static final Logger LOGGER = Logger.getLogger(FirmaDigitalDAO.class.getName());
    
    public List<FirmaDigital> listarFirmas() throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM FirmaDigital f order by f.fechaCaduca");

            List<FirmaDigital> lista = query.getResultList();

            return lista;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public FirmaDigital guardarFirmaDigital(FirmaDigital data) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();
            
            //colocar como inactivas todas las firmas existentes del mismo usuario
            Query query = em.createQuery("UPDATE FirmaDigital f SET f.idEstado = 0 WHERE f.idUsuario = :idUsuario");
            query.setParameter("idUsuario", data.getIdUsuario());
            query.executeUpdate();
            

            if (Objects.nonNull(data.getId()) && data.getId() > 0) {
                em.merge(data); //update
            } else {
                em.persist(data); //insert
            }
            em.flush(); //Confirmar el insert o update

            em.getTransaction().commit();

            return data;

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
    
    
    public FirmaDigital getFirmaActivaPorIdUsuario(Long idUsuario) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM FirmaDigital f WHERE f.idUsuario = :idUsuario AND f.idEstado = 1");
            query.setParameter("idUsuario", idUsuario);

            List<FirmaDigital> lista = query.getResultList();

            if(lista.isEmpty()){
                return null;
            }
            else{
                return lista.get(0);
            }

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    public List<FirmaDigital> listarFirmasPorUsuario(long idUsuario) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM FirmaDigital f WHERE f.idUsuario = :idUsuario order by f.fechaCaduca");
            query.setParameter("idUsuario", idUsuario);

            List<FirmaDigital> lista = query.getResultList();

            return lista;

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
