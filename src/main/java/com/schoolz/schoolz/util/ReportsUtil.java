package com.schoolz.schoolz.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@SuppressWarnings("deprecation")
@Component
public class ReportsUtil {
    private static final Logger logger = LogManager.getLogger(ReportsUtil.class);

    private static DataSource dataSource;

    @Autowired
    private DataSource dataSource_;

    @PostConstruct
    private void initStaticDataSource () {
        dataSource = this.dataSource_;
    }

    public static byte[] exportPDF(Map<String, Object> parameters, String reportName) throws JRException, IOException, SQLException {
        try {
            final JasperReport report = loadTemplate(reportName);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource.getConnection());
            byte[] result  = JasperExportManager.exportReportToPdf(jasperPrint);
            return result;

        }catch (Exception e) {
            logger.info(String.format("Error pdf export : %s", e.getMessage()));
            return null;
        }
    }

    static JasperReport loadTemplate(String reportName) throws JRException, FileNotFoundException, UnsupportedEncodingException {
        try {
            String path = "/reports/" + reportName + ".jrxml";
            logger.info(String.format("Invoice template path : %s", path));
            final InputStream reportInputStream = ReportsUtil.class.getResourceAsStream(path);
            final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);
            return JasperCompileManager.compileReport(jasperDesign);
        }catch (Exception e){
            logger.info(e.getMessage());
            return null;
        }
    }
}
