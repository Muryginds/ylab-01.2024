package io.ylab.backend.security;

import io.ylab.backend.exception.UserNotFoundException;
import io.ylab.backend.mapper.UserMapper;
import io.ylab.backend.repository.UserRepository;
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
