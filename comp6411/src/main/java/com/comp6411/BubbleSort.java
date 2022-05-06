package com.comp6411;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BubbleSort {

    private String filePath;
    private List<String> logger;
    private Runtime runtime = Runtime.getRuntime();

    public BubbleSort(String filePath) {
        this.filePath = filePath;
        logger = new ArrayList<>();
    }

    private int[] loadFile(String file) {
        List<Integer> tmp = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                tmp.add(Integer.parseInt(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp.stream().mapToInt(i -> i).toArray();
    }

    private long bubbleSort(int[] num) {
        int size = num.length;
        long iterations = 0;
        for (int i = 0; i < (size - 1); i++) {
            iterations++;
            boolean swapped = false;
            for (int j = 0; j < (size - i - 1); j++) {
                iterations++;
                if (num[j] > num[j + 1]) {
                    int temp = num[j];
                    num[j] = num[j + 1];
                    num[j + 1] = temp;

                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
        return iterations;
    }

    public void start() {
        long startTime = 0;
        long endTime = 0;
        long usedMemoryBefore;
        long usedMemoryAfter;

        System.out.println("reading file from " + filePath);
        usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        int[] num = loadFile(this.filePath);
        endTime = System.currentTimeMillis();
        usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        logger.add("Reading input file time in miliseconds: " + (endTime - startTime));
        logger.add("Memory usage before reading input file: " + usedMemoryBefore);
        logger.add("Memory usage after reading input file: " + usedMemoryAfter);

        System.out.println("file reading done, bubble sort starts");
        startTime = System.currentTimeMillis();
        long iterations = bubbleSort(num);
        endTime = System.currentTimeMillis();
        logger.add("Bubble sort time in miliseconds: " + (endTime - startTime));
        logger.add("Memory usage after bubble sort: " + (runtime.totalMemory() - runtime.freeMemory()));

        System.out.println("bubble sort done, writing results");
        writeResult(num, iterations);
        System.out.println("done!");
    }

    private void writeResult(int[] num, long iterations) {
        File fout = new File("output.txt");

        try (FileWriter fileWriter = new FileWriter(fout);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (int n : num) {
                bufferedWriter.write(n + ", ");
            }
            bufferedWriter.newLine();

            bufferedWriter.write("--------statistics--------\n");
            bufferedWriter.write("number of loop iterations: " + iterations + "\n");

            for (String str : logger) {
                bufferedWriter.write(str + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
