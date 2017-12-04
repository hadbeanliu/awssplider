package test;

public class SimpleTest {

    public static void main(String[] args){
        SimpleTest t=new SimpleTest();

        System.out.println(t.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        System.out.println(System.getProperty("user.dir"));
    }
}
