package com.xxxx.provider.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue Msgqueue(){
        return new Queue("msg");
    }
    @Bean
    public Queue Bookqueue(){
        return new Queue("book"){};
    }
    @Bean
    public Queue Personqueue(){
        return new Queue("person"){};
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Binding bindingBook(){
        return BindingBuilder.bind(Bookqueue()).to(topicExchange()).with("book.msg");
    }
    @Bean
    public Binding bindingPerson(){
        return BindingBuilder.bind(Personqueue()).to(topicExchange()).with("person.msg");
    }
    @Bean
    public Binding bindingMsg(){
        return BindingBuilder.bind(Msgqueue()).to(topicExchange()).with("msg.msg");
    }
}
