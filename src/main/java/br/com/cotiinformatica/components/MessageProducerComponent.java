package br.com.cotiinformatica.components;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.domain.entities.Pedido;

// Objetivo, enviar dados para a mensageria

@Component
public class MessageProducerComponent {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private Queue queue;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public void send(Pedido pedido) {
		try {
			var json = objectMapper.writeValueAsString(pedido);  // transofrma em json
			rabbitTemplate.convertAndSend(queue.getName(), json); // manda o nome da fila e o json
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
