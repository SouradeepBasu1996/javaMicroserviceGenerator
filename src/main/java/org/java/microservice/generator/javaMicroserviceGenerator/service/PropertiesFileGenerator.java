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
public class PropertiesFileGenerator {

    private ResourceLoader resourceLoader;

    @Value("${app.working-directory}")
    private String workingDirectory;

    @Autowired
    public PropertiesFileGenerator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void generatePropertiesFile(ProjectDetails projectDetails)throws IOException {

        Map<String, String> placeholders = Map.of(
                "microservice_name", projectDetails.getProjectName(),
                "database_name", projectDetails.getDatabaseSpecs().getDatabaseName(),
                "user_name", projectDetails.getDatabaseSpecs().getUserName(),
                "password", projectDetails.getDatabaseSpecs().getPassword());

        ClassPathResource resource = new ClassPathResource("templates/temp_application.properties");
        String content;
        try (InputStream inputStream = resource.getInputStream(); Scanner scanner = new Scanner(inputStream)) {
            content = scanner.useDelimiter("\\A").next(); // Read entire file as a string
        }

        // Replace placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Define target file path
        Path targetPath = Path.of(workingDirectory, projectDetails.getProjectName(), "application.properties");

        // Ensure parent directory exists
        Files.createDirectories(targetPath.getParent());

        // Write processed content to the new properties file
        Files.writeString(targetPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Properties file created at: " + targetPath);
    }
}
