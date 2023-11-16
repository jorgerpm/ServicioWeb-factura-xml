/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2023
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.TipoReembolso;
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
public class TipoReembolsoDAO extends Persistencia {
    
    private static final Logger LOGGER = Logger.getLogger(TipoReembolsoDAO.class.getName());
    
    public List<TipoReembolso> listarTipoReembolso(String esPrincipal) throws Exception {
        try {
            getEntityManager();
            
            StringBuilder sql = new StringBuilder("FROM TipoReembolso t ");
            
            if(Objects.nonNull(esPrincipal) && !esPrincipal.isBlank()){
                sql.append(" WHERE t.esPrincipal = :esPrincipal ");
            }
            sql.append(" order by t.tipo");
            
            Query query = em.createQuery(sql.toString());
            
            if(Objects.nonNull(esPrincipal) && !esPrincipal.isBlank()){
                query.setParameter("esPrincipal", Boolean.parseBoolean(esPrincipal));
            }

            List<TipoReembolso> lista = query.getResultList();

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
    
    public TipoReembolso guardarTipoReembolso(TipoReembolso ent) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(ent.getId()) && ent.getId() > 0) {
                em.merge(ent); //update
            } else {
                em.persist(ent); //insert
            }
            em.flush(); //Confirmar el insert o update

            em.getTransaction().commit();

            return ent;

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
    
    
    public TipoReembolso getTipoReembolsoPorId(Long id) throws Exception {
        try {
            getEntityManager();
            
            Query query = em.createQuery("FROM TipoReembolso t WHERE t.id = :id ");
            query.setParameter("id", id);

            List<TipoReembolso> lista = query.getResultList();
            
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
}
