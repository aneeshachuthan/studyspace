package com.example.demo.controller;

import com.example.demo.model.Bill;
import com.example.demo.repository.BillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/billing")
public class BillingController {

    private static final Logger logger = LoggerFactory.getLogger(BillingController.class);
    private static int billCounter = 1; // Start from 1 to keep track of the bill number

    @Autowired
    private BillRepository billRepository;

    @GetMapping
    public String showBillingPage(Model model) {
        model.addAttribute("bill", new Bill());
        return "Billing"; // Return to the billing view
    }

    @PostMapping("/generate")
    public ModelAndView generateBill(@ModelAttribute Bill bill) {
        // Generate a unique bill number
        String billNumber = generateBillNumber();
        bill.setBillNumber(billNumber);

        // Save the bill to the database
        billRepository.save(bill);

        // Log the generated bill
        logger.info("Generated bill: {}", bill);

        // Create a PDF and return it
        return createPdf(bill);
    }

    private String generateBillNumber() {
        // Generate a formatted number as 4 digits
        String formattedNumber = String.format("%04d", billCounter);

        // Determine the alphabet character (e.g., A, B, C...) based on the billCounter
        char alphabet = (char) ('A' + (billCounter - 1) / 1000); // Change letter every 1000 bills

        // Increment the counter and reset if it exceeds 1000
        billCounter++;
        if (billCounter > 1000) {
            billCounter = 1; // Reset to 1 after reaching 1000
        }

        return formattedNumber + alphabet; // Combine number and letter
    }

    private ModelAndView createPdf(Bill bill) {
        ModelAndView modelAndView = new ModelAndView("pdfBill");
        modelAndView.addObject("bill", bill);
        return modelAndView;
    }

    @GetMapping("/list")
    public String listBills(Model model) {
        List<Bill> bills = billRepository.findAll(); // Fetch all bills from the database
        logger.info("Listing bills: {}", bills);
        model.addAttribute("bills", bills);
        return "billList"; // Return to the bill listing view
    }
}
