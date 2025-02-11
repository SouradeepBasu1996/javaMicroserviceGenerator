package org.java.microservice.generator.javaMicroserviceGenerator.service;

import org.java.microservice.generator.javaMicroserviceGenerator.model.ProjectDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Scanner;

@Service
public class GenerateServiceClass {

    @Value("${app.working-directory}")
    private String workingDirectory;

    public void createServiceClass(ProjectDetails projectDetails)throws IOException {

        String repositoryName = projectDetails.getEntityClass().getEntityName();
        StringBuilder repoBuilder = new StringBuilder();
        StringBuilder importBuilder = new StringBuilder();
        StringBuilder crudBuilder = new StringBuilder();
        StringBuilder modelImportBuilder = new StringBuilder();


        repoBuilder.append("\tprivate ")
                .append(repositoryName)
                .append("Repository")
                .append(" ")
                .append(createRepositoryObj(repositoryName))
                .append("Repository")
                .append(";\n");

        modelImportBuilder.append("import")
                .append(" ")
                .append(projectDetails.getGroupId())
                .append(".")
                .append(projectDetails.getProjectName())
                .append(".")
                .append("entity")
                .append(".")
                .append(repositoryName)
                .append(";\n");

        importBuilder.append("import ")
                .append(projectDetails.getGroupId())
                .append(".")
                .append(projectDetails.getProjectName())
                .append(".")
                .append("repository.")
                .append(repositoryName)
                .append("Repository")
                .append(";\n");
        crudBuilder.append(createCRUDMethods(repositoryName));

        Map<String, String> placeholders = Map.of(
                "packageName",projectDetails.getGroupId(),
                "packageClass",projectDetails.getArtifactId(),
                "serviceClassName",projectDetails.getServiceClassName(),
                "imports",importBuilder.toString(),
                "repositories",repoBuilder.toString(),
                "crudMethods",crudBuilder.toString(),
                "entity_imports",modelImportBuilder.toString()
        );
        ClassPathResource resource = new ClassPathResource("templates/ServiceClassTemplate.java");
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
                packagePath,
                "service");

        Files.createDirectories(targetDir); // Ensure directory exists

        // Define target file path
        Path targetPath = targetDir.resolve(projectDetails.getServiceClassName() + ".java");

        // Write processed content to the new service class
        Files.writeString(targetPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Service Class created at: " + targetPath);
    }

    private String createRepositoryObj(String repository){
        return repository.substring(0,1)
                .toLowerCase()+repository.substring(1);
    }

    private String createCRUDMethods(String entityName) {
        String entityObj = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);

        return """
                   // Create
                   public %1$s save%1$s(%1$s %2$s) {
                       return %3$s.save(%2$s);
                   }
                
                   // Read All
                   public List<%1$s> findAll%1$ss() {
                       return %3$s.findAll();
                   }
                
                   // Read by ID
                   public Optional<%1$s> find%1$sById(Long id) {
                       return %3$s.findById(id);
                   }
                
                   // Update
                   public %1$s update%1$s(%1$s %2$s) {
                       if (%3$s.existsById(%2$s.getId())) {
                           return %3$s.save(%2$s);
                       }
                       throw new IllegalArgumentException("Entity not found");
                   }
                
                   // Delete
                   public void delete%1$sById(Long id) {
                       %3$s.deleteById(id);
                   }
                """.formatted(entityName, entityObj, entityObj + "Repository");
    }
}
