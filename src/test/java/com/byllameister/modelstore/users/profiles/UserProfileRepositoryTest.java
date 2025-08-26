package com.byllameister.modelstore.users.profiles;
import com.byllameister.modelstore.users.User;
import com.byllameister.modelstore.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserId_shouldReturnUserProfile() {
        User user = new User();
        user.setUsername("Test User");
        userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setName("Test User");
        userProfileRepository.save(profile);

        Optional<UserProfile> result = userProfileRepository.findByUserId(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test User");
    }

    @Test
    void findByUserId_shouldReturnEmptyIfNotFound() {
        Optional<UserProfile> result = userProfileRepository.findByUserId(999L);

        assertThat(result).isEmpty();
    }
}
