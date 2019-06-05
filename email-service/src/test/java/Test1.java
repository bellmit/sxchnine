import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class Test1 {

    @Anno
    private Test2 test2;

    public Test1(){
        try {
            setField(Test1.class, Test2.class);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void test() throws IllegalAccessException, InstantiationException {
        test2.sayHello();
    }

    public <T> void setField(Class<?> clazz, Class<?> tClass) throws IllegalAccessException, InstantiationException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Anno.class))
                field.setAccessible(true);
                field.set(this, field.getType().newInstance());

        }
    }


    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Anno {

    }

    public class Test2{

        public void sayHello(){
            System.out.println("hello ");
        }
    }
}
