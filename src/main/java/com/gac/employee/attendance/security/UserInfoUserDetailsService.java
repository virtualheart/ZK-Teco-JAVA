package com.gac.employee.attendance.security;

import com.gac.employee.attendance.model.AppUserModel;
import com.gac.employee.attendance.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<AppUserModel> userInfo = userRepository.findByUserName(userName);
        return userInfo.map(UserInfoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + userName));

    }

    public void addUser(AppUserModel userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userRepository.save(userInfo);
    }

}