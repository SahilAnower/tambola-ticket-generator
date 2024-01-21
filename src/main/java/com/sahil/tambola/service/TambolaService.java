package com.sahil.tambola.service;

import com.sahil.tambola.dto.response.TambolaCreateResponseDTO;
import com.sahil.tambola.dto.response.TambolaGetResponseDTO;
import com.sahil.tambola.entity.Tambola;
import org.springframework.data.domain.Page;

public interface TambolaService {
    TambolaCreateResponseDTO createTambolaSets (Integer n);
    TambolaGetResponseDTO getAllTambolaSets (Integer page, Integer size);
}
