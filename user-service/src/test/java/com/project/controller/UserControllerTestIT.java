package com.project.controller;

import com.project.config.TestRedisConfiguration;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestRedisConfiguration.class)
@ActiveProfiles("test")
@EmbeddedKafka
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTestIT {

    private static final String EMAIL_TEST = "toto@gmail.com";
    private static final String EMAIL_TEST_2 = "toto2@gmail.com";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);


    @AfterEach
    public void teardown() throws Exception {
        userRepository.deleteUserByEmail(EMAIL_TEST).block();
        userRepository.deleteUserByEmail(EMAIL_TEST_2).block();
    }

    @Order(1)
    @Test
    public void testCreateUser(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail(EMAIL_TEST);

        webTestClient.post()
                .uri("/save?isNew=true")
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        User savedUser = userService.getUserByEmail(user.getEmail().toLowerCase()).block();

        ReceiverOptions<Object, Object> kafkaConsumer = createKafkaConsumer().subscription(Collections.singleton("users"));

        ReceiverRecord<Object, Object> record = KafkaReceiver.create(kafkaConsumer).receive().blockFirst();

        assertNotNull(record.value());
        assertThat((String)record.value()).contains(user.getEmail().toLowerCase());

        assertThat(savedUser.getEmail().toLowerCase()).isEqualTo(user.getEmail().toLowerCase());

        userRepository.deleteUserByEmail(EMAIL_TEST).block();
    }

    @Order(2)
    @Test
    public void testUpdateUser() {
        String test = "toto3@gmail.com";
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail("Toto3@gmail.com");

        userService.save(user, true).block();

        webTestClient.post()
                .uri("/save")
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        User savedUser = userService.getUserByEmail(test).block();

        assertThat(savedUser).isEqualToIgnoringGivenFields(user, "password");

        ReceiverOptions<Object, Object> kafkaConsumer = createKafkaConsumer().subscription(Collections.singleton("users"));

        ReceiverRecord<Object, Object> record = KafkaReceiver.create(kafkaConsumer).receive().blockFirst();
        assertNotNull(record.value());

        userRepository.deleteUserByEmail(test);
    }

    @Order(3)
    @Test
    public void testDeleteByUser() {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail(EMAIL_TEST);

        userRepository.save(user).block();

        webTestClient.delete()
                .uri("/deleteByEmail/"+EMAIL_TEST)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        User savedUser = userService.getUserByEmail(EMAIL_TEST).block();

        assertThat(savedUser).isNull();
    }

    @Order(4)
    @Test
    public void testLogin() {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail(EMAIL_TEST);
        user.setPassword("toto1");

        userService.save(user, true).block();

        webTestClient.post()
                .uri("/login?email=" + user.getEmail() + "&password=toto1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .value(response -> assertThat(response.getId()).isEqualTo(user.getId()));

        ReceiverOptions<Object, Object> kafkaConsumer = createKafkaConsumer().subscription(Collections.singleton("users"));

        ReceiverRecord<Object, Object> record = KafkaReceiver.create(kafkaConsumer).receive().blockFirst();
        assertNotNull(record.value());

        userRepository.deleteUserByEmail(EMAIL_TEST).block();
    }

    @Order(5)
    @Test
    public void testGetUserByEmail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        userRepository.save(user).block();

        webTestClient.get()
                .uri("/email/" + user.getEmail())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .value(savedUser -> assertThat(savedUser).usingRecursiveComparison().isEqualTo(user));

        userRepository.deleteUserByEmail(user.getEmail()).block();
    }

    private ReceiverOptions<Object, Object> createKafkaConsumer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("spring.embedded.kafka.brokers"));
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "user-consumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "user-group");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return ReceiverOptions.create(props);
    }
}
