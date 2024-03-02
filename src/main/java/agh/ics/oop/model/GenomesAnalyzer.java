package agh.ics.oop.model;

import java.util.*;
import java.util.Map;

public class GenomesAnalyzer {

    private List<Integer[]> genomesList;

    public GenomesAnalyzer(List<Integer[]> genomesList) {
        this.genomesList = genomesList;
    }

    public Integer[] findMostPopularGenome() {
        Map<List<Integer>, Integer> genomeCountMap = new HashMap<>();

        for (Integer[] genomeArray : genomesList) {
            List<Integer> genomeList = Arrays.asList(genomeArray);
            genomeCountMap.put(genomeList, genomeCountMap.getOrDefault(genomeList, 0) + 1);
        }

        List<Integer> mostPopularGenomeList = Collections.max(genomeCountMap.entrySet(), Map.Entry.comparingByValue()).getKey();

        return mostPopularGenomeList.toArray(new Integer[0]);
    }
}
