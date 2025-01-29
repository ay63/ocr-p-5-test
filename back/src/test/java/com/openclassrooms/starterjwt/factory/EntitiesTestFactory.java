package com.openclassrooms.starterjwt.factory;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class EntitiesTestFactory {

    private final String testEmail;
    private final String testPassword;
    private final String testFirstName;
    private final String testLastName;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EntitiesTestFactory(
            @Value("${app.test.email}") String testEmail,
            @Value("${app.test.password}") String testPassword,
            @Value("${app.test.firstname}") String testFirstName,
            @Value("${app.test.lastname}") String testLastName,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.testEmail = testEmail;
        this.testPassword = testPassword;
        this.testFirstName = testFirstName;
        this.testLastName = testLastName;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(boolean isAdmin) {
        return new User(testEmail, testLastName, testFirstName,
                passwordEncoder.encode(testPassword), isAdmin);
    }

    public void createAndSaveUser(boolean isAdmin) {
        User user = this.createUser(isAdmin);
        userRepository.save(user);
    }

    public Session createSession() {
        Teacher teacher = this.createTeacher();

        Session session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDate(new Date());
        session.setDescription("A relaxing yoga session");
        session.setTeacher(teacher);

        return session;
    }

    public Teacher createTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        return teacher;
    }

}