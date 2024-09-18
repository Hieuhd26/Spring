package hanu.devteria.repository;

import hanu.devteria.model.Permission;
import hanu.devteria.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {

}
