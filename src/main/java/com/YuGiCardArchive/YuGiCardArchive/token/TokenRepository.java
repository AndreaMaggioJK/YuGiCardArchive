package com.YuGiCardArchive.YuGiCardArchive.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface TokenRepository extends JpaRepository<Token, Integer> {

  @Query("SELECT t FROM Token t INNER JOIN t.user u " +
       "WHERE u.id = :id AND (t.expired = false OR t.revoked = false)")
  List<Token> findAllValidTokenByUser(@Param("id") Integer id);

  Optional<Token> findByToken(String token);

  @Transactional
  @Modifying
  @Query("DELETE FROM Token t WHERE t.expired = true")
  void deleteExpiredTokens();

}