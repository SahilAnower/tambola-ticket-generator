package com.sahil.tambola.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TambolaGetRequestDTO implements Serializable {
    private Integer page = 0;
    private Integer size = 5;
}
