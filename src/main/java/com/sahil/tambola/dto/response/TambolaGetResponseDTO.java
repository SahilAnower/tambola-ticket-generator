package com.sahil.tambola.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TambolaGetResponseDTO implements Serializable {
    private Map<String, List<List<Integer>>> tickets;
}
