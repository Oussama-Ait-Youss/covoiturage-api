package com.covoiturage.api.repository;

import com.covoiturage.api.entity.Passager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PassagerRepository extends JpaRepository<Passager,Long> {

}
