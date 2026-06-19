
package com.corebanking.clientservice.repository;

import com.corebanking.clientservice.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {


    boolean existsByEmail(String email);

   Optional<Client> findByEmail(String email);
}