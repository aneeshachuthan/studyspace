package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.demo.model.Students;
import com.example.demo.repository.StudentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class StudentStatusService {
    private static final Logger logger = LoggerFactory.getLogger(StudentStatusService.class);

    @Autowired
    private StudentsRepository studentsRepository;

    @Scheduled(cron = "0 0 1 1 * ?") // At 1 AM on the 1st of every month
    public void updateStudentStatusToPending() {
        logger.info("Scheduled task executed. Updating student statuses to Pending.");
        List<Students> students = studentsRepository.findAll();
        for (Students student : students) {
            student.setStatus("Pending");
            studentsRepository.save(student);
            logger.info("Updated status for student: {}", student.getName());
        }
    }
}
