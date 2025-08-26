package com.byllameister.modelstore.users;

import static org.assertj.core.api.Assertions.assertThat;

import com.byllameister.modelstore.users.profiles.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByUsername_shouldReturnTrueWhenUserExists() {
        createUser("john", "john@example.com");

        boolean exists = userRepository.existsByUsername("john");

        assertThat(exists).isTrue();
    }

    private User createUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password123");
        user.setRole(Role.BUYER);
        user.setVerified(true);

        UserProfile profile = new UserProfile();
        profile.setName("John Doe");
        profile.setAvatarUrl("http://avatar.com/john.png");
        profile.setBio("Test bio");
        profile.setUser(user);

        user.setProfile(profile);
        return userRepository.save(user);
    }

    @Test
    void existsByEmail_shouldReturnTrueWhenEmailExists() {
        createUser("anna", "anna@example.com");

        boolean exists = userRepository.existsByEmail("anna@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void findByEmail_shouldReturnUserWithProfile() {
        createUser("mike", "mike@example.com");

        Optional<User> userOpt = userRepository.findByEmail("mike@example.com");

        assertThat(userOpt).isPresent();
        assertThat(userOpt.get().getProfile()).isNotNull();
        assertThat(userOpt.get().getProfile().getName()).isEqualTo("John Doe");
    }

    @Test
    void findAll_shouldReturnUsersWithProfiles() {
        createUser("alice", "alice@example.com");
        createUser("bob", "bob@example.com");

        Page<User> users = userRepository.findAll(PageRequest.of(0, 10));

        assertThat(users.getTotalElements()).isEqualTo(2);
        assertThat(users.getContent().get(0).getProfile()).isNotNull();
    }

    @Test
    void findById_shouldReturnUserWithProfile() {
        User saved = createUser("kate", "kate@example.com");

        Optional<User> found = userRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getProfile()).isNotNull();
        assertThat(found.get().getEmail()).isEqualTo("kate@example.com");
    }
}
