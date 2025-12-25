package com.ebank.service;

import com.ebank.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getCurrentUser();

    User updateUser(User user);

    User findByEmail(String email);
}
