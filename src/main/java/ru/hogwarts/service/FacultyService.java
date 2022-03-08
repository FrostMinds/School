package ru.hogwarts.service;

import ru.hogwarts.model.Faculty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.repositories.FacultyRepository;

import java.util.Collection;
import java.util.List;

@Service
public class FacultyService {

    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public ResponseEntity<List<Faculty>> getFacultyByColor(String color) {
        List<Faculty> facultyList = facultyRepository.findByColor(color);
        if (facultyList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyList);
    }

        public ResponseEntity<List<Faculty>> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String facultyFilter) {
            List<Faculty> facultyList = facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(facultyFilter, facultyFilter);
            if (facultyList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(facultyList);
        }

    }
