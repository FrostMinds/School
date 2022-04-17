package ru.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.model.Student;
import ru.hogwarts.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Student creation method started");
        logger.debug("Student {} created", student);
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

    public List<Student> getStudentsByAge(int age) {
        logger.info("The method of obtaining students by age launched");
        List<Student> studentList = studentRepository.findByAge(age);
        if (studentList.isEmpty()) {
            logger.error("There is no student with age={}", age);
            return null;
        }
        logger.debug("Founded students by age={}: {}", age, studentList);
        return studentList;
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Method to search student by age between {} and {} started", minAge, maxAge);
        List<Student> studentList = studentRepository.findByAgeBetween(minAge, maxAge);
        if (studentList.isEmpty()) {
            logger.error("There is no students with age between {} and {}", minAge, maxAge);
            return null;
        }
        logger.debug("List of students with age between {} and {} : {}", minAge, maxAge, studentList);
        return studentList;
    }

    public Long getAmountOfAllStudents() {
        logger.info("The method to get the sum of all students has been launched ");
        return studentRepository.getAmountOfAllStudents();
    }

    public Double getAverageAgeOfAllStudents() {
        logger.info("Method for obtaining the average age of students launched");
        return studentRepository.getAverageAgeOfAllStudents();
    }

    public Collection<Student> getLastFiveStudents() {
        logger.info("The method to get the last five students is launched");
        Long amount = studentRepository.getAmountOfAllStudents();
        Collection<Student> studentList = studentRepository.getLastFiveStudents(amount);
        if (studentList.isEmpty()) {
            logger.error("The list of students is empty");
            return null;
        }
        logger.debug("List of last five added students: {}", studentList);
        return studentList;
    }

    public Collection<String> getStudentNamesByFirstLetter(String chr) {
        return studentRepository.findAll().
                stream().
                map(Student::getName).
                filter(s -> s.substring(0, 1).
                        toUpperCase().
                        equals(chr.toUpperCase())).
                map(s -> s.toUpperCase()).
                sorted().
                collect(Collectors.toList());
    }

    public Double getAverageAgeOfAllStudentsUsingStream() {
        return studentRepository.findAll().
                stream().
                mapToInt(Student::getAge).
                average().
                stream().
                mapToObj(Double::new).
                findFirst().
                orElse(null);
    }

    private final Object flag = new Object();

    private int count = 0;

    public ResponseEntity<Collection<String>> getNameListByThread() {
        logger.info("Was invoked method to get name list of all students by threads");
        List<String> nameList = studentRepository.findAll().stream()
                .map(Student::getName)
                .collect(Collectors.toList());
        if (nameList.isEmpty()) {
            logger.error("There is no students at all");
            return ResponseEntity.notFound().build();
        }
        System.out.println(nameList.get(0));
        System.out.println(nameList.get(1));
        new Thread(() -> {
            System.out.println(nameList.get(2));
            System.out.println(nameList.get(3));
        }).start();
        new Thread(() -> {
            System.out.println(nameList.get(4));
            System.out.println(nameList.get(5));
        }).start();
        logger.debug("Name list of all students: {}", nameList);
        return ResponseEntity.ok(nameList);
    }

    public ResponseEntity<Collection<String>> getNameListBySyncThread() {
        logger.info("Was invoked method to get name list of all students by synchronized threads");
        List<String> nameList = studentRepository.findAll().stream()
                .map(Student::getName)
                .collect(Collectors.toList());
        if (nameList.isEmpty()) {
            logger.error("There is no students at all");
            return ResponseEntity.notFound().build();
        }

        printElementOfList(nameList);
        printElementOfList(nameList);
        new Thread(() -> {
            printElementOfList(nameList);
            printElementOfList(nameList);
        }).start();
        new Thread(() -> {
            printElementOfList(nameList);
            printElementOfList(nameList);
        }).start();
        logger.debug("Name list of all students: {}", nameList);
        return ResponseEntity.ok(nameList);
    }

    private void printElementOfList(List<String> nameList) {
        System.out.println(nameList.get(count));
        synchronized (flag) {
            count++;
        }
    }
}