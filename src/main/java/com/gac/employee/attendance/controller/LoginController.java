package com.gac.employee.attendance.controller;

import com.gac.employee.attendance.dto.UserDTO;
import com.gac.employee.attendance.model.AppUserModel;
import com.gac.employee.attendance.service.UserService;
import com.zkteco.iclockhelper.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("appName", "My Employee Attendance App");
        return "login";
    }

    @GetMapping("/addUser")
    public String showRegister(Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("userDto", userDto);
        return "addAdmin";
    }

    @PostMapping("/addUser")
    public String addUser(UserDTO userDTO) {
        userService.addUser(userDTO);
        return "Redirect:/dashboard";
    }
}
