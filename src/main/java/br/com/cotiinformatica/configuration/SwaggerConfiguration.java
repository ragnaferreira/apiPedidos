package br.com.cotiinformatica.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

// configurar como quer que a biblioteca funcione
// Qual configuração você quer personalizar
// Identifica que é uma classe de configuração

@Configuration
public class SwaggerConfiguration {

		// Anotação do spring boot que é usada para definir algum tipo de configuração ou para preparar uma injeção de dependência ou interface
		@Bean
		OpenAPI customOpenApi() {
			var openApi = new OpenAPI().components(new Components())
					.info(new Info()
							.title("API Pedidos - Treinamento TJ/PR")
							.description("Curso Java Arquiteto - COTI Informática")
							.version("v1"));			
			return openApi;
																		
		}
		
}
