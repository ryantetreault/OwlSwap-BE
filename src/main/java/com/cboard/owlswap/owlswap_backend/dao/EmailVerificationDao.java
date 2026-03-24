package com.cboard.owlswap.owlswap_backend.dao;

import com.cboard.owlswap.owlswap_backend.security.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationDao extends JpaRepository<EmailVerificationToken, Long>
{
    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

}
