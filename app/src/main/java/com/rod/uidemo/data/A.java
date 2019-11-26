package com.rod.uidemo.data;

import java.util.Date;
import java.util.Objects;

/**
 * @author Rod
 * @date 2019/11/21
 */
public class A {
    int a;
    Date b;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        A a1 = (A) o;
        return a == a1.a &&
                Objects.equals(b, a1.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
