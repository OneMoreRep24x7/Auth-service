package com.ashish.Authservice.token;

import com.ashish.Authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token,Integer> {

    @Query(value = """
      SELECT t FROM Token t INNER JOIN User u
      ON t.user.id = u.id
      WHERE u.id = :id AND (t.expired = false OR t.revoked = false)
      """)
    List<Token> findAllValidTokenByUser(UUID id);
    Optional<Token> findByToken(String token);
}
