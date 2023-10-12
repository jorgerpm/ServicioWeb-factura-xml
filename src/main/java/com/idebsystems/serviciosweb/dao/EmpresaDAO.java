/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;


import com.idebsystems.serviciosweb.entities.Empresa;
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
public class EmpresaDAO extends Persistencia {
    
    private static final Logger LOGGER = Logger.getLogger(EmpresaDAO.class.getName());
    
    
    public List<Empresa> listarEmpresas() throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Empresa e order by e.razonSocial");

            List<Empresa> data = query.getResultList();

            return data;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public Empresa guardarEmpresa(Empresa empresa) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(empresa.getId()) && empresa.getId() > 0) {
                em.merge(empresa); //update
            } else {
                em.persist(empresa); //insert
            }
            em.flush(); //Confirmar el insert o update

            em.getTransaction().commit();

            return empresa;

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
