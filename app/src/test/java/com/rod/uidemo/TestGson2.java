package com.rod.uidemo;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rod
 * @date 2019/7/5
 */
public class TestGson2 {

    @Test
    public void testParseToJson() {
        List<TestGson.Fake> list = new ArrayList<TestGson.Fake>();
        for (int i = 1; i < 9; i++) {
            list.add(new TestGson.Fake("Name_" + i, i));
        }

        Gson gson = new Gson();
        String jsonStr = gson.toJson(list);
        System.out.println(jsonStr);

        List<TestGson.Fake> list2 = fromJson(jsonStr, new ArrayList<TestGson.Fake>());
        list2.forEach(System.out::println);
    }

    private <T> T fromJson(String jsonStr, T defValue) {
        return (T) new Gson().fromJson(jsonStr, new TypeToken<ArrayList<TestGson.Fake>>(){}.getType());
    }

    private Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type type = parameterized.getRawType();
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}
