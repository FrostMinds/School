package ru.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.model.Avatar;
import ru.hogwarts.model.Student;
import ru.hogwarts.repositories.AvatarRepository;
import ru.hogwarts.repositories.StudentRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarsDirectory;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.getById(studentId);

        logger.info("Student avatar upload method has been started");

        Path filePath = Path.of(avatarsDirectory, studentId + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.getFilePath();
        avatar.getFileSize();
        avatar.setMediaType(avatarFile.getContentType());

        avatarRepository.save(avatar);

        logger.debug("Student's {} avatar has been uploaded", student);
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("Launched a method to search for a student avatar by id {}", studentId);
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private String getExtensions(String fileName) {
        logger.info("Launched avatar file extension method");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public ResponseEntity<Collection<Avatar>> getAll(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        Collection<Avatar> avatarsList = avatarRepository.findAll(pageRequest).getContent();
        if (avatarsList.isEmpty()) {
            logger.error("There is no avatars at all");
            return ResponseEntity.notFound().build();
        }
        logger.debug("List of all avatars (page number= {}, page size={}): {}", avatarsList, pageNumber, pageSize);
        return ResponseEntity.ok(avatarsList);
    }
}