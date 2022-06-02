package com.comp6411;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class HashTable {

    static private class ListNode {
        Object key;
        ListNode next;
    }

    private ListNode[] table;
    private int itemsCount;
    private String filePath;
    private List<String> logger;
    private Runtime runtime = Runtime.getRuntime();
    private long access = 0;

    public HashTable(String path) {
        // Create a hash table with an initial size of 64.
        table = new ListNode[64];
        this.filePath = path;
        logger = new ArrayList<>();
    }

    public HashTable(int initialSize) {
        // Create a hash table with a specified initial size.
        // Precondition: initalSize > 0.
        table = new ListNode[initialSize];
    }

    public void add(Object key) {
        int bucket = hash(key);
        ListNode list = table[bucket];

        while (list != null) {
            if (list.key.equals(key))
                break;
            list = list.next;
        }
        if (list == null) {
            if (itemsCount >= 0.75 * table.length) {
                resize();
            }
            ListNode newNode = new ListNode();
            newNode.key = key;
            newNode.next = table[bucket];
            table[bucket] = newNode;
            itemsCount++;
        }
    }

    public void remove(Object key) {
        int bucket = hash(key);
        if (table[bucket] == null) {
            return;
        }
        if (table[bucket].key.equals(key)) {
            table[bucket] = table[bucket].next;
            itemsCount--;
            return;
        }
        ListNode prev = table[bucket];
        ListNode curr = prev.next;
        while (curr != null && !curr.key.equals(key)) {
            curr = curr.next;
            prev = curr;
        }
        if (curr != null) {
            prev.next = curr.next;
            itemsCount--;
        }
    }

    public boolean containsKey(Object key) {
        access++;
        int bucket = hash(key);
        ListNode list = table[bucket];
        while (list != null) {
            access++;
            if (list.key.equals(key))
                return true;
            list = list.next;
        }
        return false;
    }

    private int hash(Object key) {
        return (Math.abs(key.hashCode())) % table.length;
    }

    private void resize() {
        ListNode[] newtable = new ListNode[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            ListNode list = table[i];
            while (list != null) {
                ListNode next = list.next;
                int hash = (Math.abs(list.key.hashCode())) % newtable.length;
                list.next = newtable[hash];
                newtable[hash] = list;
                list = next;
            }
        }
        table = newtable;
    }

    private void writeResult(List<String> logger) {
        File fout = new File("output.txt");

        try (FileWriter fileWriter = new FileWriter(fout);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("--------statistics--------\n");
            for (String str : logger) {
                bufferedWriter.write(str + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHashMap(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                add(Integer.parseInt(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        long startTime = 0;
        long endTime = 0;
        long usedMemoryBefore;
        long usedMemoryAfter;

        System.out.println("reading file from " + filePath);

        usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        loadHashMap(filePath);
        endTime = System.currentTimeMillis();
        usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        logger.add("Reading input file time in miliseconds: " + (endTime - startTime));
        logger.add("Memory usage before reading input file into hash table: " + usedMemoryBefore + " bytes");
        logger.add("Memory usage after reading input file into hash table: " + usedMemoryAfter + " bytes");

        int[] success = new int[] { 522821228, 426088780, 717686692, 652705375, 207855228 };
        boolean result;
        for (int i : success) {
            System.out.println("searching value: " + i + " in hash table");
            logger.add("searching value: " + i + " in hash table");
            startTime = System.currentTimeMillis();
            access = 0;
            result = containsKey(i);
            endTime = System.currentTimeMillis();
            if (result) {
                System.out.println("hit!");
                logger.add("hit!");
            } else {
                System.out.println("miss!");
                logger.add("miss!");
            }
            logger.add("Hash table value lookup time in miliseconds: " + (endTime - startTime));
            logger.add("Hash table value lookup access count: " + access);
        }

        int[] fail = new int[] { 0, 100000000, 300000000, 600000000, 1000000000 };
        for (int i : fail) {
            System.out.println("searching value: " + i + " in hash table");
            logger.add("searching value: " + i + " in hash table");
            startTime = System.currentTimeMillis();
            access = 0;
            result = containsKey(i);
            endTime = System.currentTimeMillis();
            if (result) {
                System.out.println("hit!");
                logger.add("hit!");
            } else {
                System.out.println("miss!");
                logger.add("miss!");
            }
            logger.add("Hash table value lookup time in miliseconds: " + (endTime - startTime));
            logger.add("Hash table value lookup access count: " + access);
        }

        System.out.println("done, writing results to file");
        writeResult(logger);
        System.out.println("done!");
    }
}
