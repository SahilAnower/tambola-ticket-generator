package com.sahil.tambola.dao;

import com.sahil.tambola.entity.Tambola;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TambolaRepository extends JpaRepository<Tambola, Long> {
}
