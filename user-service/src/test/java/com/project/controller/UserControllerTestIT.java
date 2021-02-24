package com.project.controller;

import com.project.config.TestRedisConfiguration;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.service.UserService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
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
public class UserControllerTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);

    @Test
    public void testGetUserByEmail() {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        userRepository.save(user).block();

        webTestClient.get()
                .uri("/email/" + user.getEmail())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .value(savedUser -> assertThat(savedUser).usingRecursiveComparison().isEqualTo(user));
    }

    @Test
    public void testUpdateUser() {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        webTestClient.post()
                .uri("/save")
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        User savedUser = userService.getUserByEmail(user.getEmail()).block();

        assertThat(savedUser).isEqualToIgnoringGivenFields(user, "password");
    }

    @Test
    public void testCreateUser() {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        webTestClient.post()
                .uri("/save?true")
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        User savedUser = userService.getUserByEmail(user.getEmail()).block();

        ReceiverOptions<Object, Object> kafkaConsumer = createKafkaConsumer().subscription(Collections.singleton("users"));

        ReceiverRecord<Object, Object> record = KafkaReceiver.create(kafkaConsumer).receive().blockFirst();

        assertNotNull(record.value());

        assertThat(savedUser).isEqualToIgnoringGivenFields(user, "password");
    }

    @Test
    public void testDeleteByUser() {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail("toto@gmail.com");

        userRepository.save(user).block();

        webTestClient.delete()
                .uri("/deleteByEmail/toto@gmail.com")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        User savedUser = userService.getUserByEmail("toto@gmail.com").block();

        assertThat(savedUser).isNull();
    }

    @Test
    public void testLogin() {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail("toto@gmail.com");
        user.setPassword("toto1");

        userRepository.save(user).block();

        webTestClient.post()
                .uri("/login?email=" + user.getEmail() + "&password=toto1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .value(response -> assertThat(response.getId()).isEqualTo(user.getId()));
    }

    private ReceiverOptions<Object, Object> createKafkaConsumer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("spring.embedded.kafka.brokers"));
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "user-consumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "suser-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return ReceiverOptions.create(props);
    }
}
