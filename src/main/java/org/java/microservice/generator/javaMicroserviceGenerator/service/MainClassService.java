package org.java.microservice.generator.javaMicroserviceGenerator.service;

import org.java.microservice.generator.javaMicroserviceGenerator.model.ProjectDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Scanner;

@Service
public class MainClassService {

    @Value("${app.working-directory}")
    private String workingDirectory;

    public void createMainClass(ProjectDetails projectDetails)throws IOException {
        String className = projectDetails.getProjectName()
                .substring(0,1)
                .toUpperCase()+projectDetails.getProjectName()
                .substring(1)+"Application";
        Map<String, String> placeholders = Map.of(
                "packageName", projectDetails.getGroupId(),
                "packageClass",projectDetails.getProjectName(),
                "className", className);

        ClassPathResource resource = new ClassPathResource("templates/MainClassTemplate.java");
        String content;
        try (InputStream inputStream = resource.getInputStream(); Scanner scanner = new Scanner(inputStream)) {
            content = scanner.useDelimiter("\\A").next(); // Read entire file as a string
        }

        // Replace placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        String packagePath = projectDetails.getGroupId().replace(".", "/")
                +"/"
                +projectDetails.getProjectName();

        Path targetDir = Path.of(workingDirectory,
                projectDetails.getProjectName(),
                "src/main/java",
                packagePath);

        Files.createDirectories(targetDir); // Ensure directory exists

        // Define target file path
        Path targetPath = targetDir.resolve(className + ".java");

        // Write processed content to the new main class
        Files.writeString(targetPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Main Class created at: " + targetPath);
    }
}
