package hanu.devteria.repository;

import hanu.devteria.model.InvalidateToken;
import hanu.devteria.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidateTokenRepository extends JpaRepository<InvalidateToken,String> {

}
