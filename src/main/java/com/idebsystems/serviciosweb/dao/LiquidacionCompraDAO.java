/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. (C) 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.LiquidacionCompra;
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
public class LiquidacionCompraDAO extends Persistencia {
    
    private static final Logger LOGGER = Logger.getLogger(LiquidacionCompraDAO.class.getName());
    
    public LiquidacionCompra guardarLiquidacion(LiquidacionCompra data) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();
            
            //primero buscar si ya existe el la liquidacion con ese reembolso
            

            if (Objects.nonNull(data.getId()) && data.getId() > 0) {
                em.merge(data); //update
            } else {
                em.persist(data); //insert
            }

            em.getTransaction().commit();

            return data;

        } catch (SQLException exc) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } catch (Exception exc) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, null, exc);
            if(exc.getMessage().contains("liquidacioncompra_idreembolso_uk")){
                throw new Exception("Ya existe la liquidacion asociada a un reembolso. " + data.getIdReembolso());
            }
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    public LiquidacionCompra getLiquidacionPorId(Long id) throws Exception {
        try {
            getEntityManager();

            return em.find(LiquidacionCompra.class, id);

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public LiquidacionCompra getLiquidacionPorReembolso(Long idReembolso) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM LiquidacionCompra l where l.idReembolso = :idReembolso");
            query.setParameter("idReembolso", idReembolso);
            
            List<LiquidacionCompra> data = query.getResultList();
            
            if(data.isEmpty()){
                return null;
            }
            else{
                return data.get(0);
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
    
    
    public boolean tieneLiquidacionesPendientesUsuario(Long idUsuarioCarga) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("SELECT count(l.id) FROM LiquidacionCompra l INNER JOIN DocumentoReembolsos d ON l.idReembolso = d.id "
                    + " WHERE l.estado = :estado AND d.usuarioCarga = :idUsuarioCarga ");
            
            query.setParameter("estado", "PENDIENTE");
            query.setParameter("idUsuarioCarga", idUsuarioCarga);
            
            List<Long> data = query.getResultList();
            
            if(data.isEmpty()){
                return false;
            }
            else{
                return Objects.nonNull(data.get(0)) && data.get(0) > 0;
            }

       } catch (NoResultException exc) {
            return false;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
}
