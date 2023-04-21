package com.sparks.api.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String consumerGroupId;

	@Value("${spring.kafka.reply-topics.user.create}")
	private String createUserReplyTopic;

	@Value("${spring.kafka.reply-topics.user.find-all}")
	private String findAllUsersReplyTopic;

	@Value("${spring.kafka.reply-topics.user.find-by-id}")
	private String findUserByIdReplyTopic;

	@Value("${spring.kafka.reply-topics.user.update-by-id}")
	private String updateUserByIdReplyTopic;

	@Value("${spring.kafka.reply-topics.user.delete-by-id}")
	private String deleteUserByIdReplyTopic;

	@Bean
	Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return props;
	}

	@Bean
	Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);

		return props;
	}

	@Bean
	ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate(ProducerFactory<String, String> pf,
			KafkaMessageListenerContainer<String, String> container) {
		ReplyingKafkaTemplate<String, String, String> replyTemplate = new ReplyingKafkaTemplate<>(pf, container);

		replyTemplate.setSharedReplyTopic(true);
		replyTemplate.setMessageConverter(new StringJsonMessageConverter());
		replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(30));

		replyTemplate.start();

		return replyTemplate;
	}

	@Bean
	KafkaMessageListenerContainer<String, String> replyContainer(ConsumerFactory<String, String> cf) {
		ContainerProperties containerProperties = new ContainerProperties(createUserReplyTopic, findAllUsersReplyTopic,
				findUserByIdReplyTopic, updateUserByIdReplyTopic, deleteUserByIdReplyTopic);
		return new KafkaMessageListenerContainer<>(cf, containerProperties);
	}

	@Bean
	ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new StringDeserializer());
	}

	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setReplyTemplate(kafkaTemplate());

		return factory;
	}
}
