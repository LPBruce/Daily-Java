package demo.Annotation;

@PersonInfo(value = "yongman", isYoug = true)
public class Person {
    private String name;

    private long age;

    public Person(String name, long age) {
        this.name = name;
        this.age = age;
    }
}
