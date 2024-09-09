package hanu.devteria.repository;

import hanu.devteria.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public boolean existsByName(String name);
    Optional<User> findByName(String name);
}
