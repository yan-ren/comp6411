package com.comp6411;

import org.junit.Test;

public class HeapSortTest {
    @Test
    public void testStart() {
        String path = Main.class.getClassLoader().getResource("test.txt").getPath();
        new HeapSort(path).start();
    }
}
