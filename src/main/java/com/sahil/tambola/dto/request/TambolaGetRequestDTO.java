package com.sahil.tambola.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TambolaGetRequestDTO implements Serializable {
    @Min(0)
    private Integer page = 0;
    @Min(0)
    private Integer size = 5;
}
