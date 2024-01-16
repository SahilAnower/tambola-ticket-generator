package com.sahil.tambola.service;

import com.sahil.tambola.dao.TambolaRepository;
import com.sahil.tambola.entity.Tambola;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class TambolaServiceImpl implements TambolaService{
    private final TambolaRepository tambolaRepository;
    @Override
    public void createTambolaSets(Integer n) {
        if (n == null || n == 0) {
            throw new IllegalArgumentException("Number of sets cannot be null or 0");
        }
        for (int i = 0; i < n; i++) {
            // create 'n' new tambola sets - 6*n
            createTambolaSet();
        }
    }

    @Override
    public Page<Tambola> getAllTambolaSets(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return tambolaRepository.findAll(pageRequest);
    }

    private void createTambolaSet() {
        Set<Integer> numberSet = new HashSet<>();
        // total number set for each tambola set from 1 to 90
        for (int i = 1; i <= 90; i++) {
            numberSet.add(i);
        }
        for (int i = 1; i <= 6; i++) {
            // create single tambola ticket
            int arr[][] = new int[3][9];
            createTambola(arr, numberSet);
        }
    }

    private void createTambola(int[][] arr, Set<Integer> numberSet) {

    }
}
