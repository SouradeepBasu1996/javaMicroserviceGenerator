package org.java.microservice.generator.javaMicroserviceGenerator.service;

import org.java.microservice.generator.javaMicroserviceGenerator.model.DataType;
import org.springframework.stereotype.Service;

@Service
public class DataTypeService {

    public String resolveDataType(DataType dataType){

        if(dataType.equals(DataType.INTEGER))
            return "Integer";

        if(dataType.equals(DataType.BOOLEAN))
            return "Boolean";

        if(dataType.equals(DataType.STRING))
            return "String";

        if(dataType.equals(DataType.LONG))
            return "Long";

        if(dataType.equals(DataType.DOUBLE))
            return "Double";

        if(dataType.equals(DataType.CHARACTER))
            return "Character";

        return "";
    }
}
