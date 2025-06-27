package br.com.cotiinformatica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// http://localhost:8081/swagger-ui/index.html pode ser executada pela API no dockerou pelo eclipse
// botão direito, executar como springbootapp chamo a API pelo eclipse
// Coloca as informações pelo endereço no swagger pelo try it out

@SpringBootApplication
public class ApiPedidosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiPedidosApplication.class, args);
	}

}
