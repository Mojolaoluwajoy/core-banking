package com.corebanking.clientservice.repository;

import com.corebanking.clientservice.entity.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  boolean existsByEmail(String email);

  Optional<Client> findByEmail(String email);
}
