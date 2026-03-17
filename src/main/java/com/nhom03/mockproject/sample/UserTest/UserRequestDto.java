package com.nhom03.mockproject.sample.UserTest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String name;
    private int age;

    @Override
    public String toString() {
        return "UserRequestDto [name=" + name + ", age=" + age + "]";
    }

}
