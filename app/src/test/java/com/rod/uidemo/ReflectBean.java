package com.rod.uidemo;

/**
 * @author Rod
 * @date 2019/3/20
 */
public class ReflectBean {

    private String mName;
    private int mAge;

    @TestAnno
    public ReflectBean() {

    }

    private ReflectBean(int age) {
        mAge = age;
    }

    public ReflectBean(String name) throws IllegalArgumentException {
        mName = name;
    }

    protected ReflectBean(@TestAnno2 @TestAnno String name, int age) {
        mName = name;
        mAge = age;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }

    private String getInfo() {
        return mName + ", " + mAge;
    }

    private static void hehe() {
        System.out.println("you call hehe");
    }

    @Override
    public String toString() {
        return "ReflectBean{" +
                "mName='" + mName + '\'' +
                ", mAge=" + mAge +
                '}';
    }
}
