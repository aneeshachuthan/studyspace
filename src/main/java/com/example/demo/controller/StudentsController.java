package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.repository.query.Param;
import com.example.demo.model.Students;
import com.example.demo.repository.StudentsRepository;

@Controller
public class StudentsController {

    @Autowired
    private StudentsRepository studentsRepository;

    @GetMapping("/students/create")
    public String createAction(Model model) {
        model.addAttribute("message", "Enter The Student Details");
        return "createStudent"; // Change this to your actual view name
    }

    @PostMapping("/students/create")
    public String createActionProcess(Students studentData, Model model) {
        // Check if the seat number is already occupied
        Optional<Students> existingStudent = studentsRepository.findBySeatNumber(studentData.getSeatNumber());
        if (existingStudent.isPresent()) {
            model.addAttribute("message", "Error: Seat number " + studentData.getSeatNumber() + " is already occupied.");
            return "createStudent"; // Return to the same view with an error message
        }

        // Save the new student
        studentsRepository.save(studentData);
        model.addAttribute("message", "The student " + studentData.getName() + " has been created successfully.");
        return "createStudent"; // Return to the same view with a success message
    }

    @GetMapping("/students/all")
    public String getAllStudents(Model model, @Param("keyword") String keyword) {
        List<Students> students;
        if (keyword != null && !keyword.isEmpty()) {
            students = studentsRepository.findAllByKeyword(keyword);
        } else {
            students = studentsRepository.findAll();
        }
        model.addAttribute("students", students);

        // Check if today is the 24th day of the month for testing
        boolean isSpecialDay = LocalDate.now().getDayOfMonth() == 24; // Change to 24 for testing
        model.addAttribute("alert", isSpecialDay); // Set alert flag

        return "listStudents"; // Change this to your actual view name
    }

    @GetMapping("/students/update/{seatNumber}")
    public String updateStudent(@PathVariable String seatNumber, Model model) {
        Optional<Students> optionalStudentDetails = studentsRepository.findBySeatNumber(seatNumber);
        if (optionalStudentDetails.isPresent()) {
            model.addAttribute("studentDetails", optionalStudentDetails.get());
            return "updateStudent"; // Change this to your actual view name
        }
        return "redirect:/students/all"; // Handle not found case
    }

    @PostMapping("/students/update/{seatNumber}")
    public String updateStudent(@PathVariable String seatNumber, Students studentData) {
        Optional<Students> optionalStudentDetails = studentsRepository.findBySeatNumber(seatNumber);
        if (optionalStudentDetails.isPresent()) {
            Students studentDetails = optionalStudentDetails.get();
            studentDetails.setName(studentData.getName());
            studentDetails.setPhoneNumber(studentData.getPhoneNumber());
            studentDetails.setAddress(studentData.getAddress());
            studentDetails.setJoiningDate(studentData.getJoiningDate());
            studentDetails.setFee(studentData.getFee());
            studentDetails.setStatus(studentData.getStatus());
            studentsRepository.save(studentDetails);
        }
        return "redirect:/students/all";
    }

    @GetMapping("/students/delete/{seatNumber}")
    public String deleteStudent(@PathVariable String seatNumber, Model model) {
        Optional<Students> optionalStudentDetails = studentsRepository.findBySeatNumber(seatNumber);
        if (optionalStudentDetails.isPresent()) {
            model.addAttribute("studentDetails", optionalStudentDetails.get());
            return "deleteStudent"; // Change this to your actual view name
        }
        return "redirect:/students/all"; // Handle not found case
    }

    @PostMapping("/students/delete/{seatNumber}")
    public String deleteStudent(@PathVariable String seatNumber) {
        Optional<Students> optionalStudentDetails = studentsRepository.findBySeatNumber(seatNumber);
        optionalStudentDetails.ifPresent(studentsRepository::delete);
        return "redirect:/students/all";
    }

    @GetMapping("/students/update-status")
    public String updateStudentStatus(Model model) {
        // Check if today is the first day of the month
        if (LocalDate.now().getDayOfMonth() == 1) {
            // Update all student statuses to "Pending"
            List<Students> students = studentsRepository.findAll();
            students.forEach(student -> {
                student.setStatus("Unpaid");
                studentsRepository.save(student);
            });
            model.addAttribute("message", "All student statuses have been updated to Unpaid.");
        } else {
            model.addAttribute("message", "Today is not the first day of the month. No updates made.");
        }
        return "statusUpdate"; // Change this to your actual view name for displaying the message
    }
   

}
