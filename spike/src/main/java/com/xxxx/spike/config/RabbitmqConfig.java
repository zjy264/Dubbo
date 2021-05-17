package com.xxxx.spike.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue Bookqueue(){
        return new Queue("spike"){};
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("spike_topicExchange");
    }

    @Bean
    public Binding bindingBook(){
        return BindingBuilder.bind(Bookqueue()).to(topicExchange()).with("spike.msg");
    }
}
