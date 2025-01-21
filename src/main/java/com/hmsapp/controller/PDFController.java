package com.hmsapp.controller;

import com.hmsapp.service.PDFService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/pdf")
public class PDFController {

    @Autowired
    private PDFService pdfService;

    @PostMapping("/generate")
    public String generatePdf() throws DocumentException, IOException {
        //pdfService.generateBookingPdf("E:\\pdf_booking\\test.pdf");
        return "booking created successfully";
    }
}
