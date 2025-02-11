package org.java.microservice.generator.javaMicroserviceGenerator.service;

import lombok.extern.slf4j.Slf4j;
import org.java.microservice.generator.javaMicroserviceGenerator.model.ProjectDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CreateProjectService {

    @Value("${app.working-directory}")
    private String workingDirectory;

    private ZipService zipService;
    private PomGeneratorService pomGeneratorService;
    private PropertiesFileGenerator propertiesFileGenerator;
    private MainClassService mainClassService;
    private GenerateControllerService generateControllerService;
    private GenerateEntityService generateEntityService;
    private GenerateServiceClass generateServiceClass;
    private GenerateRepositoryService generateRepositoryService;

    public CreateProjectService(ZipService zipService,
                                PomGeneratorService pomGeneratorService,
                                PropertiesFileGenerator propertiesFileGenerator,
                                MainClassService mainClassService,
                                GenerateControllerService generateControllerService,
                                GenerateEntityService generateEntityService,
                                GenerateServiceClass generateServiceClass,
                                GenerateRepositoryService generateRepositoryService){
        this.zipService=zipService;
        this.pomGeneratorService=pomGeneratorService;
        this.propertiesFileGenerator=propertiesFileGenerator;
        this.mainClassService=mainClassService;
        this.generateControllerService=generateControllerService;
        this.generateEntityService=generateEntityService;
        this.generateServiceClass=generateServiceClass;
        this.generateRepositoryService=generateRepositoryService;
    }

    public Path createProject(ProjectDetails projectDetails)throws IOException{

        Path projectDir = getProjectRootPath(projectDetails.getProjectName());

        pomGeneratorService.generatePom(projectDetails);
        propertiesFileGenerator.generatePropertiesFile(projectDetails);
        mainClassService.createMainClass(projectDetails);
        generateControllerService.createControllerClass(projectDetails);
        generateEntityService.createEntityClass(projectDetails);
        generateServiceClass.createServiceClass(projectDetails);
        generateRepositoryService.createRepository(projectDetails);


        Files.createDirectories(projectDir);

        createBasicStructure(projectDetails);

        return zipService.zipProject(projectDir);
    }

    private void createBasicStructure(ProjectDetails projectDetails)throws IOException {
        Path projectDir = getProjectRootPath(projectDetails.getProjectName());

        List<Path> directories = List.of(
                projectDir.resolve("src/main/java/"
                        + projectDetails.getGroupId().replace(".", "/")
                        +"/"
                        +projectDetails.getProjectName().replace(".","/")),
                projectDir.resolve("src/test/java/"
                        +projectDetails.getGroupId().replace(".", "/")
                        +"/"
                        +projectDetails.getProjectName().replace(".","/")),
                projectDir.resolve("src/main/resources")
        );

        for (Path dir : directories) {
            try{
                Files.createDirectories(dir);
                System.out.println("Directory created successfully :"+dir);
            }catch (IOException ioException){
                System.out.println("Failed to create directory : "+dir);
            }
        }
        createFileIfNotExists(projectDir.resolve("pom.xml"));
        createFileIfNotExists(projectDir.resolve("README.md"));
        createFileIfNotExists(projectDir.resolve(".gitignore"));

        System.out.println("Project Structure created successfully : CreateProjectService.createBasicStructure");
    }
    private Path getProjectRootPath(String projectName) {
        return Paths.get(workingDirectory, projectName);
    }
    private void createFileIfNotExists(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }
}
