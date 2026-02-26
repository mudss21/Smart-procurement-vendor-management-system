package com.procurement.procurement.service.report;

import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.repository.vendor.VendorRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class ReportService {

    private final VendorRepository vendorRepository;

    public ReportService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public byte[] generateVendorReport(String format) {

        try {

            // 🔥 Fetch real vendors from DB
            List<Vendor> vendors = vendorRepository.findAll();

            JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(vendors);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", "Vendor Report");

            InputStream reportStream =
                    new ClassPathResource("jasper/vendor_report.jrxml")
                            .getInputStream();

            JasperDesign jasperDesign =
                    JRXmlLoader.load(reportStream);

            JasperReport jasperReport =
                    JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            if ("excel".equalsIgnoreCase(format)) {

                JRXlsxExporter exporter = new JRXlsxExporter();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(
                        new SimpleOutputStreamExporterOutput(outputStream)
                );

                exporter.exportReport();

                return outputStream.toByteArray();
            }

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Report generation failed", e);
        }
    }
}