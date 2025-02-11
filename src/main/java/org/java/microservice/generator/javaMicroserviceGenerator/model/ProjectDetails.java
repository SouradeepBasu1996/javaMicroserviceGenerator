package org.java.microservice.generator.javaMicroserviceGenerator.model;

import lombok.*;

public class ProjectDetails {

    private String projectName;
    private String groupId;
    private String artifactId;
    private BuildTool buildTool;
    private String description;
    private String controller;
    private String apiURL;
    private EntityClass entityClass;
    private DatabaseSpecs databaseSpecs;
    private String serviceClassName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public BuildTool getBuildTool() {
        return buildTool;
    }

    public void setBuildTool(BuildTool buildTool) {
        this.buildTool = buildTool;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public EntityClass getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(EntityClass entityClass) {
        this.entityClass = entityClass;
    }

    public DatabaseSpecs getDatabaseSpecs() {
        return databaseSpecs;
    }

    public void setDatabaseSpecs(DatabaseSpecs databaseSpecs) {
        this.databaseSpecs = databaseSpecs;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }
}
