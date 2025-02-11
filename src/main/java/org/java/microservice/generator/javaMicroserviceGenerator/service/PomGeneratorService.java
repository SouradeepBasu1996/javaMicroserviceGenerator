package org.java.microservice.generator.javaMicroserviceGenerator.service;

import org.java.microservice.generator.javaMicroserviceGenerator.model.ProjectDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Scanner;

@Service
public class PomGeneratorService {
    private ResourceLoader resourceLoader;

    @Value("${app.working-directory}")
    private String workingDirectory;

    @Autowired
    public PomGeneratorService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void generatePom(ProjectDetails projectDetails) throws IOException {
        // Define placeholders
        Map<String, String> placeholders = Map.of(
                "groupId", projectDetails.getGroupId(),
                "artifactId", projectDetails.getArtifactId(),
                "description", projectDetails.getDescription()
        );

        // Read the template file from classpath
        ClassPathResource resource = new ClassPathResource("templates/pom-template.xml");
        String content;
        try (InputStream inputStream = resource.getInputStream(); Scanner scanner = new Scanner(inputStream)) {
            content = scanner.useDelimiter("\\A").next(); // Read entire file as a string
        }

        // Replace placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Define target file path
        Path targetPath = Path.of(workingDirectory, projectDetails.getProjectName(), "pom.xml");

        // Ensure parent directory exists
        Files.createDirectories(targetPath.getParent());

        // Write processed content to the new POM file
        Files.writeString(targetPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("POM file created at: " + targetPath);
    }
}
