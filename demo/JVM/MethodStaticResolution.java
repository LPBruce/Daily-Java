package demo.JVM;

// 静态方法 invokestatic调用

public class MethodStaticResolution {
    public static void sayHello() {
        System.out.println("Hello world");
    }

    public static void main(String[] args) {
        MethodStaticResolution.sayHello();
    }
}
