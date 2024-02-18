package io.ylab.security;

import io.ylab.exception.UserNotFoundException;
import io.ylab.mapper.UserMapper;
import io.ylab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var userModel = userRepository.findUserByName(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return userMapper.toUser(userModel);
    }
}
