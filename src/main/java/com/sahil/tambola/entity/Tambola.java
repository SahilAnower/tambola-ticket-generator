package com.sahil.tambola.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tambola")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tambola {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // saving each 2d array as a string
    @Column(name = "array_number_set")
    private String arrayNumberSet;
}
