package com.openclassrooms.starterjwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.starterjwt.models.User;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setPassword("password123");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
 
        Optional<User> found = userRepository.findByEmail(testUser.getEmail());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(testUser.getEmail());
        assertThat(found.get().getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(found.get().getLastName()).isEqualTo(testUser.getLastName());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenUserDoesNotExist() {
        assertThat(userRepository.findByEmail("nonexistent@test.com")).isEmpty();
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenUserExists() {
        assertThat( userRepository.existsByEmail(testUser.getEmail())).isTrue();
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenUserDoesNotExist() {

        assertThat(userRepository.existsByEmail("nonexistent@test.com")).isFalse();
    }
} 