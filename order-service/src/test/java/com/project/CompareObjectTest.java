package com.project;

import com.project.model.Order;
import com.project.model.OrderId;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CompareObjectTest {

    @Test
    public void testCompare() throws IllegalAccessException {
        compareO1ToO2(Order.class, OrderId.class);
        //compareO2ToO1(Order.class, OrderId.class);
    }

    public void compareO1ToO2(Class<?> o1, Class<?> o2) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();

        Field[] fields = o1.getDeclaredFields();
        Field[] fields2 = o2.getDeclaredFields();
        for (int i=0; i <= o1.getDeclaredFields().length; i++){
            if (!fields[i].getName().equals(fields2[i].getName())
                    && (!fields[i].getName().equals("serialVersionUID")
                    && !fields2[i].getName().equals("serialVersionUID"))){

                map.put(fields[i].getName(), fields2[i].getDeclaringClass().getName());

                if ((!fields[i].getType().isPrimitive() && !fields2[i].getType().isPrimitive())
                        && (!fields[i].getType().isInstance(String.class) || !fields2[i].getType().isInstance(String.class))
                        || (!fields[i].getType().isInstance(UUID.class) || !fields2[i].getType().isInstance(UUID.class))){

                    compareO1ToO2(fields[i].getType(), fields2[i].getType());
                }
            }


        }

        map.forEach((key, value) -> System.out.println("missing field: " + key + " in class " + value));
    }

    public void compareO2ToO1(Class<?> o1, Class<?> o2) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();

        Field[] fields = o1.getDeclaredFields();
        Field[] fields2 = o2.getDeclaredFields();
        for (int i=0; i < o2.getDeclaredFields().length; i++){
            if (!fields2[i].getName().equals(fields[i].getName())){
                map.put(fields2[i].getName(), fields[i].getDeclaringClass().getName());
            }
        }

        map.forEach((key, value) -> System.out.println("missing field: " + key + " in class " + value));
    }
}
