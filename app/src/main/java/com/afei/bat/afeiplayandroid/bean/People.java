package com.afei.bat.afeiplayandroid.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by MrLiu on 2018/2/26.
 *
 * @Entity：将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类;
 * @nameInDb：在数据库中的名字，如不写则为实体中类名；
 * @Id：选择一个long / Long属性作为实体ID。 在数据库方面，它是主键。 参数autoincrement是设置ID值自增；
 * @NotNull：使该属性在数据库端成为“NOT NULL”列。 通常使用@NotNull标记原始类型（long，int，short，byte）是有意义的；
 * @Transient：表明这个字段不会被写入数据库，只是作为一个普通的java类字段，用来临时存储数据的，不会被持久化。
 */
@Entity
public class People {
    @Id(autoincrement = true)
    private long id;
    private String age;
    @NotNull
    private String gender;
    @Generated(hash = 1016081183)
    public People(long id, String age, @NotNull String gender) {
        this.id = id;
        this.age = age;
        this.gender = gender;
    }
    @Generated(hash = 1406030881)
    public People() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getAge() {
        return this.age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "People{" +
                "id=" + id +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
