/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.DatoContableReembolso;
import com.idebsystems.serviciosweb.util.Persistencia;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author jorge
 */
public class DatoContableReembolsoDAO extends Persistencia {
    
    private static final Logger LOGGER = Logger.getLogger(DatoContableReembolsoDAO.class.getName());
    
    public DatoContableReembolso guardarDatosContabilidad(DatoContableReembolso data) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

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
    
    public DatoContableReembolso getDatoPorIdReembolso(long idReembolso) throws Exception {
        try {

            getEntityManager();

            Query query = em.createQuery("FROM DatoContableReembolso d WHERE d.idReembolso = :idReembolso");
            query.setParameter("idReembolso", idReembolso);
            
            List<DatoContableReembolso> lista = query.getResultList();
            
            if(lista.isEmpty()){
                return null;
            }
            else{
                return lista.get(0);
            }

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
    
    
}
