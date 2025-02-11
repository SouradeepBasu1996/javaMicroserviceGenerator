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
public class GenerateEntityService {

    @Value("${app.working-directory}")
    private String workingDirectory;

    private DataTypeService dataTypeService;

    public GenerateEntityService(DataTypeService dataTypeService){
        this.dataTypeService=dataTypeService;
    }

    public void createEntityClass(ProjectDetails projectDetails)throws IOException {
        StringBuilder fieldsBuilder = new StringBuilder();
        projectDetails.getEntityClass().getFields().forEach(entityModel -> {
            fieldsBuilder.append("    @Column(name =\"")
                    .append(formatSnakeCase(entityModel.getFieldName()))
                    .append("\")\n")
                    .append("    ")
                    .append("private ")
                    .append(dataTypeService.resolveDataType(entityModel.getDataType()))
                    .append(" ")
                    .append(entityModel.getFieldName())
                    .append(";\n");
        });

        Map<String, String> placeholders = Map.of(
                "packageName",projectDetails.getGroupId(),
                "packageClass",projectDetails.getArtifactId(),
                "entityName",projectDetails.getEntityClass().getEntityName(),
                "tableName",formatSnakeCase(projectDetails.getEntityClass().getEntityName()),
                "fields",fieldsBuilder.toString()
        );

        ClassPathResource resource = new ClassPathResource("templates/EntityTemplate.java");
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
                "entity");

        Files.createDirectories(targetDir); // Ensure directory exists

        // Define target file path
        Path targetPath = targetDir.resolve(projectDetails.getEntityClass().getEntityName() + ".java");

        // Write processed content to the new main class
        Files.writeString(targetPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Entity Class created at: " + targetPath);
    }

    private String formatSnakeCase(String fieldName){

        String str = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);

        StringBuilder tableName = new StringBuilder();

        for(int index = 0; index<str.length();index++){
            char hump =str.charAt(index);

            if(Character.isUpperCase(hump)){
                if(index>0)
                    tableName.append('_');
                tableName.append(Character.toLowerCase(hump));
            }
            else{
                tableName.append(hump);
            }
        }

        return tableName.toString();
    }
}
