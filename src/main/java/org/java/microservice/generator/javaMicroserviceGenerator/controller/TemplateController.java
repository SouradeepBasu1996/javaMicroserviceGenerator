package org.java.microservice.generator.javaMicroserviceGenerator.controller;

import lombok.AllArgsConstructor;
import org.java.microservice.generator.javaMicroserviceGenerator.model.ProjectDetails;
import org.java.microservice.generator.javaMicroserviceGenerator.service.CreateProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TemplateController {

    private CreateProjectService createProjectService;

    public TemplateController(CreateProjectService createProjectService){
        this.createProjectService=createProjectService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Resource> createProject(@RequestBody ProjectDetails projectDetails) throws IOException {
        Path zipFilePath = createProjectService.createProject(projectDetails);

        if (zipFilePath == null) {
            return ResponseEntity.internalServerError().build();
        }

        Resource resource = new UrlResource(zipFilePath.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFilePath.getFileName() + "\"")
                .body(resource);
    }
}
