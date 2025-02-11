package org.java.microservice.generator.javaMicroserviceGenerator.model;


public class EntityModel {
    private String fieldName;
    private DataType dataType;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
