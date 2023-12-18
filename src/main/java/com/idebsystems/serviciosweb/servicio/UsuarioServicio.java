/*
 * Proyecto API Rest (ServiciosWeb)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.servicio;

import com.idebsystems.serviciosweb.dao.DocumentoReembolsosDAO;
import com.idebsystems.serviciosweb.dao.LiquidacionCompraDAO;
import com.idebsystems.serviciosweb.dao.ParametroDAO;
import com.idebsystems.serviciosweb.dao.RolDAO;
import com.idebsystems.serviciosweb.dao.UsuarioDAO;
import com.idebsystems.serviciosweb.dto.FirmaDigitalDTO;
import com.idebsystems.serviciosweb.dto.UsuarioDTO;
import com.idebsystems.serviciosweb.entities.Parametro;
import com.idebsystems.serviciosweb.entities.Rol;
import com.idebsystems.serviciosweb.entities.Usuario;
import com.idebsystems.serviciosweb.mappers.UsuarioMapper;
import com.idebsystems.serviciosweb.util.MyMD5;
import static com.idebsystems.serviciosweb.util.MyMD5.getInstance;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author jorge
 */
public class UsuarioServicio {

    private static final Logger LOGGER = Logger.getLogger(UsuarioServicio.class.getName());

    private final UsuarioDAO dao = new UsuarioDAO();

    public UsuarioDTO loginSistema(UsuarioDTO userDto) throws Exception {
        try {
            Usuario user = dao.loginSistema(userDto.getUsuario(), userDto.getClave());
            
            if (Objects.nonNull(user)) {
                userDto = UsuarioMapper.INSTANCE.entityToDto(user);
                userDto.setRespuesta("OK");
                
                if(userDto.getIdEstado() != 1){//es porque no esta activo el usuario, el idestado no es 1=activo
                    userDto.setId(0);
                    userDto.setRespuesta("USUARIO INACTIVO");
                }
                else{//si esta activo, aqui comprobar le fecha de la firma electronica, y mostrar una alerta si esta caducada
                    try{
                        FirmaDigitalServicio srv = new FirmaDigitalServicio();
                        FirmaDigitalDTO fd = srv.getFirmaActivaPorIdUsuario(user.getId(), true);
                        
                        //solo se cimprueba si es una firma digital, y no una imagen
                        if(fd.getTipoFirma() == 0){
                            
                            //buscar en los parametros el tiempo para caducar
                            ParametroDAO paramDao = new ParametroDAO();
                            Parametro prd = paramDao.buscarParametroPorNombre("DIAS_CADUCIDAD");
                            
                            if(Objects.isNull(prd)){
                                return new UsuarioDTO("No existe el parametro DIAS_CADUCIDAD configurado en el sistema.");
                            }
                            
                            int diasCaduca = Integer.parseInt(prd.getValor());
                            
                            Date fechaCaduca = new Date(fd.getFechaCaducaLong());

                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + diasCaduca);
                            LOGGER.log(Level.INFO, "fecha actual: {0}", cal.getTime());
                            if(cal.getTime().after(fechaCaduca)){
                                
                                cal = Calendar.getInstance();
                                
                                Calendar calFC = Calendar.getInstance();
                                calFC.setTime(fechaCaduca);
                                
                                int dias = (calFC.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR));
                                
                                LOGGER.log(Level.INFO, "dias faltantes:: {0}", dias);
                                
                                userDto.setAlertaFD(1);
                                userDto.setTextoAlertaFD("Su firma digital caducar\u00e1 en " + dias + " d\u00edas.");
                                
                                if(dias <= 0){
                                    userDto.setTextoAlertaFD("Su firma digital ha caducado " + (-dias) + " d\u00edas atr\u00e1s.");
                                }
                            }
                            
                        }
                    }catch(Exception exc){
                        //si pasa error al obtener la firma, debe igual iniciar sesion
                    }
                        
                    //para mostrar la alerta si este usuario tiene liquidaciones de compra en estado pendiente
                    LiquidacionCompraDAO lcdao = new LiquidacionCompraDAO();
                    boolean tieneLC = lcdao.tieneLiquidacionesPendientesUsuario(userDto.getId());
                    if(tieneLC){
                        userDto.setAlertaLC(1);
                        userDto.setTextoAlertaLC("Tiene liquidaciones de compra pendientes por firmar.");
                    }
                    else{
                        userDto.setAlertaLC(0);
                        userDto.setTextoAlertaLC(null);
                    }

                    //aca valida si tiene reembolsos POR_AUTORIZAR, se debe validar por aprobador
                    //y tambien validar por los contador y auxiliar
                    ParametroDAO pdao = new ParametroDAO();
                    Parametro ptiempo = pdao.buscarParametroPorNombre("TIEMPO_ALERTA_REEMBOLSO_PENDIENTE");
                    DocumentoReembolsosDAO reemdao = new DocumentoReembolsosDAO();
                    boolean porAutorizar = reemdao.tieneDocumentosPorAprobarUsuario(userDto.getId(), ptiempo.getValor());
                    LOGGER.log(Level.INFO, "porAutorizar1 {0}", porAutorizar);
                    if(porAutorizar){
                        userDto.setAlertaRPA(1);
                        userDto.setTextoAlertaRPA("Tiene reembolsos pendientes por aprobar.");
                    }
                    else{
                        //si el rol es contador o auxiliar
                        LOGGER.log(Level.INFO, "esrol {0}", userDto.getIdRol());
                        if(userDto.getIdRol() == 4 || userDto.getIdRol() == 5){
                            porAutorizar = reemdao.tieneDocumentosPorAprobarContador(ptiempo.getValor());
                            LOGGER.log(Level.INFO, "porAutorizar2 {0}", porAutorizar);
                            if(porAutorizar){
                                userDto.setAlertaRPA(1);
                                userDto.setTextoAlertaRPA("Tiene reembolsos pendientes por aprobar.");
                            }
                            else{
                                userDto.setAlertaRPA(0);
                                userDto.setTextoAlertaRPA(null);
                            }
                        }
                        else{
                            userDto.setAlertaRPA(0);
                            userDto.setTextoAlertaRPA(null);
                        }
                    }
                    //hasta aca
                    
                }
            } else {
                return new UsuarioDTO("OK");
            }

            return userDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public List<UsuarioDTO> listarUsuarios() throws Exception {
        try {
            List<UsuarioDTO> listaUsuarioDto = new ArrayList();
            
            List<Usuario> listaUsuario = dao.listarUsuarios();
            //buscar los roles
            RolDAO rolDao = new RolDAO();
            List<Rol> listaRoles = rolDao.listarRoles();
            
            listaUsuario.forEach(usuario->{
                UsuarioDTO usuarioDto = new UsuarioDTO();
                usuarioDto = UsuarioMapper.INSTANCE.entityToDto(usuario);
                //buscar para colocar el nombre del rol
                Rol rol = listaRoles.stream().filter(r -> r.getId() == usuario.getIdRol()).findAny().get();
                usuarioDto.setNombreRol(rol.getNombre());
                
                listaUsuarioDto.add(usuarioDto);
            });

            return listaUsuarioDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public UsuarioDTO guardarUsuario(UsuarioDTO usuarioDto) throws Exception {
        try{
            //si clave viene nula desde pantalla es porque no le cambiaron
            if(Objects.nonNull(usuarioDto.getId()) && usuarioDto.getId() > 0 && Objects.isNull(usuarioDto.getClave())){
                Usuario userTemp = dao.buscarUsuarioPorId(usuarioDto.getId());
                usuarioDto.setClave(userTemp.getClave());
            }
            
            Usuario usuario = UsuarioMapper.INSTANCE.dtoToEntity(usuarioDto);
            Usuario usuarioRespuesta = dao.guardarUsuario(usuario);
            usuarioDto = UsuarioMapper.INSTANCE.entityToDto(usuarioRespuesta);
            usuarioDto.setRespuesta("OK");
            return usuarioDto;
        } catch (Exception exc) {
            if(Objects.nonNull(exc.getMessage()) && exc.getMessage().contains("usuario_usuario_key")){
                usuarioDto.setId(0);
                usuarioDto.setRespuesta("YA EXISTE UN USUARIO REGISTRADO CON EL USUARIO INGRESADO. INGRESE UN USUARIO DIFERENTE.");
                return usuarioDto;
            }
            if(Objects.nonNull(exc.getMessage()) && exc.getMessage().contains("usuario_correo_key")){
                usuarioDto.setId(0);
                usuarioDto.setRespuesta("EL CORREO INGRESADO PERTENECE A OTRO USUARIO, INGRESE UN CORREO DIFERENTE.");
                return usuarioDto;
            }
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    public UsuarioDTO generarClavePorCorreo(String correo) throws Exception {
        try{
            Usuario usuario = dao.buscarUsuarioPorCorreo(correo);
            if(Objects.isNull(usuario))
                return null;//el usuario con ese correo no existe
            
            //se procede a generar la nueva clave
            final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            SecureRandom random = new SecureRandom();
            String nuevaClave = IntStream.range(0, 8)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
            
            MyMD5 md = getInstance();
            usuario.setClave(md.hashData(nuevaClave.getBytes()));
            
            usuario = dao.guardarUsuario(usuario);
            
            UsuarioDTO usuarioDto = UsuarioMapper.INSTANCE.entityToDto(usuario);
            usuarioDto.setClave(nuevaClave);
            
            return usuarioDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    
    public List<UsuarioDTO> listarUsuariosPorRol(long idRol) throws Exception {
        try {
            List<UsuarioDTO> listaUsuarioDto = new ArrayList();
            
            List<Usuario> listaUsuario = dao.listarUsuariosPorRol(idRol);
            //buscar los roles
            RolDAO rolDao = new RolDAO();
            List<Rol> listaRoles = rolDao.listarRoles();
            
            listaUsuario.forEach(usuario->{
                UsuarioDTO usuarioDto = new UsuarioDTO();
                usuarioDto = UsuarioMapper.INSTANCE.entityToDto(usuario);
                //buscar para colocar el nombre del rol
                Rol rol = listaRoles.stream().filter(r -> r.getId() == usuario.getIdRol()).findAny().get();
                usuarioDto.setNombreRol(rol.getNombre());
                
                listaUsuarioDto.add(usuarioDto);
            });

            return listaUsuarioDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    public UsuarioDTO cambiarClave(UsuarioDTO usuarioDto) throws Exception {
        try{
            Usuario usuario = dao.buscarUsuarioPorId(usuarioDto.getId());
            
            if(usuarioDto.getClave().equals(usuario.getClave())){
                usuarioDto.setId(0);
                usuarioDto.setRespuesta("La nueva clave es igual a la clave anterior. Ingrese una nueva clave.");
                return usuarioDto;
            }
            
            usuario.setClave(usuarioDto.getClave());
            
            usuario = dao.guardarUsuario(usuario);
            
            usuarioDto = UsuarioMapper.INSTANCE.entityToDto(usuario);
            usuarioDto.setRespuesta("OK");
            return usuarioDto;
            
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
    
    
    public List<UsuarioDTO> listarUsuariosActivos() throws Exception {
        try {
            List<UsuarioDTO> listaUsuarioDto = new ArrayList();
            
            List<Usuario> listaUsuario = dao.listarUsuariosActivos();
            //buscar los roles
            RolDAO rolDao = new RolDAO();
            List<Rol> listaRoles = rolDao.listarRoles();
            
            listaUsuario.forEach(usuario->{
                UsuarioDTO usuarioDto = new UsuarioDTO();
                usuarioDto = UsuarioMapper.INSTANCE.entityToDto(usuario);
                //buscar para colocar el nombre del rol
                Rol rol = listaRoles.stream().filter(r -> r.getId() == usuario.getIdRol()).findAny().get();
                usuarioDto.setNombreRol(rol.getNombre());
                
                listaUsuarioDto.add(usuarioDto);
            });

            return listaUsuarioDto;
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, null, exc);
            throw new Exception(exc);
        }
    }
}
