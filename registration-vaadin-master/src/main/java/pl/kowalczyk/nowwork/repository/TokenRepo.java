package pl.kowalczyk.nowwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kowalczyk.nowwork.model.Token;
import pl.kowalczyk.nowwork.model.User;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token,Long> {
    Optional<Token> findTokenByToken(String token);
}

