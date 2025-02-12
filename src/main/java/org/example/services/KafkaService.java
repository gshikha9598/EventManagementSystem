package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.example.models.Event;
import java.util.Collections;
import java.util.Properties;

@ApplicationScoped
public class KafkaService {

    @ConfigProperty(name = "kafka_server")
    private String kafkaServer;

    private static final Logger LOGGER = Logger.getLogger(KafkaService.class);

    private KafkaProducer<String, String> kafkaProducer;
    private KafkaConsumer<String, String> kafkaConsumer;

    void init(@Observes StartupEvent ev) {  // method call automatically when application start-@Observes StartupEvent(use both)
        // Producer setup
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        this.kafkaProducer = new KafkaProducer<>(producerProps);

        // Consumer setup
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start from beginning if no offset

        this.kafkaConsumer = new KafkaConsumer<>(consumerProps);
        kafkaConsumer.subscribe(Collections.singleton("event-topic")); // Ensure same topic as producer

        startConsumer();

    }

    public void publishEventMessage(Event event) {
        try {
            String eventJson = new ObjectMapper().writeValueAsString(event);
            ProducerRecord<String, String> record = new ProducerRecord<>("event-topic", eventJson);

            kafkaProducer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    LOGGER.error("Failed to publish event to Kafka", exception);
                } else {
                    LOGGER.info("Event successfully published to Kafka: " + eventJson);
                }
            });

        } catch (Exception e) {
            LOGGER.error("Error serializing event", e);
        }
    }

    void startConsumer() {
        new Thread(() -> {
            try {
                while (true) {
                    var records = kafkaConsumer.poll(java.time.Duration.ofMillis(100));
                    records.forEach(record -> LOGGER.info("Received message: " + record.value()));
                }
            } catch (Exception e) {
                LOGGER.error("Error in Kafka consumer", e);
            }
        }).start();
    }

}
