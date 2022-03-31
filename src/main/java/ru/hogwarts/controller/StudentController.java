package ru.hogwarts.controller;

import ru.hogwarts.model.Student;
import ru.hogwarts.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping()
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping()
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @GetMapping()
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("filter/{age}")
    public ResponseEntity<List<Student>> getStudents(@PathVariable int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("filter")
    public ResponseEntity<List<Student>> getStudents(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("count-all")
    public ResponseEntity<Long> getAmountOfAllStudents() {
        return studentService.getAmountOfAllStudents();
    }

    @GetMapping("average-age")
    public ResponseEntity<Double> getAverageAgeOfAllStudents() {
        return studentService.getAverageAgeOfAllStudents();
    }

    @GetMapping("last5")
    public ResponseEntity<Collection<Student>> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("names")
    public ResponseEntity<Collection<String>> getStudentsNamesByFirstLetter(@RequestParam String chr) {
        return (ResponseEntity<Collection<String>>) studentService.getStudentNamesByFirstLetter(chr);
    }

    @GetMapping("average-age-by-stream")
    public ResponseEntity<Double> getAverageAgeOfAllStudentsUsingStream() {
        return studentService.getAverageAgeOfAllStudentsUsingStream();
    }
}
