package br.com.cotiinformatica.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.cotiinformatica.domain.StatusPedido;
import br.com.cotiinformatica.domain.dtos.requests.PedidoRequest;
import br.com.cotiinformatica.domain.dtos.responses.PedidoResponse;
import br.com.cotiinformatica.domain.dtos.responses.StatusPedidoResponse;
import br.com.cotiinformatica.domain.entities.Pedido;

// Usa o ModelMapper em service

@Configuration
public class ModelMapperConfiguration {
	
	// devolve uma estancia de ModdelMapper
	@Bean
	public ModelMapper modelMapper() {
		
			var mapper = new ModelMapper();
			
			//Configurar para tratar campos com tipos diferentes, tipo datahora que é string no DTO mas localDate na Entidade e banco
			// e o integer do status pedido
			// ctx.getsource () é o valor original
			//Converter String 'yyyy-MM-dd' para LocalDateTime
			Converter<String, LocalDateTime> stringToLocalDate = ctx -> ctx.getSource() == null ? null
					: LocalDate.parse(ctx.getSource(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();

			//Typemao converte um tipo em outro
			// -> é um lambda
			// Primeiro faz as conversões e depois mapeia os campos
			//Converter Integer para StatusPedido
			//Converter Integer para StatusPedido
			Converter<Integer, StatusPedido> intToStatusPedido = ctx -> ctx.getSource() == null ? null
					: StatusPedido.fromInt(ctx.getSource());

			
			//Mapeamento para transferencia de dados da classe PedidoRequest para Pedido
			mapper.typeMap(PedidoRequest.class, Pedido.class)
				.addMappings(map -> {
					map.using(stringToLocalDate).map(PedidoRequest::getDataHora, Pedido::setDataHora);
					map.using(intToStatusPedido).map(PedidoRequest::getStatus, Pedido::setStatus);
					});
			
			//Mapeamento da classe pedido para a classe pedido response
			//Mapeamento para transferencia de dados da classe Pedido para PedidoResponse
			mapper.typeMap(Pedido.class, PedidoResponse.class)
				.addMappings(map -> {
					map.using(ctx -> {
						
						var statusPedido = (StatusPedido) ctx.getSource();
						var statusResponse = new StatusPedidoResponse();
						
						statusResponse.setId(statusPedido.getCode());
						statusResponse.setNome(statusPedido.toString());
						
						return statusResponse;
					}).map(Pedido::getStatus, PedidoResponse::setStatus);
				});

				return mapper;
	}
}
