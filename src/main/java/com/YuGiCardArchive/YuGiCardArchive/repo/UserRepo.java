package com.YuGiCardArchive.YuGiCardArchive.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.YuGiCardArchive.YuGiCardArchive.model.User;


public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
