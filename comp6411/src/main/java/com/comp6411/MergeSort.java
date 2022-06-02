package com.comp6411;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// A Linked List Node
class Node {
    int data;
    Node next;

    Node(int data, Node next) {
        this.data = data;
        this.next = next;
    }
}

class LinkedList {
    Node head;

    public void addFrist(int data) {
        if (head == null) {
            head = new Node(data, null);
        } else {
            Node newNode = new Node(data, this.head);
            this.head = newNode;
        }
    }

    public void printList() {
        Node ptr = this.head;
        while (ptr != null) {
            System.out.print(ptr.data + " —> ");
            ptr = ptr.next;
        }

        System.out.println("null");
    }
}

public class MergeSort {

    private String filePath;
    private List<String> logger;
    private Runtime runtime = Runtime.getRuntime();

    public MergeSort(String path) {
        this.filePath = path;
        logger = new ArrayList<>();
    }

    public void start() {
        long startTime = 0;
        long endTime = 0;
        long usedMemoryBefore;
        long usedMemoryAfter;
        LinkedList evenPrime = new LinkedList();
        LinkedList evenUnprime = new LinkedList();
        LinkedList oddPrime = new LinkedList();
        LinkedList oddUnprime = new LinkedList();

        System.out.println("reading file from " + filePath);

        usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        loadLinkedList(evenPrime, evenUnprime, oddPrime, oddUnprime);
        endTime = System.currentTimeMillis();
        usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        logger.add("Reading input file and populate four linked lists, time in miliseconds: " + (endTime - startTime));
        logger.add("Memory usage before reading input file into hash table: " + usedMemoryBefore + " bytes");
        logger.add("Memory usage after reading input file into hash table: " + usedMemoryAfter + " bytes");

        System.out.println("Merge sort starts...");
        multiThreadingMergeSort(evenPrime, evenUnprime, oddPrime, oddUnprime);
        System.out.println("done!");
    }

    private void multiThreadingMergeSort(LinkedList evenPrime, LinkedList evenUnprime, LinkedList oddPrime,
            LinkedList oddUnprime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = 0;
                long endTime = 0;
                List<String> localLogger = new ArrayList<>();
                System.out.println("Start sorting even prime linked list");
                startTime = System.currentTimeMillis();
                evenPrime.head = mergesort(evenPrime.head);
                endTime = System.currentTimeMillis();
                localLogger.add("Reading input file and populate four linked lists, time in miliseconds: "
                        + (endTime - startTime));
            }
        }).start();
    }

    private void loadLinkedList(LinkedList evenPrime, LinkedList evenUnprime, LinkedList oddPrime,
            LinkedList oddUnprime) {

        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                int number = Integer.parseInt(line);
                boolean prime = isPrime(number);
                boolean even = isEvent(number);

                if (even && prime) {
                    evenPrime.addFrist(number);
                } else if (even && !prime) {
                    evenUnprime.addFrist(number);
                } else if (!even && prime) {
                    oddPrime.addFrist(number);
                } else if (!even && !prime) {
                    oddUnprime.addFrist(number);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Node merge(Node a, Node b) {
        // base cases
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        }

        Node result;

        // pick either `a` or `b`, and recur
        if (a.data <= b.data) {
            result = a;
            result.next = merge(a.next, b);
        } else {
            result = b;
            result.next = merge(a, b.next);
        }

        return result;
    }

    public static Node[] split(Node source) {
        // if the length is less than 2, handle it separately
        if (source == null || source.next == null) {
            return new Node[] { source, null };
        }

        Node slow = source;
        Node fast = source.next;

        // advance `fast` two nodes, and advance `slow` one node
        while (fast != null) {
            fast = fast.next;
            if (fast != null) {
                slow = slow.next;
                fast = fast.next;
            }
        }

        // `slow` is before the midpoint in the list, so split it in two
        // at that point.
        Node[] arr = new Node[] { source, slow.next };
        slow.next = null;

        return arr;
    }

    // Sort a given linked list using the merge sort algorithm
    public static Node mergesort(Node head) {
        // base case — length 0 or 1
        if (head == null || head.next == null) {
            return head;
        }

        // split `head` into `a` and `b` sublists
        Node[] arr = split(head);
        Node front = arr[0];
        Node back = arr[1];

        // recursively sort the sublists
        front = mergesort(front);
        back = mergesort(back);

        // answer = merge the two sorted lists
        return merge(front, back);
    }

    public static boolean isPrime(int number) {
        if (number == 2 || number == 3) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }
        int sqrt = (int) Math.sqrt(number) + 1;
        for (int i = 3; i < sqrt; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEvent(int number) {
        return number % 2 == 0;
    }

}
