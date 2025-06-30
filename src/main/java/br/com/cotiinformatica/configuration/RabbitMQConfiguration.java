package br.com.cotiinformatica.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
	
	/*Ler uma chave de configuração criada no application properties */
	// quero ler a queue.name
	
	@Value("${queue.name}")  // anotação para ler varivel do application properties
	private String queueName;
	
	//Configuração
	@Bean
	Queue queue() {
		//True significa fila durável, mantem salvo mesmo que o servidor seja reiniciado
		return new Queue(queueName, true);
	}

}
