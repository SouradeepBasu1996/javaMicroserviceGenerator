package ${packageName}.${packageClass}.service;

${imports}
${entity_imports}
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ${serviceClassName}{

${repositories}

${crudMethods}



}