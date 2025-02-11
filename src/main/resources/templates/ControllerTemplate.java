package ${packageName}.${packageClass}.controller;
import ${packageName}.${packageClass}.entity.${entityName};
import ${packageName}.${packageClass}.service.${serviceClassName};
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${requestMapping}")
public class ${controller_name}{

    @Autowired
    ${serviceDependency}

${methods}

}