package com.project.repository;

import com.project.config.TestRedisConfiguration;
import com.project.model.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestRedisConfiguration.class)
@ActiveProfiles("test")
@EmbeddedKafka
public class UserRepositoryTestIT {

    private static final String EMAIL_TEST = "toto@gmail.com";

    @Autowired
    private UserRepository userRepository;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @BeforeEach
    public void teardown(){
        userRepository.deleteUserByEmail(EMAIL_TEST);
    }
    @Test
    public void testFindByEmail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        User savedUser = userRepository.save(user)
                .then(userRepository.findByEmail(user.getEmail()))
                .block();

        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void testDeleteById(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setId("TOTO");
        user.setEmail(EMAIL_TEST);

        User deletedUser = userRepository.save(user)
                .then(userRepository.deleteUserByEmail(EMAIL_TEST))
                .then(userRepository.findByEmail("toto@gmail.com"))
                .block();

        assertThat(deletedUser).isNull();
    }


}
