/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.Usuario;
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
public class UsuarioDAO extends Persistencia {

    private static final Logger LOGGER = Logger.getLogger(UsuarioDAO.class.getName());

    public Usuario loginSistema(String usuario, String clave) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Usuario u WHERE u.usuario = :usuario AND u.clave = :clave order by u.nombre");
            query.setParameter("usuario", usuario);
            query.setParameter("clave", clave);

            Usuario user = (Usuario) query.getSingleResult();

            return user;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }

    public List<Usuario> listarUsuarios() throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Usuario u");

            List<Usuario> listaUsuario = query.getResultList();

            return listaUsuario;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public Usuario guardarUsuario(Usuario usuario) throws Exception {
        try {

            getEntityManager();

            em.getTransaction().begin();

            if (Objects.nonNull(usuario.getId()) && usuario.getId() > 0) {
                em.merge(usuario);
            } else {
                em.persist(usuario);
            }
            em.flush();

            em.getTransaction().commit();

            return usuario;

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
    
    
    public Usuario buscarUsuarioPorCorreo(String correo) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Usuario u WHERE u.correo = :correo");
            query.setParameter("correo", correo);

            List<Usuario> listaUsuario = query.getResultList();
            
            if(listaUsuario.isEmpty())
                return null;

            return listaUsuario.get(0);

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    public Usuario buscarUsuarioPorId(Long idUsuario) throws Exception {
        try {
            getEntityManager();

            Usuario usuario = em.find(Usuario.class, idUsuario);

            return usuario;

       } catch (NoResultException exc) {
            return null;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        } finally {
            closeEntityManager();
        }
    }
    
    
    public List<Usuario> listarUsuariosPorRol(long idRol) throws Exception {
        try {
            getEntityManager();

            Query query = em.createQuery("FROM Usuario u where u.idEstado = 1 and u.idRol = :idRol ");
            query.setParameter("idRol", idRol);

            List<Usuario> listaUsuario = query.getResultList();

            return listaUsuario;

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
