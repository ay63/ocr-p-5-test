package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MockFactory mockFactory;

    User user;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        user = mockFactory.createUser(false);
        userDto = userMapper.toDto(user);
    }

    @Test
    void userToDto_ShouldMapAllFields() {

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
    void usersToDto_ShouldReturnListOfUserDto() {
        User user2 = mockFactory.createUser(false);
        user2.setId(2L);

        List<User> users = Arrays.asList(user, user2);

        List<UserDto> usersDto = userMapper.toDto(users);

        assertThat(usersDto).isNotNull();
    }

    @Test
    void userToEntity_ShouldMapAllFields() {

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

      @Test
    void usersToEntity_ShouldReturnListOfUser() {

        UserDto userDto2 = mockFactory.createUserDto(false);
        userDto2.setId(2L);

        List<UserDto> usersDto = Arrays.asList(userDto, userDto2);

        List<User> users = userMapper.toEntity(usersDto);

        assertThat(users).isNotNull();
    }

}