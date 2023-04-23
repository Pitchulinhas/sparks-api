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
import com.sparks.api.entities.Product;
import com.sparks.api.responses.ServiceResponse;

@Component
public class ProductProducer {
	@Value("${spring.kafka.topics.product.create}")
	private String createProductTopic;

	@Value("${spring.kafka.reply-topics.product.create}")
	private String createProductReplyTopic;

	@Value("${spring.kafka.topics.product.find-all}")
	private String findAllProductsTopic;

	@Value("${spring.kafka.reply-topics.product.find-all}")
	private String findAllProductsReplyTopic;

	@Value("${spring.kafka.topics.product.find-by-id}")
	private String findProductByIdTopic;

	@Value("${spring.kafka.reply-topics.product.find-by-id}")
	private String findProductByIdReplyTopic;

	@Value("${spring.kafka.topics.product.update-by-id}")
	private String updateProductByIdTopic;

	@Value("${spring.kafka.reply-topics.product.update-by-id}")
	private String updateProductByIdReplyTopic;

	@Value("${spring.kafka.topics.product.delete-by-id}")
	private String deleteProductByIdTopic;

	@Value("${spring.kafka.reply-topics.product.delete-by-id}")
	private String deleteProductByIdReplyTopic;

	@Autowired
	private ReplyingKafkaTemplate<String, String, String> kafkaTemplate;

	private Gson gson;

	public ProductProducer() {
		this.gson = new Gson();
	}

	public ServiceResponse<Product> createProduct(Product createProductInput) throws Exception {
		Message<String> message = MessageBuilder.withPayload(this.gson.toJson(createProductInput))
				.setHeader(KafkaHeaders.TOPIC, createProductTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, createProductReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<Product>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<Product>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<Product>> productFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		productFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<Product>> typedProduct = productFut.get(30, TimeUnit.SECONDS);

		return typedProduct.getPayload();
	}

	public ServiceResponse<List<Product>> findAllProducts() throws Exception {
		Message<String> message = MessageBuilder.withPayload("getProducts")
				.setHeader(KafkaHeaders.TOPIC, findAllProductsTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, findAllProductsReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<List<Product>>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<List<Product>>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<List<Product>>> productsFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		productsFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<List<Product>>> typedProducts = productsFut.get(20, TimeUnit.SECONDS);

		return typedProducts.getPayload();
	}

	public ServiceResponse<Product> findProductById(String id) throws Exception {
		Message<String> message = MessageBuilder.withPayload(id).setHeader(KafkaHeaders.TOPIC, findProductByIdTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, findProductByIdReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<Product>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<Product>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<Product>> productFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		productFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<Product>> typedProduct = productFut.get(30, TimeUnit.SECONDS);

		return typedProduct.getPayload();
	}

	public ServiceResponse<Product> updateProductById(String id, Product updateProductInput) throws Exception {
		updateProductInput.setId(id);

		Message<String> message = MessageBuilder.withPayload(this.gson.toJson(updateProductInput))
				.setHeader(KafkaHeaders.TOPIC, updateProductByIdTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, updateProductByIdReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<Product>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<Product>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<Product>> productFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		productFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<Product>> typedProduct = productFut.get(30, TimeUnit.SECONDS);

		return typedProduct.getPayload();
	}

	public ServiceResponse<Product> deleteProductById(String id) throws Exception {
		Message<String> message = MessageBuilder.withPayload(id).setHeader(KafkaHeaders.TOPIC, deleteProductByIdTopic)
				.setHeader(KafkaHeaders.REPLY_TOPIC, deleteProductByIdReplyTopic).build();

		ParameterizedTypeReference<ServiceResponse<Product>> messageReturnType = new ParameterizedTypeReference<ServiceResponse<Product>>() {
		};

		RequestReplyTypedMessageFuture<String, String, ServiceResponse<Product>> productFut = kafkaTemplate
				.sendAndReceive(message, messageReturnType);

		productFut.getSendFuture().get(10, TimeUnit.SECONDS);

		Message<ServiceResponse<Product>> typedProduct = productFut.get(30, TimeUnit.SECONDS);

		return typedProduct.getPayload();
	}
}
