package com.LostAndFound.UserService.repository;

import com.LostAndFound.UserService.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);

    void deleteByEmail(String email);

    @Query(value = "SELECT * FROM Users WHERE status =:b", nativeQuery = true)
    List<Users> findAllStatus(boolean b);
}
