package com.generador_informes.backend.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.generador_informes.backend.Names;
import com.generador_informes.backend.dto.FormDataDto;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class ReportService {

	private static final String OUTPUT_DIR = "reports/";
	private static final String JRXML_FILE = "src/main/resources/reports/formulario.jrxml";
	private static final String LOGO_PATH = "src/main/resources/img/logo.png";

	public String generateJasperReport(FormDataDto formData) throws Exception {
	    JasperReport jasperReport = JasperCompileManager.compileReport(JRXML_FILE);

	    Map<String, Object> params = new HashMap<>();
	    params.put(Names.NOMBRE_LC.get(), formData.getNombre());
	    params.put(Names.EMAIL_LC.get(), formData.getEmail());
	    params.put(Names.COMENTARIO_LC.get(), formData.getComentario());

	    JRDataSource dataSource = new JREmptyDataSource();
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

	    String filename = OUTPUT_DIR + formData.getNombre() + Names.INFORME_JASPER_SUFIJO.get();
	    File file = new File(filename);
	    file.getParentFile().mkdirs();

	    JasperExportManager.exportReportToPdfFile(jasperPrint, filename);
	    System.out.println(Names.INFORME_JASPER_GENERADO.get() + filename);

	    return filename;
	}

	public String generateITextReport(FormDataDto formData) throws Exception {
	    String filename = OUTPUT_DIR + formData.getNombre() + Names.INFORME_ITEXT_SUFIJO.get();
	    File file = new File(filename);
	    file.getParentFile().mkdirs();

	    try (PdfWriter writer = new PdfWriter(file);
	         PdfDocument pdfDoc = new PdfDocument(writer);
	         Document document = new Document(pdfDoc)) {

	        if (new File(LOGO_PATH).exists()) {
	            Image logo = new Image(ImageDataFactory.create(LOGO_PATH));
	            logo.scaleToFit(120, 120);
	            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
	            document.add(logo);
	        }

	        Paragraph title = new Paragraph(Names.FORMULARIO_TITULO.get())
	                .setBold()
	                .setFontSize(20)
	                .setTextAlignment(TextAlignment.CENTER);
	        document.add(title);
	        document.add(new Paragraph("\n"));

	        float[] columnWidths = {150, 350};
	        Table table = new Table(columnWidths);
	        table.setWidth(UnitValue.createPercentValue(100));

	        table.addCell(new Cell().add(new Paragraph(Names.NOMBRE_LABEL.get()).setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
	        table.addCell(new Cell().add(new Paragraph(formData.getNombre())));

	        table.addCell(new Cell().add(new Paragraph(Names.EMAIL_LABEL.get()).setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
	        table.addCell(new Cell().add(new Paragraph(formData.getEmail())));

	        table.addCell(new Cell().add(new Paragraph(Names.TIPO_INFORME_LABEL.get()).setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
	        table.addCell(new Cell().add(new Paragraph(formData.getTipoInforme())));

	        table.addCell(new Cell().add(new Paragraph(Names.COMENTARIO_LABEL.get()).setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
	        table.addCell(new Cell().add(new Paragraph(formData.getComentario())));

	        document.add(table);
	    }

	    System.out.println(Names.INFORME_ITEXT_GENERADO.get() + filename);
	    return filename;
	}
	
	public String generateExcelReport(FormDataDto formData) throws Exception {
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet(Names.INFORME.get());

	    org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
	    Font font = workbook.createFont();
	    font.setBold(true);
	    headerStyle.setFont(font);

	    Row header = sheet.createRow(0);
	    String[] headers = {Names.NOMBRE_LABEL.get(), Names.EMAIL_LABEL.get(), Names.TIPO_INFORME_LABEL.get(), Names.COMENTARIO_LABEL.get()};
	    for (int i = 0; i < headers.length; i++) {
	        org.apache.poi.ss.usermodel.Cell cell = header.createCell(i);
	        cell.setCellValue(headers[i]);
	        cell.setCellStyle(headerStyle);
	    }

	    Row row = sheet.createRow(1);
	    row.createCell(0).setCellValue(formData.getNombre());
	    row.createCell(1).setCellValue(formData.getEmail());
	    row.createCell(2).setCellValue(formData.getTipoInforme());
	    row.createCell(3).setCellValue(formData.getComentario());

	    for (int i = 0; i < headers.length; i++) {
	        sheet.autoSizeColumn(i);
	    }

	    String filename = OUTPUT_DIR + formData.getNombre() + Names.INFORME_EXCEL_SUFIJO.get();
	    File file = new File(filename);
	    file.getParentFile().mkdirs();
	    try (FileOutputStream fos = new FileOutputStream(file)) {
	        workbook.write(fos);
	    }
	    workbook.close();

	    System.out.println(Names.INFORME_EXCEL_GENERADO.get() + filename);
	    return filename;
	}

}
