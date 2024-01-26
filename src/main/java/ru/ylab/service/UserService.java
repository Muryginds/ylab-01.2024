package ru.ylab.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.ylab.entity.User;
import ru.ylab.exception.UserAlreadyExistException;
import ru.ylab.exception.UserAuthenticationException;
import ru.ylab.in.dto.request.UserAuthenticationRequestDTO;
import ru.ylab.in.dto.request.UserRegistrationRequestDTO;
import ru.ylab.in.dto.UserDTO;
import ru.ylab.repository.UserRepository;
import ru.ylab.security.PasswordEncoder;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Getter
    private User currentUser;

    public UserDTO registerUser(UserRegistrationRequestDTO requestDTO) {
        if (!checkUsernameExists(requestDTO.name())) {
            var user = User.builder()
                    .name(requestDTO.name())
                    .password(passwordEncoder.encode(requestDTO.password()))
                    .build();
            userRepository.save(user);
            return UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build();
        }
        throw new UserAlreadyExistException(requestDTO.name());
    }

    public UserDTO authorize(UserAuthenticationRequestDTO requestDTO) {
        var user = userRepository.getUserByName(requestDTO.name())
                .orElseThrow(UserAuthenticationException::new);
        if (!passwordEncoder.verify(requestDTO.password(), user.getPassword())) {
            throw new UserAuthenticationException();
        }
        currentUser = user;
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public void logout() {
        currentUser = null;
    }

    public boolean checkUsernameExists(String username) {
        return userRepository.checkUserExistsByName(username);
    }
}
