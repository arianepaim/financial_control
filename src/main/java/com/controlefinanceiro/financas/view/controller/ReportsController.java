package com.controlefinanceiro.financas.view.controller;

import com.controlefinanceiro.financas.entities.Reports;
import com.controlefinanceiro.financas.services.ReportsService;
import com.controlefinanceiro.financas.shared.ExpensesDTO;
import com.controlefinanceiro.financas.shared.IncomeDTO;
import com.controlefinanceiro.financas.shared.UserDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @GetMapping(value = "/generate", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReportsPdf(@RequestParam(name = "month", required = false) Integer month) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDTO) authentication.getPrincipal()).getId();

        Reports reports;
        if (month != null) {
            reports = reportsService.generateReportsForMonth(userId, month);
        } else {
            reports = reportsService.generateReports(userId);
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Relatório", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Receitas:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            document.add(new Paragraph(" "));
            double totalIncome = 0.0;
            for (IncomeDTO incomeDTO : reports.getIncome()) {
                document.add(new Paragraph(String.format("Categoria: %s%nNome: %s%nValor: %.2f%n",
                        incomeDTO.getCategory(), incomeDTO.getName(), incomeDTO.getValue())));
                document.add(new Paragraph(" "));
                totalIncome += incomeDTO.getValue();
            }

            document.add(new Paragraph("Despesas:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            document.add(new Paragraph(" "));
            double totalExpenses = 0.0;
            for (ExpensesDTO expensesDTO : reports.getExpenses()) {
                document.add(new Paragraph(String.format("Categoria: %s%nNome: %s%nValor: %.2f%n",
                        expensesDTO.getCategory(), expensesDTO.getName(), expensesDTO.getValue())));
                document.add(new Paragraph(" "));
                totalExpenses += expensesDTO.getValue();
            }

            document.add(new Paragraph(String.format("Total de Receitas: %.2f", totalIncome), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(new Paragraph(String.format("Total de Despesas: %.2f", totalExpenses), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(new Paragraph(String.format("Balanço: %.2f", totalIncome - totalExpenses), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "relatorio.pdf");

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        } catch (DocumentException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
