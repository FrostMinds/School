package ru.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.model.Faculty;
import ru.hogwarts.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private FacultyRepository facultyRepository;

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Faculty creation method launched");
        logger.debug("{} faculty created", faculty);
        return facultyRepository.save(faculty);

    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Faculty update method launched");
        logger.debug("{} faculty was updated", faculty);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.info("Faculty remove method launched");
        logger.debug("Faculty by id={} has been deleted", id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Method of obtaining all faculties launched");
        return facultyRepository.findAll();
    }

    public List<Faculty> getFacultyByColor(String color) {
        logger.info("Faculty color search method has been launched");
        List<Faculty> facultyList = facultyRepository.findByColor(color);
        if (facultyList.isEmpty()) {
            logger.error("Faculty with color {} is missing", color);
            return null;
        }
        logger.debug("Founded faculties with color={}: {}", color, facultyList);
        return facultyList;
    }

    public List<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String facultyFilter) {
        logger.info("Method to search all faculties with name or color contain {} case insensitive launched", facultyFilter);
        List<Faculty> facultyList = facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(facultyFilter, facultyFilter);
        if (facultyList.isEmpty()) {
            logger.error("Faculty with name or inclusion color {} case insensitive is missing", facultyFilter);
            return facultyList;
        }
        logger.debug("List of faculties with name or color containing {} wit ignoring case: {}", facultyFilter, facultyList);
        return facultyList;
    }

    public String getFacultyNameWithMaxLength() {
        Collection<Faculty> facultyList = facultyRepository.findAll();
        Optional<String> maxFacultyName = facultyList.stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length));
        if (maxFacultyName.isEmpty()) {
            return null;
        } else {
            return maxFacultyName.get();
        }
    }

}