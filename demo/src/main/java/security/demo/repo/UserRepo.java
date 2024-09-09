package security.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.demo.model.User;
@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByUsername(String name);
}
