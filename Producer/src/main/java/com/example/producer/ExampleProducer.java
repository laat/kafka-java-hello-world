package com.example.producer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.example.shared.Person;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;


public class ExampleProducer {
    public static void main(final String[] args) throws IOException {
        System.out.println("Hello World");
        final Properties props = loadConfig();
        String topic = checkNotNull(props.getProperty("producer.topic"),
                "producer.topic must be defined in config.properties");
        final Producer<String, Person> producer = new KafkaProducer<String, Person>(props);
        String id = "1";
        Person person = new Person("Sigurd", "Fosseng");
        producer.send(new ProducerRecord<String, Person>(topic, id, person), (RecordMetadata m, Exception e) -> {
            if (e != null) {
                e.printStackTrace();
            } else {
                System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n", m.topic(), m.partition(), m.offset());
            }
        });
        producer.flush();
        producer.close();
    }

    private static Properties loadConfig() throws IOException {
        final Properties props = new Properties();
        try (InputStream inputStream = ExampleProducer.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(inputStream);
        }
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");
        return props;
    }
}