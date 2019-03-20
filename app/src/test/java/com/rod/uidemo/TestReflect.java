package com.rod.uidemo;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Rod
 * @date 2019/3/20
 */
public class TestReflect {

    @Test
    public void testClassInfo() throws ClassNotFoundException {
        Class cls = Class.forName("com.rod.uidemo.ReflectBean");
        Constructor<?>[] allConstructors = cls.getDeclaredConstructors();

        printConstructors(allConstructors);
        printFields(cls.getDeclaredFields());
        printMethods(cls.getDeclaredMethods());
    }

    @Test
    public void testInvokeMethod() throws ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        Class cls = Class.forName("com.rod.uidemo.ReflectBean");
        Constructor constructor = cls.getDeclaredConstructor(int.class);
        constructor.setAccessible(true);
        println("init by age" + constructor.newInstance(23));
        Constructor nameConstructor = cls.getDeclaredConstructor(String.class);
        nameConstructor.setAccessible(true);
        println("init by name" + nameConstructor.newInstance("Rod"));
        Constructor defConstructor = cls.getDeclaredConstructor();
        Object defaultConstructorObj = defConstructor.newInstance();
        println("init by default" + defaultConstructorObj);
        Constructor fullConstructor = cls.getDeclaredConstructor(String.class, int.class);
        fullConstructor.setAccessible(true);
        println("init by fullConstructor" + fullConstructor.newInstance("Rod", 28));

        Method method = cls.getDeclaredMethod("setAge", int.class);
        method.invoke(defaultConstructorObj, 18);
        cls.getDeclaredMethod("setName", String.class).invoke(defaultConstructorObj, "Rod");

        Method hehe = cls.getDeclaredMethod("hehe");
        hehe.setAccessible(true);
        hehe.invoke(null);

        Method getInfo = cls.getDeclaredMethod("getInfo");
        getInfo.setAccessible(true);
        println((String) getInfo.invoke(defaultConstructorObj));
    }

    private void printFields(Field[] declaredFields) {
        println("--------printFields start--------");
        for (Field field : declaredFields) {
            printAnnotations(field.getDeclaredAnnotations());
            println(getFieldInfo(field));
        }
        println("--------printFields end--------\n");
    }

    private String getFieldInfo(Field field) {
        String mod = getModifierStr(field.getModifiers());
        return mod
                + field.getType().getName()
                + " "
                + field.getName();
    }

    private void printMethods(Method[] declaredMethods) {
        println("--------printMethods start--------");
        for (Method method : declaredMethods) {
            printAnnotations(method.getDeclaredAnnotations());
            println(getMethodInfo(method));
        }
        println("--------printMethods end--------\n");
    }

    private String getMethodInfo(Method method) {
        StringBuilder sb = new StringBuilder();
        String modifierStr = getModifierStr(method.getModifiers());
        return sb.append(modifierStr)
                .append(method.getReturnType().getName()).append(" ")
                .append(method.getName())
                .append("(")
                .append(getParametersInfo(method.getParameterAnnotations(), method.getParameterTypes()))
                .append(")")
                .toString();
    }

    private void printConstructors(Constructor<?>[] allConstructors) {
        println("--------print getDeclaredConstructors start--------");
        for (Constructor<?> item : allConstructors) {
            printAnnotations(item.getDeclaredAnnotations());
            println(getConstructorInfo(item));
        }
        println("--------print getDeclaredConstructors end--------\n");
    }

    private void printAnnotations(Annotation[] annotations) {
        for (Annotation item : annotations) {
            println(item.toString());
        }
    }

    private String getConstructorInfo(Constructor constructor) {
        String modifier = getModifierStr(constructor.getModifiers());
        String params = getParametersInfo(constructor.getParameterAnnotations(), constructor.getParameterTypes());
        return modifier
                + constructor.getName()
                + "("
                + params
                + ")";

    }

    private String getParametersInfo(Annotation[][] parameterAnnotations, Class[] parameterTypes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterAnnotations[i] != null && parameterAnnotations[i].length > 0) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    sb.append(annotation.toString()).append(" ");
                }
            }
            sb.append(parameterTypes[i].getName()).append(", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length() - 1);
        }
        return sb.toString();
    }

    private String getModifierStr(int mod) {
        String modifier = Modifier.toString(mod);
        modifier = "".equals(modifier) ? "" : modifier + " ";
        return modifier;
    }

    private void print(String msg) {
        System.out.print(msg);
    }

    private void println(String msg) {
        System.out.println(msg);
    }
}
