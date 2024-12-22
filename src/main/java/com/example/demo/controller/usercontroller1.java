package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.model.Students; 
import com.example.demo.repository.StudentsRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class usercontroller1 {

    @Autowired
    private StudentsRepository studentsRepository;

    @GetMapping("/user/add")
    public String showAddUserForm(Model model) {
        List<Integer> availableSeats = getAvailableSeats();
        List<Students> existingStudents = studentsRepository.findAll();
        
        // Create a list of occupied seats
        List<Integer> occupiedSeats = new ArrayList<>();
        for (Students s : existingStudents) {
            if (s.getSeatNumber() != null) { // Check for null before parsing
                try {
                    occupiedSeats.add(Integer.parseInt(s.getSeatNumber()));
                } catch (NumberFormatException e) {
                    // Handle invalid seat numbers if necessary (log or notify)
                }
            }
        }
        
        model.addAttribute("availableSeats", availableSeats);
        model.addAttribute("occupiedSeats", occupiedSeats);
        return "addUser"; // Ensure this matches your actual view name
    }

    @PostMapping("/user/add")
    public String addUser(Students student, Model model) {
        // Debugging line to confirm the method is reached
        System.out.println("Form submitted with student: " + student);

        String seatNumber = student.getSeatNumber();
        if (!isSeatNumberValid(seatNumber)) {
            model.addAttribute("errorMessage", "Seat number must be between 1 and 60 and unique.");
            return "addUser"; 
        }

        List<Students> existingStudents = studentsRepository.findAll();
        for (Students s : existingStudents) {
            if (s.getSeatNumber().equals(seatNumber)) {
                model.addAttribute("errorMessage", "Seat already occupied.");
                return "addUser"; 
            }
        }

        student.setFee(0.0);
        student.setStatus("Unpaid");
        studentsRepository.save(student);
        model.addAttribute("message", "Your information has been submitted successfully.");
        return "success.html"; 
    }

    private boolean isSeatNumberValid(String seatNumber) {
        try {
            int seatNum = Integer.parseInt(seatNumber);
            return seatNum >= 1 && seatNum <= 60;
        } catch (NumberFormatException e) {
            return false; 
        }
    }


    private List<Integer> getAvailableSeats() {
        List<Integer> allSeats = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            allSeats.add(i);
        }

        // Remove occupied seats
        List<Students> existingStudents = studentsRepository.findAll();
        for (Students s : existingStudents) {
            if (s.getSeatNumber() != null) { // Check for null
                try {
                    int occupiedSeat = Integer.parseInt(s.getSeatNumber());
                    allSeats.remove((Integer) occupiedSeat); // Remove the occupied seat
                } catch (NumberFormatException e) {
                    // Handle invalid seat numbers if necessary
                }
            }
        }

        return allSeats;
    }
}
