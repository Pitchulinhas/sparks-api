package com.sparks.api.producers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyTypedMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.sparks.api.entities.User;
import com.sparks.api.responses.ServiceResponse;

@Component
public class UserProducer {
	@Value("${spring.kafka.topics.user.create}")
	private String createUserTopic;

	@Value("${spring.kafka.reply-topics.user.create}")
	private String createUserReplyTopic;

	@Value("${spring.kafka.topics.user.find-all}")
	private String findAllUsersTopic;

	@Value("${spring.kafka.reply-topics.user.find-all}")
	private String findAllUsersReplyTopic;

	@Value("${spring.kafka.topics.user.find-by-id}")
	private String findUserByIdTopic;

	@Value("${spring.kafka.reply-topics.user.find-by-id}")
	private String findUserByIdReplyTopic;

	@Value("${spring.kafka.topics.user.update-by-id}")
	private String updateUserByIdTopic;

	@Value("${spring.kafka.reply-topics.user.update-by-id}")
	private String updateUserByIdReplyTopic;

	@Value("${spring.kafka.topics.user.delete-by-id}")
	private String deleteUserByIdTopic;

	@Value("${spring.kafka.reply-topics.user.delete-by-id}")
	private String deleteUserByIdReplyTopic;

	@Autowired
	private ReplyingKafkaTemplate<String, String, String> kafkaTemplate;

	private Gson gson;

	public UserProducer() {
		this.gson = new Gson();
	}

	public ServiceResponse<User> createUser(User createUserInput) throws Exception {
		Message<String> message = MessageBuilder.withPayload(this.gson.toJson(createUserInput))
				.setHeader(KafkaHeaders.TOPIC, createUserTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, createUserReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<User>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<User>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<User>> userFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		userFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<User>> typedUser = userFut.get(30, TimeUnit.SECONDS);

		return typedUser.getPayload();
	}

	public ServiceResponse<List<User>> findAllUsers() throws Exception {
		Message<String> message = MessageBuilder.withPayload("getUsers")
				.setHeader(KafkaHeaders.TOPIC, findAllUsersTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, findAllUsersReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<List<User>>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<List<User>>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<List<User>>> usersFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		usersFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<List<User>>> typedUsers = usersFut.get(20, TimeUnit.SECONDS);

		return typedUsers.getPayload();
	}

	public ServiceResponse<User> findUserById(String id) throws Exception {
		Message<String> message = MessageBuilder.withPayload(id).setHeader(KafkaHeaders.TOPIC, findUserByIdTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, findUserByIdReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<User>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<User>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<User>> userFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		userFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<User>> typedUser = userFut.get(30, TimeUnit.SECONDS);

		return typedUser.getPayload();
	}

	public ServiceResponse<User> updateUserById(String id, User updateUserInput) throws Exception {
		updateUserInput.setId(id);

		Message<String> message = MessageBuilder.withPayload(this.gson.toJson(updateUserInput))
				.setHeader(KafkaHeaders.TOPIC, updateUserByIdTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, updateUserByIdReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<User>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<User>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<User>> userFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		userFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<User>> typedUser = userFut.get(30, TimeUnit.SECONDS);

		return typedUser.getPayload();
	}

	public ServiceResponse<User> deleteUserById(String id) throws Exception {
		Message<String> message = MessageBuilder.withPayload(id).setHeader(KafkaHeaders.TOPIC, deleteUserByIdTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, deleteUserByIdReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<User>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<User>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<User>> userFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		userFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<User>> typedUser = userFut.get(30, TimeUnit.SECONDS);

		return typedUser.getPayload();
	}
}
