package org.lostfan.ktv;

/**
 * Created by Ihar_Niakhlebau on 09-Nov-15.
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class App {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        String className = "java.util.ArrayList";
        String addMethodName = "add";
        String removeMethodName = "remove";

        Class<?> objClass = Class.forName(className);
        Object list = objClass.newInstance();

        Method addMethod = objClass.getMethod(addMethodName, Object.class);
        Method removeMethod = objClass.getMethod(removeMethodName, Object.class);

        addMethod.invoke(list, 5);
        addMethod.invoke(list, 8);
        addMethod.invoke(list, 7);

        removeMethod.invoke(list, 5);

        System.out.println(list);

    }
}
