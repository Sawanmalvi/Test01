package com.ums_crm.userManagement.service;

import com.ums_crm.userManagement.dto.LoginResponse;
import com.ums_crm.userManagement.entity.User;

public interface AuthServiceInterface {
    String encodePassword(String password);

    User registerUser(User user);

    LoginResponse login(String username, String password);
//    public String generateToken(User user);
    public boolean validateToken(String token);
}
