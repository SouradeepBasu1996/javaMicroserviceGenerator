package ${packageName}.${packageClass}.repository;

import ${packageName}.${packageClass}.entity.${model_name};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${model_name}Repository extends JpaRepository<${model_name},Long>{

}