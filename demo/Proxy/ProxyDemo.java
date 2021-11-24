package demo.Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
// import java.lang.reflect.*;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Random;

/**
 * 本Demo是Java proxy代理代码示例，内容来源于《java核心技术 卷1》
 * 核心功能是使用代理对象对二分查找进行跟踪
 * 
 */
public class ProxyDemo {
    public static void main(String[] args) {
        // 使用代理proxy填充数组，所以注意：这个是代理数组，相当于用proxy代替了整数进行计算
        Object[] elements = new Object[1000];
        for (int i = 0; i < elements.length; i++) {
            Integer value = i + 1;
            // TraceHandler为包装器类,实现了InvocationHandler接口，并保存了隐含参数
            InvocationHandler handler = new TraceHandler(value);
            // 生成 Proxy对象实例，第一个参数是类加载器，默认为null, 第二个参数是Class数组，第三个是调用处理器
            Object proxy = Proxy.newProxyInstance(null, new Class[] {Comparable.class}, handler);
            // 将代理对象填充数组，这里的proxy就是一个实现了给定接口的新类！！！
            elements[i] = proxy;
        }

        // 构造一个随机的整数
        Integer key = new Random().nextInt(elements.length) + 1;

        // 二分法查找, 由于elements实现了Comparable接口，所以此新对象是可以作为binarySearch的参数
        int result = Arrays.binarySearch(elements, key);

        // 如果找到则打印
        if (result >= 0) {
            System.out.print(elements[result]);
        }
    }
}

/**
 * TraceHandler为包装器类，内部包装了了一个Integer类，并实现了InvocationHandler调用处理器接口
 * 
 * InvocationHandler调用处理器接口的主要作用就是用来实现
 * 
 */
class TraceHandler implements InvocationHandler{
    private Object target;
    /**
     * TraceHandle是包装器类
     * @param t 调用方法的隐含参数
     */
    public TraceHandler(Object t) {
        this.target = t;
    }

    /**
    * InvocationHandler调用处理器接口
    * @param proxy 代理
    * @param m  Method对象
    * @param args 参数
    */
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        // 打印隐含参数，调用静态方法时，隐含参数可以为null
        System.out.print(target);
        // 打印 Method方法名
        System.out.print("." + m.getName() + "(");
        // 打印显式参数，这里只有一个
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                System.out.print(args[i]);
                if (i < args.length -1) {
                    System.out.print(",");
                }
            }
        }
        System.out.println(")");
        // 调用 Method方法，传入参数，这里就是compareTo比较函数了
        return m.invoke(target, args);
    }

    // 结果：
    // 500.compareTo(274)
    // 250.compareTo(274)
    // 375.compareTo(274)
    // 312.compareTo(274)
    // 281.compareTo(274)
    // 265.compareTo(274)
    // 273.compareTo(274)
    // 277.compareTo(274)
    // 275.compareTo(274)
    // 274.compareTo(274)
    // 274.toString()
    // 274
}