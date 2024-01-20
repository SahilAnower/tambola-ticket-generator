package com.sahil.tambola.controller;

import com.sahil.tambola.dto.request.TambolaCreateRequestDTO;
import com.sahil.tambola.dto.request.TambolaGetRequestDTO;
import com.sahil.tambola.dto.response.TambolaCreateResponseDTO;
import com.sahil.tambola.dto.response.TambolaGetResponseDTO;
import com.sahil.tambola.entity.Tambola;
import com.sahil.tambola.service.TambolaService;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tambola")
@RequiredArgsConstructor
public class TambolaController {
    private final TambolaService tambolaService;
    @PostMapping
    public TambolaCreateResponseDTO createNewTambolaTickets (@RequestBody TambolaCreateRequestDTO tambolaCreateRequestDTO) {
        return tambolaService.createTambolaSets(tambolaCreateRequestDTO.getNumberOfSets());
    }
    @GetMapping
    public TambolaGetResponseDTO getTambolaTickets (@ParameterObject TambolaGetRequestDTO tambolaGetRequestDTO) {
        return tambolaService.getAllTambolaSets(tambolaGetRequestDTO.getPage(), tambolaGetRequestDTO.getSize());
    }
}
