package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.User;
import ru.ylab.exception.UserAlreadyExistException;
import ru.ylab.exception.UserAuthenticationException;
import ru.ylab.dto.UserAuthenticationRequestDTO;
import ru.ylab.dto.UserRegistrationRequestDTO;
import ru.ylab.repository.UserRepository;
import ru.ylab.security.PasswordEncoder;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Getter
    private User currentUser;

    public User registerUser(UserRegistrationRequestDTO requestDTO) {
        if (!checkUsernameExists(requestDTO.name())) {
            User user = User.builder()
                    .name(requestDTO.name())
                    .password(passwordEncoder.encode(requestDTO.password()))
                    .build();
            userRepository.save(user);
            return user;
        }
        throw new UserAlreadyExistException(requestDTO.name());
    }

    public User authenticate(UserAuthenticationRequestDTO requestDTO) {
        var user = userRepository.getUserByName(requestDTO.name())
                .orElseThrow(UserAuthenticationException::new);
        if (!passwordEncoder.verify(requestDTO.password(), user.getPassword())) {
            throw new UserAuthenticationException();
        }
        return user;
    }

    public boolean checkUsernameExists(String username) {
        return userRepository.checkUserExistsByName(username);
    }
}
