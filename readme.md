# Kafka hello world with Java

based on:

- https://docs.confluent.io/4.0.0/cloud-quickstart.html
- https://github.com/confluentinc/examples/tree/latest/clients/cloud/java

Steps:

1. `ccloud kafka topic create example-topic`
2. create Producer/src/main/resources/config.properties
3. create Consumer/src/main/resources/config.properties
4. `./gradlew :Consumer:run`
5. `./gradlew :Producer:run`