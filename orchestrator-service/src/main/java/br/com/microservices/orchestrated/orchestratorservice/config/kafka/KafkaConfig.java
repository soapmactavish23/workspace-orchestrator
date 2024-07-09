package br.com.microservices.orchestrated.orchestratorservice.config.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

import br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics;
import lombok.RequiredArgsConstructor;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

	private static final Integer PARTITION_COUNT = 1;
	private static final Integer REPLICA_COUNT = 1;

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${spring.kafka.consumer.auto-offset-reset}")
	private String autoOffsetReset;

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerProps());
	}

	private Map<String, Object> consumerProps() {
		var props = new HashMap<String, Object>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		return props;
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerProps());
	}

	private Map<String, Object> producerProps() {
		var props = new HashMap<String, Object>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}

	private NewTopic buildTopic(String name) {
		return TopicBuilder.name(name).replicas(REPLICA_COUNT).partitions(PARTITION_COUNT).build();
	}

	@Bean
	public NewTopic startSagaTopic() {
		return buildTopic(ETopics.START_SAGA.getTopic());
	}

	@Bean
	public NewTopic baseOrchestratorTopic() {
		return buildTopic(ETopics.BASE_ORCHESTRATOR.getTopic());
	}

	@Bean
	public NewTopic finishSuccessTopic() {
		return buildTopic(ETopics.FINISH_SUCCESS.getTopic());
	}
	
	@Bean
	public NewTopic finishFailTopic() {
		return buildTopic(ETopics.FINISH_FAIL.getTopic());
	}

	@Bean
	public NewTopic productValidationSuccessTopic() {
		return buildTopic(ETopics.PRODUCT_VALIDATION_SUCCESS.getTopic());
	}

	@Bean
	public NewTopic productValidationFailTopic() {
		return buildTopic(ETopics.PRODUCT_VALIDATION_FAIL.getTopic());
	}

	@Bean
	public NewTopic paymentSuccessTopic() {
		return buildTopic(ETopics.PAYMENT_SUCCESS.getTopic());
	}

	@Bean
	public NewTopic paymentFailTopic() {
		return buildTopic(ETopics.PAYMENT_FAIL.getTopic());
	}

	@Bean
	public NewTopic inventorySuccessTopic() {
		return buildTopic(ETopics.INVENTORY_SUCCESS.getTopic());
	}

	@Bean
	public NewTopic inventoryFailTopic() {
		return buildTopic(ETopics.INVENTORY_FAIL.getTopic());
	}

	@Bean
	public NewTopic notifyEndingTopic() {
		return buildTopic(ETopics.NOTIFY_ENDING.getTopic());
	}

}