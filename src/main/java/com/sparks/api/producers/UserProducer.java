package com.sparks.api.producers;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

	private Gson gson;
	private ReplyingKafkaTemplate<String, String, String> kafkaTemplate;
	
	public UserProducer(ReplyingKafkaTemplate<String, String, String> kafkaTemplate) {
		this.gson = new Gson();
		this.kafkaTemplate = kafkaTemplate;
	}

	public User create(User user) throws InterruptedException, ExecutionException, TimeoutException {
		String userAsString = this.gson.toJson(user);

		RequestReplyTypedMessageFuture<String, String, User> userFut = kafkaTemplate.sendAndReceive(
				MessageBuilder.withPayload(userAsString).setHeader(KafkaHeaders.TOPIC, createUserTopic)
						.setHeader(KafkaHeaders.REPLY_TOPIC, createUserReplyTopic).build(),
				new ParameterizedTypeReference<User>() {
				});

		userFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<User> typedUser = userFut.get(30, TimeUnit.SECONDS);

		User userCreated = typedUser.getPayload();

		return userCreated;
	}

	public List<User> findAll() throws InterruptedException, ExecutionException, TimeoutException {
		RequestReplyTypedMessageFuture<String, String, List<User>> usersFut = kafkaTemplate.sendAndReceive(
				MessageBuilder.withPayload("getUsers").setHeader(KafkaHeaders.TOPIC, findAllUsersTopic)
						.setHeader(KafkaHeaders.REPLY_TOPIC, findAllUsersReplyTopic).build(),
				new ParameterizedTypeReference<List<User>>() {
				});

		usersFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<List<User>> typedUsers = usersFut.get(20, TimeUnit.SECONDS);

		List<User> users = typedUsers.getPayload();

		return users;
	}

	public User findById(String id)
			throws InterruptedException, ExecutionException, TimeoutException, IllegalArgumentException {
		RequestReplyTypedMessageFuture<String, String, User> userFut = kafkaTemplate.sendAndReceive(
				MessageBuilder.withPayload(id).setHeader(KafkaHeaders.TOPIC, findUserByIdTopic)
						.setHeader(KafkaHeaders.REPLY_TOPIC, findUserByIdReplyTopic).build(),
				new ParameterizedTypeReference<User>() {
				});

		userFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<User> typedUser = userFut.get(30, TimeUnit.SECONDS);

		User userFound = typedUser.getPayload();

		return userFound;
	}
	
	public User updateById(String id, User user) throws InterruptedException, ExecutionException, TimeoutException {
		user.setId(id);
		
		String userAsString = this.gson.toJson(user);

		RequestReplyTypedMessageFuture<String, String, User> userFut = kafkaTemplate.sendAndReceive(
				MessageBuilder.withPayload(userAsString).setHeader(KafkaHeaders.TOPIC, updateUserByIdTopic)
						.setHeader(KafkaHeaders.REPLY_TOPIC, updateUserByIdReplyTopic).build(),
				new ParameterizedTypeReference<User>() {
				});

		userFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<User> typedUser = userFut.get(30, TimeUnit.SECONDS);

		User userCreated = typedUser.getPayload();

		return userCreated;
	}
	
	public User deleteById(String id)
			throws InterruptedException, ExecutionException, TimeoutException, IllegalArgumentException {
		RequestReplyTypedMessageFuture<String, String, User> userFut = kafkaTemplate.sendAndReceive(
				MessageBuilder.withPayload(id).setHeader(KafkaHeaders.TOPIC, deleteUserByIdTopic)
						.setHeader(KafkaHeaders.REPLY_TOPIC, deleteUserByIdReplyTopic).build(),
				new ParameterizedTypeReference<User>() {
				});

		userFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<User> typedUser = userFut.get(30, TimeUnit.SECONDS);

		User userFound = typedUser.getPayload();

		return userFound;
	}
}
