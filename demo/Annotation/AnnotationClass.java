package demo.Annotation;


public class AnnotationClass {
    public static void main(String[] args) {
        try {
            Person person = new Person("张三", 10);
            Class<?> clazz = person.getClass();
            if (clazz.isAnnotationPresent(PersonInfo.class)) {
                System.out.println("Person 配置了PersonInfo注解");
                PersonInfo personInfo = (PersonInfo) clazz.getAnnotation(PersonInfo.class);
                System.out.println("Person value: " + personInfo.value() + "Person isYong: " + personInfo.isYoug());
            } else {
                System.out.println("Person 没有配置了PersonInfo注解");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


