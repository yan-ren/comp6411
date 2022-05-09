package com.comp6411;

public class Main {
    public static void main(String[] args) {
        String path = Main.class.getClassLoader().getResource("rand.txt").getPath();
        new HeapSort(path).start();
    }
}
