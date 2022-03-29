package ru.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hogwarts.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Student creation method started");
        logger.debug("Student {} created",student);
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        logger.info("Student update method started");
        logger.debug("Student {} updated", student);
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Student remove method started");
        logger.debug("Student with id={} deleted", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Get all students method started");
        return studentRepository.findAll();
    }

    public ResponseEntity<List<Student>> getStudentsByAge(int age) {
        logger.info("The method of obtaining students by age launched");
        List<Student> studentList = studentRepository.findByAge(age);
        if (studentList.isEmpty()) {
            logger.error("There is no student with age={}", age);
            return ResponseEntity.notFound().build();
        }
        logger.debug("Founded students by age={}: {}", age, studentList);
        return ResponseEntity.ok(studentList);
    }

    public ResponseEntity<List<Student>> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Method to search student by age between {} and {} started", minAge, maxAge);
        List<Student> studentList = studentRepository.findByAgeBetween(minAge, maxAge);
        if (studentList.isEmpty()) {
            logger.error("There is no students with age between {} and {}", minAge, maxAge);
            return ResponseEntity.notFound().build();
        }
        logger.debug("List of students with age between {} and {} : {}", minAge, maxAge, studentList);
        return ResponseEntity.ok(studentList);
    }

    public ResponseEntity<Long> getAmountOfAllStudents() {
        logger.info("The method to get the sum of all students has been launched ");
        return ResponseEntity.ok(studentRepository.getAmountOfAllStudents());
    }

    public ResponseEntity<Double> getAverageAgeOfAllStudents() {
        logger.info("Method for obtaining the average age of students launched");
        return ResponseEntity.ok(studentRepository.getAverageAgeOfAllStudents());
    }

    public ResponseEntity<Collection<Student>> getLastFiveStudents() {
        logger.info("The method to get the last five students is launched");
        Long amount = studentRepository.getAmountOfAllStudents();
        Collection<Student> studentList = studentRepository.getLastFiveStudents(amount);
        if (studentList.isEmpty()) {
            logger.error("The list of students is empty");
            return ResponseEntity.notFound().build();
        }
        logger.debug("List of last five added students: {}", studentList);
        return ResponseEntity.ok(studentList);
    }
}
