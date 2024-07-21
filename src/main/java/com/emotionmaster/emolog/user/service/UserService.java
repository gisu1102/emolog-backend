package com.emotionmaster.emolog.user.service;

import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserRequestDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserResponseDto;
import com.emotionmaster.emolog.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long save(UserRequestDto dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public UserResponseDto update(Long id, UserRequestDto dto) {
        User user = findById(id);
        //builder -> toBuilder (객체 수정 )
        User updatedUser= user.toBuilder()
                .nickname(dto.getNickname())
                .age(dto.getAge())
                .build();
        userRepository.save(updatedUser);
        return new UserResponseDto(updatedUser) ;
    }


    //user 정보 넘기기
    public UserInfoResponseDto info(Long id) {
        User user = findById(id);
        return new UserInfoResponseDto(user);
    }

    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }





    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}