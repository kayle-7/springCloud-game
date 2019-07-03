package com.springboot.rpc.api;

import com.springboot.rpc.dto.User;

public interface UserService {

    String saveUser(User user);

    User getUser(User user);

}
