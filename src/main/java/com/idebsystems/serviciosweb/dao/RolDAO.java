/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.Rol;
import com.idebsystems.serviciosweb.entities.RolEmpresa;
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
public class RolDAO extends Persistencia {
    private static final Logger LOGGER = Logger.getLogger(RolDAO.class.getName());
    
    public List<Rol> listarRoles() throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Rol r order by r.nombre");

            List<Rol> listaRol = query.getResultList();

            return listaRol;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public Rol guardarRol(Rol rol, List<RolEmpresa> listaEmpresas) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(rol.getId()) && rol.getId() > 0) {
                em.merge(rol); //update
            } else {
                em.persist(rol); //insert
            }
            
            //primero se debe eliminar de la tabla rolempresa lo que esta asociado al rol
            Query query = em.createQuery("delete from RolEmpresa re where re.idRol = :idRol");
            query.setParameter("idRol", rol.getId());
            query.executeUpdate();
            //guardar segun las empresas
            listaEmpresas.stream().map(re -> {
                re.setIdRol(rol.getId());
                return re;
            }).forEachOrdered(re -> {
                em.persist(re);
            });
            
            em.flush(); //Confirmar el insert o update

            em.getTransaction().commit();

            return rol;

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
    
    
    public Rol buscarRolPorId(Long idRol) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Rol r WHERE r.id = :idRol");
            query.setParameter("idRol", idRol);

            Rol rol = (Rol)query.getSingleResult();

            return rol;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    public List<RolEmpresa> buscarRolEmpresasPorRol(long idRol) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM RolEmpresa r WHERE r.idRol = :idRol order by r.idEmpresa");
            query.setParameter("idRol", idRol);

            List<RolEmpresa> lista = query.getResultList();

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
