注解 Annotation

+ 注解是当做一个修饰符来使用的，他被置于被注解项之前，中间没有分号。
+ 注解可以定义成包含元素的形式
+ 注解内容包括：方法，类，成员，局部变量，包，参数变量，类型参数和类型用法
+ 每个注解都必须通过一个注解接口进行定义，这些接口中的方法与注解中的元素相对应，例如 JUnit的注解`@Test(timeout="10000")`
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    // timeout()方法与 @Test(timeout="10000")中的timeout相对应
    long timeout() default 0L;
}
```
+ @interface创建了一个真正的Java接口
+ @Target和@Retention是元注解
+ 
+
+ 
 