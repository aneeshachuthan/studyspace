package com.example.demo.controller;

import com.example.demo.model.Bill;
import com.example.demo.repository.BillRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Controller
public class PdfController {

    @Autowired
    private BillRepository billRepository;

    @PostMapping("/generate-bill")
    public void generateBill(@ModelAttribute Bill bill, HttpServletResponse response) throws IOException {
        // Generate a unique bill number
        bill.setBillNumber(UUID.randomUUID().toString());

        // Save the bill to the database
        billRepository.save(bill);

        // Create a PDF and return it
        createPdf(bill, response);
    }

    private void createPdf(Bill bill, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=bill_" + bill.getBillNumber() + ".pdf");

        try (PdfWriter writer = new PdfWriter(response.getOutputStream());
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Study.Space Reading Room").setFontSize(20).setBold());
            document.add(new Paragraph("Bill").setFontSize(10).setBold());

            Table table = new Table(2);
            table.addCell("Bill Number:");
            table.addCell(bill.getBillNumber());
            table.addCell("Name:");
            table.addCell(bill.getName());
            table.addCell("Type of Service:");
            table.addCell(bill.getServiceType());
            table.addCell("Amount:");
            table.addCell(String.valueOf(bill.getAmount()));

            document.add(table);
            document.add(new Paragraph("Thank you").setFontSize(12));
        }
    }
}
