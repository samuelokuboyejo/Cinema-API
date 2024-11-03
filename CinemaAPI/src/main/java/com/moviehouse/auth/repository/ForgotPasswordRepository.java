package com.moviehouse.auth.repository;

import com.moviehouse.auth.entities.ForgotPassword;
import com.moviehouse.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("select forgotPassword from ForgotPassword forgotPassword where forgotPassword.otp = ?1 and forgotPassword.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}
