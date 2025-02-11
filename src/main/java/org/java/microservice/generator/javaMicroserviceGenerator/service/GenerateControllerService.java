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
public class GenerateControllerService {

    @Value("${app.working-directory}")
    private String workingDirectory;

    public void createControllerClass(ProjectDetails projectDetails)throws IOException {
        StringBuilder dependencyBuilder = new StringBuilder();
        String serviceObj = projectDetails.getServiceClassName().substring(0, 1).toLowerCase()+projectDetails.getServiceClassName().substring(1);
        dependencyBuilder.append("\tprivate ")
                .append(projectDetails.getServiceClassName())
                .append(" ")
                .append(serviceObj)
                .append(";\n");
        Map<String, String> placeholders = Map.of(
                "packageName",projectDetails.getGroupId(),
                "packageClass",projectDetails.getProjectName(),
                "entityName",projectDetails.getEntityClass().getEntityName(),
                "serviceClassName",projectDetails.getServiceClassName(),
                "requestMapping",projectDetails.getApiURL(),
                "controller_name",projectDetails.getController(),
                "serviceDependency",dependencyBuilder.toString(),
                "methods",createCrudMethods(projectDetails));

        ClassPathResource resource = new ClassPathResource("templates/ControllerTemplate.java");
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
                "controller");

        Files.createDirectories(targetDir); // Ensure directory exists

        // Define target file path
        Path targetPath = targetDir.resolve(projectDetails.getController() + ".java");

        // Write processed content to the new main class
        Files.writeString(targetPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Controller Class created at: " + targetPath);
    }
    private String  createCrudMethods(ProjectDetails projectDetails){
        return """
                \t@GetMapping("all%1$s/{id}")
                \tpublic ResponseEntity<Optional<%1$s>> get%1$sById(@PathVariable(value = "id") Long %2$s){
                \tOptional<%1$s> %3$s = %6$s.find%1$sById(%2$s);
                \treturn ResponseEntity.ok(%3$s);
                \t}
                \t@GetMapping("/all")
                \tpublic ResponseEntity<List<%1$s>> getAll%1$ss(){
                \tList<%1$s> %5$s = %6$s.findAll%1$ss();
                \treturn ResponseEntity.ok(%5$s);
                \t}
                \t@PostMapping("/add")
                \tpublic String add%1$s(@RequestBody %1$s %3$s){
                \t%1$s %3$sResponse = %6$s.save%1$s(%3$s);
                \treturn "Created"+%3$sResponse;
                \t}
                \t@PutMapping("/update/{id}")
                \tpublic ResponseEntity<String> update%1$s(@PathVariable(value = "id") Long %2$s){
                \treturn ResponseEntity.ok("Updated Successfully");
                \t}
                \t@DeleteMapping("/delete/{id}")
                \tpublic ResponseEntity<String> delete%1$s(@PathVariable(value = "id") Long %2$s){
                \treturn ResponseEntity.ok("Deleted Successfully");
                \t}
                """.formatted(projectDetails.getEntityClass().getEntityName(),
                projectDetails.getEntityClass().getEntityName().toLowerCase()+"Id",
                projectDetails.getEntityClass().getEntityName().toLowerCase(),
                projectDetails.getServiceClassName(),
                projectDetails.getEntityClass().getEntityName().toLowerCase()+"List",
                projectDetails.getServiceClassName().substring(0, 1).toLowerCase()+projectDetails.getServiceClassName().substring(1)
        );
    }
}
