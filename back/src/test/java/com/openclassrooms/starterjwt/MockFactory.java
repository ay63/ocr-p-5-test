package com.openclassrooms.starterjwt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.springframework.stereotype.Service;

@Service
public class MockFactory {

    // Session
    public static final String NAME_SESSION = "Yoga Session";
    public static final String DESCIP_SESSION = "Yoga Session description";

    // User
    public static final String EMAIL = "john.doe@studio.com";
    public static final String PASSWORD = "test!1234";
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";

    // Teacher
    public static final String TEACHER_FIRST_NAME = "Jane";
    public static final String TEACHER_LAST_NAME = "Smith";

    // Admin
    public static final String ADMIN_EMAIL = "yoga@studio.com";
    public static final String ADMIN_PASSWORD = "test!1234";
    public static final String ADMIN_FIRST_NAME = "Admin";
    public static final String ADMIN_LAST_NAME = "Admin";

    public static final String TEST_TOKEN = "Bearer validToken";

    public Session createSession() {
        Session session = new Session();
        session.setId(1L);
        session.setName(NAME_SESSION);
        session.setDescription(DESCIP_SESSION);
        session.setDate(new Date());
        session.setTeacher(createTeacher());
        session.setUsers(this.createUserList());
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        return session;
    }

    public SessionDto createSessionDto() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName(NAME_SESSION);
        sessionDto.setDescription(DESCIP_SESSION);
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        return sessionDto;
    }

    public User createUser(Boolean admin) {
        if (admin == null) {
            admin = false;
        }

        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setPassword(PASSWORD);
        user.setAdmin(admin);
        return user;
    }

    public UserDetailsImpl createUserDetails() {
        User user = createUser(false);
        return UserDetailsImpl.builder().id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .admin(user.isAdmin()).build();
    }

    public List<User> createUserList() {
        User user = this.createUser(false);
        User user2 = this.createUser(false).setId(2L);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        return users;
    }

    public UserDetailsImpl createUserDetails(User user) {
        if (user == null) {
            user = createUser(false);
        }

        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .admin(user.isAdmin()).build();
    }

    public LoginRequest createLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);
        return loginRequest;
    }

    public SignupRequest createSignupRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(EMAIL);
        signupRequest.setFirstName(FIRST_NAME);
        signupRequest.setLastName(LAST_NAME);
        signupRequest.setPassword(PASSWORD);
        return signupRequest;
    }

    public Teacher createTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
        return teacher;
    }

    public TeacherDto createTeacherDto() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName(TEACHER_FIRST_NAME);
        teacherDto.setLastName(TEACHER_LAST_NAME);
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());
        return teacherDto;
    }

    public UserDto createUserDto(Boolean admin) {
        if (admin == null) {
            admin = false;
        }

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail(EMAIL);
        userDto.setFirstName(FIRST_NAME);
        userDto.setLastName(LAST_NAME);
        userDto.setPassword(PASSWORD);
        userDto.setAdmin(admin);
        return userDto;
    }

}
