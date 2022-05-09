package com.comp6411;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HeapSort {

    private String filePath;
    private List<String> logger;
    private Runtime runtime = Runtime.getRuntime();

    public HeapSort(String filePath) {
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

    private void heapify(int a[], int n, int i) {
        int largest = i; // Initialize largest as root
        int left = 2 * i + 1; // left child
        int right = 2 * i + 2; // right child
        // If left child is larger than root
        if (left < n && a[left] > a[largest])
            largest = left;
        // If right child is larger than root
        if (right < n && a[right] > a[largest])
            largest = right;
        // If root is not largest
        if (largest != i) {
            // swap a[i] with a[largest]
            int temp = a[i];
            a[i] = a[largest];
            a[largest] = temp;

            heapify(a, n, largest);
        }
    }

    /* Function to implement the heap sort */
    private long heapSort(int a[]) {
        int iterations = 0;
        for (int i = a.length / 2 - 1; i >= 0; i--) {
            heapify(a, a.length, i);
            iterations++;
        }

        // One by one extract an element from heap
        for (int i = a.length - 1; i >= 0; i--) {
            /* Move current root element to end */
            // swap a[0] with a[i]
            int temp = a[0];
            a[0] = a[i];
            a[i] = temp;

            heapify(a, i, 0);
            iterations++;
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

        System.out.println("file reading done, heap sort starts");
        startTime = System.currentTimeMillis();
        long iterations = heapSort(num);
        endTime = System.currentTimeMillis();
        logger.add("Heap sort time in miliseconds: " + (endTime - startTime));
        logger.add("Memory usage after heap sort: " + (runtime.totalMemory() - runtime.freeMemory()));

        System.out.println("heap sort done, writing results");
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
