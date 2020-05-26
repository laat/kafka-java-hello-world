package com.example.consumer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import com.example.shared.Person;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;

public class ExampleConsumer {

    public static void main(final String[] args) throws IOException {
        final Properties props = loadConfig();
        final String topic = checkNotNull(props.getProperty("consumer.topic"),
                "consumer.topic must be defined in config.properties");
        final Consumer<String, Person> consumer = new KafkaConsumer<String, Person>(props);
        consumer.subscribe(Arrays.asList(topic));
        System.out.println("Consumer started, subscribed to topic");
        try {
            while (true) {
                final ConsumerRecords<String, Person> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, Person> consumerRecord : records) {
                    String key = consumerRecord.key();
                    Person value = consumerRecord.value();
                    System.out.printf("got key: [%s] value: [%s]%n", key, value);
                }
            }
        } finally {
            consumer.close();
        }
    }

    private static Properties loadConfig() throws IOException {
        final Properties props = new Properties();
        try (InputStream inputStream = ExampleConsumer.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            props.load(inputStream);
        }
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "io.confluent.kafka.serializers.KafkaJsonDeserializer");
        props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, Person.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer-1");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}