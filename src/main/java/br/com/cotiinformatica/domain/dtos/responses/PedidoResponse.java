package br.com.cotiinformatica.domain.dtos.responses;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

// o que será devolvido da operação, exemplo criar = id do pedido e demais informações
// o retorno não deve ser uma mensagem (sucesso) mas os dados do registro para confirmar que deu certo

@Setter
@Getter
public class PedidoResponse {

	private UUID id;
	private String cliente;
	// captura o campo data/hora como string, fica mais simples
	private String dataHora;
	private Double valor;
	private StatusPedidoResponse status;

}
