package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;


    @Test
    void userToDto_ShouldMapAllFields() {
    
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");

        UserDto userDto = userMapper.toDto(user);

        assertAll(() -> {
            assertThat(userDto).isNotNull();
            assertThat(userDto.getId()).isEqualTo(user.getId());
            assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
            assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
            assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
            assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
        });
    }

    @Test
    void userToEntity_ShouldMapAllFields() {
    
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPassword("password123");

        User user = userMapper.toEntity(userDto);

        assertAll(() -> {
            assertThat(user).isNotNull();
            assertThat(user.getId()).isEqualTo(userDto.getId());
            assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
            assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
            assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
            assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        });
    }
} 