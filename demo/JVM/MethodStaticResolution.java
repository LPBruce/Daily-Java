package demo.JVM;

public class MethodStaticResolution {
    public static void sayHello() {
        System.out.println("Hello world");
    }

    public static void main(String[] args) {
        MethodStaticResolution.sayHello();
    }
}
