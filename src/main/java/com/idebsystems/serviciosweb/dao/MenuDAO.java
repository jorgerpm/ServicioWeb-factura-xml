/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.Menu;
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
public class MenuDAO extends Persistencia {
    private static final Logger LOGGER = Logger.getLogger(MenuDAO.class.getName());
    
    public List<Menu> listarMenus() throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Menu m order by m.titulo");

            List<Menu> listaMenu = query.getResultList();

            return listaMenu;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public Menu guardarMenu(Menu menu) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(menu.getId()) && menu.getId() > 0) {
                em.merge(menu); //update
            } else {
                em.persist(menu); //insert
            }
            em.flush(); //Confirmar el insert o update

            em.getTransaction().commit();

            return menu;

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
    
    public List<Menu> listarMenusPorRol(long idRol) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("SELECT m FROM Rol r, MenuRol mr, Menu m WHERE r.id=mr.idRol AND mr.idMenu=m.id AND r.id = :id order by m.titulo");
            query.setParameter("id", idRol);

            List<Menu> menuLista = query.getResultList();

            return menuLista;

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
