package test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleTest {

    public static void main(String[] args) throws IOException {

        AtomicInteger atomicInteger =new AtomicInteger();
        for(int i =0;i<100;i++)
            System.out.println(atomicInteger.getAndIncrement());

        AtomicInteger atomicInteger2 =new AtomicInteger();
        for(int i =0;i<100;i++)
            System.out.println(atomicInteger2.getAndIncrement());
    }
}
