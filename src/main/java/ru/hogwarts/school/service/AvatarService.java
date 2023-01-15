package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        logger.info("Was invoked method for get all avatars");
        return avatarRepository.findAll(pageRequest).getContent();
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.getReferenceById(studentId);

        Path filePath = Path.of(avatarsDir, student + "." + getExtension(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        logger.info("Was invoked method for upload avatar");

        avatarRepository.save(avatar);

        logger.debug("Loaded avatar with id {} size {}", avatar.getId(), avatar.getFileSize());
    }
    //Позволяет загружать файлы не более 1024 КБ (1 МБ), еще ограничение в контроллере - не более 300 Кбайт

    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method to get the avatar of the student with id = " + studentId);
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private String getExtension(String fileName) {
        logger.info("A method was called to extract the file extension");
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.debug("File extension '{}'", fileExtension);
        return fileExtension;
    }
}
