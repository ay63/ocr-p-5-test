package com.openclassrooms.starterjwt;

import java.util.Date;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

public class MockFactory {

    //Session
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


    public Session createSession(String name, String description) {
        Session session = new Session();
        session.setId(1L);
        session.setName(name != null ? name : NAME_SESSION);
        session.setDescription(description != null ? description : DESCIP_SESSION);
        session.setDate(new Date());
        return session;
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

    public Teacher createTeacher(String firstName, String lastName) {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName(firstName != null ? firstName : TEACHER_FIRST_NAME);
        teacher.setLastName(lastName != null ? lastName : TEACHER_LAST_NAME);
        return teacher;
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
