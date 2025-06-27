package br.com.cotiinformatica.domain.dtos.responses;

import lombok.Getter;
import lombok.Setter;

// vai devolver a descrição do status

@Setter
@Getter
public class StatusPedidoResponse {
	
	private Integer id;
	private String nome;

}
