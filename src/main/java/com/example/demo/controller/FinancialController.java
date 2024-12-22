package com.example.demo.controller;

import com.example.demo.model.Financials;
import com.example.demo.repository.FinancialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/financials")
public class FinancialController {

    @Autowired
    private FinancialsRepository financialRecordRepository;

    @GetMapping
    public String showFinancialsPage(Model model) {
        List<Financials> records = financialRecordRepository.findAll();
        Double totalIncome = financialRecordRepository.sumIncome();
        Double totalExpenditure = financialRecordRepository.sumExpenditure();
        
        model.addAttribute("records", records);
        model.addAttribute("totalIncome", totalIncome != null ? totalIncome : 0);
        model.addAttribute("totalExpenditure", totalExpenditure != null ? totalExpenditure : 0);
        return "financials"; // Return to the financials view
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("financialRecord", new Financials());
        return "addFinancialRecord"; // Create this view
    }

    @PostMapping("/add")
    public String addFinancialRecord(@ModelAttribute Financials financialRecord) {
        financialRecord.setDate(LocalDate.now()); // Set current date
        financialRecordRepository.save(financialRecord);
        return "redirect:/admin/financials";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Financials record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid record ID: " + id));
        model.addAttribute("financialRecord", record);
        return "editFinancialRecord"; // Create this view
    }

    @PostMapping("/edit/{id}")
    public String updateFinancialRecord(@PathVariable Long id, @ModelAttribute Financials financialRecord) {
        financialRecord.setId(id); // Ensure ID is set for update
        financialRecordRepository.save(financialRecord);
        return "redirect:/admin/financials";
    }

    @GetMapping("/delete/{id}")
    public String deleteFinancialRecord(@PathVariable Long id) {
        financialRecordRepository.deleteById(id);
        return "redirect:/admin/financials";
    }
}
