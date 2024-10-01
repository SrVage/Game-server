package org.gameserver.repositories;

import org.gameserver.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<Object> findByUuid(UUID uuid);
}
