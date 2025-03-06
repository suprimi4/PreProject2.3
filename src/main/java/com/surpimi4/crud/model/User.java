package com.surpimi4.crud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 2,max = 30, message = "Имя должно быть между 2 и 30 символами")
    private String name;

    @Column(name = "age")
    @Min(value = 0, message = "Возраст должен больше 0")
    private Integer age;

    @Column(name = "email")
    @NotEmpty(message = "Поле почты не должно быть пустым")
    @Email(message = "Почта должна быть валидной")
    private String email;

    public User(String name, Integer age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}
