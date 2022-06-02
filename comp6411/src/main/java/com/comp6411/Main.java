package com.comp6411;

public class Main {
    public static void main(String[] args) {
        String path;
        if (args.length == 0) {
            path = Main.class.getClassLoader().getResource("rand.txt").getPath();
        } else {
            path = args[0];
        }

        new MergeSort(path).start();
    }
}
