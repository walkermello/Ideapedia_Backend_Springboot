package com.tugasakhir.ideapedia.repo;

import com.tugasakhir.ideapedia.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

        // Retrieves the most recently created User by ID in descending order
        Optional<User> findTopByOrderByIdDesc();

        // Finds users where the email contains the given value, ignoring case
        Page<User> findByEmailContainingIgnoreCase(Pageable pageable, String value);

        // Finds users where the NIP contains the given value, ignoring case
        Page<User> findByNipContainingIgnoreCase(Pageable pageable, String value);

        // Finds users where the phone number contains the given value, ignoring case
        Page<User> findByNoHpContainingIgnoreCase(Pageable pageable, String value);

        // Finds users where the username contains the given value, ignoring case
        Page<User> findByUsernameContainingIgnoreCase(Pageable pageable, String value);

        // Finds users where the status contains the given value, ignoring case
        Page<User> findByStatusContainingIgnoreCase(Pageable pageable, String value);

        // Finds users by username
        Optional<User> findByUsername(String value);
}
