/*
 * Proyecto API Rest (ServiciosWebGestionCompras)
 * IdebSystems Cia. Ltda. Derechos reservados. 2022
 * Prohibida la reproducción total o parcial de este producto
 */
package com.idebsystems.serviciosweb.dao;

import com.idebsystems.serviciosweb.entities.Parametro;
import com.idebsystems.serviciosweb.util.Persistencia;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author jorge
 */
public class ReporteDAO extends Persistencia {

    private static final Logger LOGGER = Logger.getLogger(ReporteDAO.class.getName());

    private Connection getConn() throws Exception {
        try {

            getEntityManager();
            em.getTransaction().begin(); //se debe hacer el begin para que se pueda obtener la conexion, sino retorna nulo

            Connection con = em.unwrap(Connection.class);

            em.getTransaction().commit();

            return con;

        } catch (Exception exc) {
            exc.printStackTrace();
            throw new Exception(exc);
        }
    }

    public JasperPrint compilacionReportePdf(String report, Map<String, Object> parameters, JRXmlDataSource jrds) throws Exception {
        JasperPrint jasperPrint = null;
        Connection con = null;
        try {
            con = getConn();

            String urlReportes = getPathReportes();

            parameters.put("SUBREPORT_DIR", urlReportes);

            File sourceFile = new File(urlReportes + report + ".jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(sourceFile);

            if(Objects.nonNull(jrds)){
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrds);
            }
            else{
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            try {
                closeEntityManager();
                if (con != null) {
                    LOGGER.log(Level.INFO, "REPORTE EJECUTADO");
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jasperPrint;
    }
    
    public JasperPrint compilacionReporteCsv(String report, Timestamp fechaIni, Timestamp fechaFin) throws Exception {
        JasperPrint jasperPrint = null;
        Connection con = null;
        try {
            con = getConn();

            String urlReportes = getPathReportes();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_fechaIni", fechaIni);
            parameters.put("p_fechaFin", fechaFin);
            parameters.put("SUBREPORT_DIR", urlReportes);

            File sourceFile = new File(urlReportes + report + ".jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(sourceFile);

            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            try {
                closeEntityManager();
                if (con != null) {
                    LOGGER.log(Level.INFO, "REPORTE EJECUTADO");
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jasperPrint;
    }

    private String getPathReportes() throws Exception {
        ParametroDAO paramDao = new ParametroDAO();
        List<Parametro> listaparametros = paramDao.listarParametros();
        
        Parametro param = listaparametros.stream().filter(p -> p.getNombre().equalsIgnoreCase("PATH_REPORTES")).findAny().get();
        
        return param.getValor();
    }
}
