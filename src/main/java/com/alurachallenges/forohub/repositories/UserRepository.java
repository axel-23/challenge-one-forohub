package com.alurachallenges.forohub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.alurachallenges.forohub.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByUsername(String username);

    boolean existsByUsername(String username);
}
