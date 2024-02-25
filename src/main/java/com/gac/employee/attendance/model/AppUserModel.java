package com.gac.employee.attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;

@Setter
@Getter
@Entity
@Table(name = "app_user")
public class AppUserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appUserid;
    private String userName;
    private String password;
    @Convert(converter = NumericBooleanConverter.class)
    private boolean userStatus;
    private String roles;

}
