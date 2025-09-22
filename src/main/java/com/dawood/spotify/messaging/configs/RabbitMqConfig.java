package com.dawood.spotify.messaging.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  public static final String QUEUE_NAME = "email-queue";
  public static final String RESET_PASSWORD_QUEUE = "email-queue-reset-password";
  public static final String RESET_PASSWORD_ROUTING_KEY = "routing.key.reset-password.#";

  public static final String TOPIC_EXCHANGE = "app-exchange";

  @Bean
  public Queue queue() {
    return new Queue(QUEUE_NAME, false);
  }

  @Bean
  public Queue resetPasswordQueue() {
    return new Queue(RESET_PASSWORD_QUEUE, true);
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(TOPIC_EXCHANGE);
  }

  @Bean
  public Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("routing.key.#");
  }

  @Bean
  public Binding resetPasswordBinding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(RESET_PASSWORD_ROUTING_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
      Jackson2JsonMessageConverter messageConverter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);
    return rabbitTemplate;
  }
}
