/*
 * Copyright(C) 2011-2012 Alibaba Group Holding Limited
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *
 *  Authors:
 *    wentong <wentong@taobao.com>
 */

package com.taobao.tdhs.jdbc.test.mybatis.vo;

import org.junit.Ignore;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-23 上午10:58
 */
@Ignore
public class Person extends BaseVO {

    private String name;

    private int age;

    private String mm;

    public Person() {
    }

    public Person(long id, String name, int age, String mm) {
        setId(id);
        this.name = name;
        this.age = age;
        this.mm = mm;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (age != person.age) return false;
        if (id != person.id) return false;
        if (mm != null ? !mm.equals(person.mm) : person.mm != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (mm != null ? mm.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", mm='" + mm + '\'' +
                '}';
    }
}
