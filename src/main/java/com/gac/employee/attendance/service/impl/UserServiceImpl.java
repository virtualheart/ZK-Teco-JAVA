package com.gac.employee.attendance.service.impl;

import com.gac.employee.attendance.dto.UserDTO;
import com.gac.employee.attendance.model.AppUserModel;
import com.gac.employee.attendance.security.UserInfoUserDetailsService;
import com.gac.employee.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoUserDetailsService userInfoService;
    @Override
    public void addUser(UserDTO userDTO) {
        AppUserModel model = new AppUserModel();
        model.setUserName(userDTO.getUserName());
        model.setPassword(userDTO.getPassword());
        model.setUserStatus(true);
        model.setRoles("ROLE_ADMIN");
        userInfoService.addUser(model);
    }
}
