/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.FacturaFisica;
import com.idebsystems.serviciosweb.util.Persistencia;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class FacturaFisicaDAO extends Persistencia {
    
    private static final Logger LOGGER = Logger.getLogger(FacturaFisicaDAO.class.getName());
    
    
    public FacturaFisica guardarFacturaFisica(FacturaFisica facturaFisica) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(facturaFisica.getId()) && facturaFisica.getId() > 0) {
                em.merge(facturaFisica); //update
            } else {
                em.persist(facturaFisica); //insert
            }
            em.flush(); //Confirmar el insert o update

            em.getTransaction().commit();

            return facturaFisica;

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
