package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.dto.simulation.SimulationResponseDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    public byte[] generarPDF(SimulationResponseDto dto) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(doc, out);
            doc.open();

            /* === ENCABEZADO CORPORATIVO === */
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);
            header.setWidths(new float[] { 1, 2 });

            // Logo DebtGo
            try {
                String logoPath = "src/main/resources/static/img/debtgo-logo.png";
                File file = new File(logoPath);
                if (file.exists()) {
                    Image logo = Image.getInstance(new FileInputStream(file).readAllBytes());
                    logo.scaleAbsolute(90, 35);
                    PdfPCell logoCell = new PdfPCell(logo, false);
                    logoCell.setBorder(Rectangle.NO_BORDER);
                    header.addCell(logoCell);
                } else {
                    PdfPCell emptyCell = new PdfPCell(
                            new Phrase("DebtGo", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(40, 60, 130))));
                    emptyCell.setBorder(Rectangle.NO_BORDER);
                    header.addCell(emptyCell);
                }
            } catch (Exception ex) {
                PdfPCell fallback = new PdfPCell(
                        new Phrase("DebtGo", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(40, 60, 130))));
                fallback.setBorder(Rectangle.NO_BORDER);
                header.addCell(fallback);
            }

            // Información de usuario y fecha
            String nombreUsuario = dto.getUserName() != null ? dto.getUserName() : "Usuario DebtGo";
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph info = new Paragraph(
                    "Generado por: " + nombreUsuario + "\nFecha: " + fecha,
                    new Font(Font.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY));
            PdfPCell userInfo = new PdfPCell(info);
            userInfo.setBorder(Rectangle.NO_BORDER);
            userInfo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            header.addCell(userInfo);
            doc.add(header);

            doc.add(new Paragraph(" "));

            /* === TÍTULO PRINCIPAL === */
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(40, 60, 130));
            Paragraph title = new Paragraph("Reporte de Simulación - DebtGo", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Resumen general:", new Font(Font.HELVETICA, 13, Font.BOLD)));

            /* === RESUMEN === */
            PdfPTable resumen = new PdfPTable(3);
            resumen.setWidthPercentage(100);
            resumen.setSpacingBefore(10);

            resumen.addCell(celda("Cuota mensual:", true));
            resumen.addCell(celda("Interés total:", true));
            resumen.addCell(celda("Pago total:", true));

            resumen.addCell(celda("S/ " + dto.getMonthlyPayment(), false));
            resumen.addCell(celda("S/ " + dto.getTotalInterest(), false));
            resumen.addCell(celda("S/ " + dto.getTotalPayment(), false));

            doc.add(resumen);
            doc.add(new Paragraph(" "));

            /* === TABLA DE PLAN DE PAGOS === */
            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[] { 1.2f, 2.2f, 2f, 2f, 2f, 2f });
            tabla.setSpacingBefore(15);

            Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
            String[] headers = { "#", "Fecha", "Cuota", "Interés", "Capital", "Saldo" };
            for (String h : headers) {
                PdfPCell hcell = new PdfPCell(new Phrase(h, headerFont));
                hcell.setBackgroundColor(new Color(37, 99, 235));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setPadding(6);
                tabla.addCell(hcell);
            }

            dto.getSchedule().forEach(r -> {
                tabla.addCell(celda(String.valueOf(r.getN()), false));
                tabla.addCell(celda(r.getDate(), false));
                tabla.addCell(celda("S/ " + r.getPayment(), false));
                tabla.addCell(celda("S/ " + r.getInterest(), false));
                tabla.addCell(celda("S/ " + r.getPrincipal(), false));
                tabla.addCell(celda("S/ " + r.getBalance(), false));
            });

            doc.add(tabla);

            /* === PIE DE PÁGINA === */
            doc.add(new Paragraph(" "));
            Paragraph footer = new Paragraph(
                    "DebtGo © " + LocalDate.now().getYear() + " - Sistema de gestión financiera inteligente.",
                    new Font(Font.HELVETICA, 9, Font.ITALIC, Color.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            doc.add(footer);

            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    private PdfPCell celda(String texto, boolean bold) {
        Font font = new Font(Font.HELVETICA, bold ? 11 : 10, bold ? Font.BOLD : Font.NORMAL, Color.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}