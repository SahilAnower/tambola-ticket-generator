package com.sahil.tambola.service;

import com.sahil.tambola.dao.TambolaRepository;
import com.sahil.tambola.dto.response.TambolaCreateResponseDTO;
import com.sahil.tambola.dto.response.TambolaGetResponseDTO;
import com.sahil.tambola.entity.Tambola;
import com.sahil.tambola.util.HelperUtils;
import com.sahil.tambola.util.Randomizer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TambolaServiceImpl implements TambolaService {
    private final TambolaRepository tambolaRepository;
    private final int[][] ranges = {{1, 9}, {10, 19}, {20, 29}, {30, 39}, {40, 49}, {50, 59}, {60, 69}, {70, 79}, {80, 90}};
    private final int ROW_SIZE = 3;
    private final int COL_SIZE = 9;
    private final int TAMBOLA_SET_TICKET_SIZE = 6;

    @Override
    public TambolaCreateResponseDTO createTambolaSets(Integer n) {
        log.info("Inside tambola creation logic for number of sets, n: {}", n);
        if (n == null || n == 0) {
            throw new IllegalArgumentException("Number of sets cannot be null or 0");
        }
        List<Tambola> tambolas = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            // create 'n' new tambola sets - 6*n
            tambolas.addAll(createTambolaSet());
        }
        return constructTambolaCreateResponseDTOFromTambolas(tambolas);
    }

    private TambolaCreateResponseDTO constructTambolaCreateResponseDTOFromTambolas(List<Tambola> tambolas) {
        Map<String, List<List<Integer>>> tickets = new TreeMap<>();
        tambolas.forEach(tambola -> {
            List<List<Integer>> list = new ArrayList<>();
            List<Integer> allNumbers = HelperUtils.getNumbersFromString(tambola.getArrayNumberSet());
            List<Integer> rowList = new ArrayList<>();
            for (int i = 0; i < allNumbers.size(); i++) {
                if (rowList.size() == COL_SIZE) {
                    list.add(rowList);
                    rowList = new ArrayList<>();
                }
                rowList.add(allNumbers.get(i));
            }
            list.add(rowList);
            tickets.put(tambola.getId().toString(), list);
        });
        return new TambolaCreateResponseDTO(tickets);
    }

    @Override
    public TambolaGetResponseDTO getAllTambolaSets(Integer page, Integer size) {
        log.info("Inside tambola get logic for pageNumber: {} and pageSize: {}", page, size);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        Page<Tambola> pageResult = tambolaRepository.findAll(pageRequest);
        List<Tambola> tambolaList = pageResult.getContent();
        TambolaCreateResponseDTO tambolaCreateResponseDTO = constructTambolaCreateResponseDTOFromTambolas(tambolaList);
        return new TambolaGetResponseDTO(tambolaCreateResponseDTO.getTickets());
    }

    private List<Tambola> createTambolaSet() {
        List<int[][]> tambolaSet = new ArrayList<>();
        for (int i = 1; i <= TAMBOLA_SET_TICKET_SIZE; i++) {
            // create single tambola ticket
            int arr[][] = new int[ROW_SIZE][COL_SIZE];
            tambolaSet.add(arr);
        }
        populateTambolaSet(tambolaSet);
        List<Tambola> savedTambolas = saveTambolaSetInDB(tambolaSet);
        return savedTambolas;
    }

    private void populateTambolaSet(List<int[][]> tambolaSet) {
        List<Set<Integer>> numberSets = new ArrayList<>();
        for (int i = 0; i < ranges.length; i++) {
            Set<Integer> st = new HashSet<>();
            for (int num = ranges[i][0]; num <= ranges[i][1]; num++) {
                st.add(num);
            }
            numberSets.add(st);
        }
        int rowStart = 0, rowEnd = ROW_SIZE - 1, colStart = 0, colEnd = COL_SIZE - 1;
        for (int currCol = colStart; currCol <= colEnd; currCol++) {
            Set<Integer> sets = new HashSet<>();
            for (int i = 0; i < TAMBOLA_SET_TICKET_SIZE; i++) {
                sets.add(i);
            }
            while(!sets.isEmpty()) {
                int setNumber = Randomizer.generate(0, TAMBOLA_SET_TICKET_SIZE - 1);
                if (!sets.contains(setNumber)) {
                    continue;
                }
                int numberToPut = Randomizer.generate(ranges[currCol][0], ranges[currCol][1]);
                if (!numberSets.get(currCol).contains(numberToPut)) {
                    continue;
                }
                int rowNumber = calculateRowNumber(numberToPut, ranges[currCol][0], ranges[currCol][1], ROW_SIZE);
                if (getCurrentRowElementCountGreaterThanOrEqualToFive(tambolaSet, setNumber, rowNumber)) {
                    continue;
                }
                tambolaSet.get(setNumber)[rowNumber][currCol] = numberToPut;
                numberSets.get(currCol).remove(numberToPut);
                sets.remove(setNumber);
            }
        }
        log.info("tambolaSets: {}", tambolaSet);
        log.info("numberSets: {}", numberSets);

        for (int currCol = colEnd; currCol >= colStart; currCol--) {
            recursePopulate(numberSets, tambolaSet, currCol, rowStart, rowEnd);
        }

        sortColumns(tambolaSet, colStart, colEnd, rowStart, rowEnd);
    }

    private void sortColumns(List<int[][]> tambolaSet, int colStart, int colEnd, int rowStart, int rowEnd) {
        for (int currSet = 0; currSet < TAMBOLA_SET_TICKET_SIZE; currSet++) {
            for (int currCol = colStart; currCol <= colEnd; currCol++) {
                List<Integer> sortedOrderList = new ArrayList<>();
                for (int currRow = rowStart; currRow <= rowEnd; currRow++) {
                    if (tambolaSet.get(currSet)[currRow][currCol] > 0) {
                        sortedOrderList.add(tambolaSet.get(currSet)[currRow][currCol]);
                    }
                }
                Collections.sort(sortedOrderList);
                int currIndex = 0;
                for (int currRow = rowStart; currRow <= rowEnd; currRow++) {
                    if (tambolaSet.get(currSet)[currRow][currCol] > 0) {
                        tambolaSet.get(currSet)[currRow][currCol] = sortedOrderList.get(currIndex++);
                    }
                }
            }
        }
    }

    private void recursePopulate(List<Set<Integer>> numberSets, List<int[][]> tambolaSet, int currCol, int rowStart, int rowEnd) {
        if (numberSets.get(currCol).isEmpty()) {
            return;
        }
        int numberToPut = Randomizer.generate(ranges[currCol][0], ranges[currCol][1]);
        while(!numberSets.get(currCol).contains(numberToPut)) {
            numberToPut = Randomizer.generate(ranges[currCol][0], ranges[currCol][1]);
        }
        int setNumber = Randomizer.generate(0, TAMBOLA_SET_TICKET_SIZE - 1);
        int rowNumber = Randomizer.generate(rowStart, rowEnd);
        int count = 0;
        while(tambolaSet.get(setNumber)[rowNumber][currCol] > 0 || getCurrentRowElementCountGreaterThanOrEqualToFive(tambolaSet, setNumber, rowNumber)) {
            setNumber = Randomizer.generate(0, TAMBOLA_SET_TICKET_SIZE - 1);
            count++;
            if (count > 6) {
                rowNumber = Randomizer.generate(rowStart, rowEnd);
            }
            if (count > 20) {
                numberToPut = Randomizer.generate(ranges[currCol][0], ranges[currCol][1]);
            }
            if (count > 1000) {
                return;
            }
        }
        tambolaSet.get(setNumber)[rowNumber][currCol] = numberToPut;
        numberSets.get(currCol).remove(numberToPut);
        recursePopulate(numberSets, tambolaSet, currCol, rowStart, rowEnd);
    }

    private boolean isValidColOrder(int[][] arr, int rowNumber, int currCol, int numberToPut) {
        if (rowNumber == 0) {
            return (arr[rowNumber + 1][currCol] <= 0 || arr[rowNumber + 1][currCol] >= numberToPut) &&
                    (arr[rowNumber + 2][currCol] <= 0 || arr[rowNumber + 2][currCol] >= numberToPut);
        } else if (rowNumber == 1) {
            return (arr[rowNumber - 1][currCol] <= 0 || arr[rowNumber - 1][currCol] <= numberToPut) &&
                    (arr[rowNumber + 1][currCol] <= 0 || arr[rowNumber + 1][currCol] >= numberToPut);
        }else {
            return (arr[rowNumber - 1][currCol] <= 0 || arr[rowNumber - 1][currCol] <= numberToPut) &&
                    (arr[rowNumber - 2][currCol] <= 0 || arr[rowNumber - 2][currCol] <= numberToPut);
        }
    }

    private boolean getCurrentRowElementCountGreaterThanOrEqualToFive(List<int[][]> tambolaSet, int setNumber, int rowNumber) {
        int[][] arr = tambolaSet.get(setNumber);
        int count = 0;
        for (int i = 0; i < COL_SIZE; i++) {
            if (arr[rowNumber][i] > 0) {
                count++;
            }
        }
        return count >= 5;
    }

    private int calculateRowNumber(int numberToPut, int rangeStart, int rangeEnd, int rowSize) {
        int pos = (numberToPut - rangeStart) / rowSize;
        if (pos > 2) {
            return 2;
        }else return Math.max(pos, 0);
    }

    @Transactional
    private List<Tambola> saveTambolaSetInDB(List<int[][]> tambolaSet) {
        List<Tambola> toSaveTambolas = tambolaSet.stream().map(t -> {
            Tambola tambola = new Tambola();
            String arrayNumberSet = HelperUtils.convert2DArrayToString(t);
            tambola.setArrayNumberSet(arrayNumberSet);
            return tambola;
        }).toList();
        return tambolaRepository.saveAll(toSaveTambolas);
    }
}