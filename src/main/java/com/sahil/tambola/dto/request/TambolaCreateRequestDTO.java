package com.sahil.tambola.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TambolaCreateRequestDTO implements Serializable {
    @NotNull
    private Integer numberOfSets;
}
