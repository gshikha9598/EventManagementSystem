//package org.example.configuration;
//
//import com.fasterxml.jackson.databind.ser.std.StringSerializer;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.inject.Produces;
//import jakarta.inject.Named;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//
//import java.util.Properties;
//
//@ApplicationScoped
//public class KafkaConfig {
//    @ConfigProperty(name = "kafka_server")
//    private String kafkaServer;
//
//    @Named("kafkaProducer")
//    @Produces
//    public KafkaProducer<String, String> getKafkaProducer() {
//        // Set properties for the Kafka Producer
//        Properties properties = new Properties();
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//
//        return new KafkaProducer<>(properties);
//    }
//}
