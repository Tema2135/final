package org.example.finalproject.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class AdminService {
    public void savePhoto(MultipartFile file)throws IOException {
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/img").resolve(filename).toAbsolutePath().normalize();
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }

}
