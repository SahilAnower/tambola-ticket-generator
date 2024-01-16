package com.sahil.tambola.service;

import com.sahil.tambola.entity.Tambola;
import org.springframework.data.domain.Page;

public interface TambolaService {
    void createTambolaSets (Integer n);
    Page<Tambola> getAllTambolaSets (Integer page, Integer size);
}
