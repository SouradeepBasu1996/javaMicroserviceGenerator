package org.java.microservice.generator.javaMicroserviceGenerator.model;


import java.util.List;


public class EntityClass {

    private String entityName;
    private List<EntityModel> fields;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<EntityModel> getFields() {
        return fields;
    }

    public void setFields(List<EntityModel> fields) {
        this.fields = fields;
    }
}
