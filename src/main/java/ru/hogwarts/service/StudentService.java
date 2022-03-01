package ru.hogwarts.service;

import ru.hogwarts.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public ResponseEntity<List<Student>> getStudentsByAge(int age) {
        List<Student> studentList = studentRepository.findByAge(age);
        if (studentList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentList);
    }
}
