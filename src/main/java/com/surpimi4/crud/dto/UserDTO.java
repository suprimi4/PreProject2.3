package com.surpimi4.crud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Integer id;


    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть между 2 и 30 символами")
    private String name;


    @Min(value = 0, message = "Возраст должен больше 0")
    private Integer age;


    @NotEmpty(message = "Поле почты не должно быть пустым")
    @Email(message = "Почта должна быть валидной")
    private String email;

    private String password;


    public UserDTO(Integer id, String name, Integer age, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
    }


    @Override
    public String toString() {
        return "User" +
                " name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'';
    }
}

