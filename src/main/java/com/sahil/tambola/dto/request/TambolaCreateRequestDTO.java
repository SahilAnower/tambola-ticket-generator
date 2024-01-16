package com.sahil.tambola.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TambolaCreateRequestDTO implements Serializable {
    private Integer numberOfSets;
}
