package br.com.cotiinformatica.domain.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.cotiinformatica.domain.dtos.requests.PedidoRequest;
import br.com.cotiinformatica.domain.dtos.responses.PedidoResponse;

// Serviços necessários para o domínio
// DTOS-data transfer object: classe java bean utilizada para capturar dados e transporte de dados entre camadas/módulos/partes do sistema
// Para cada serviço, tenho dados que seram recebidos (requisição/input/request) e dados que serão retornados após processamento (resposta/output/response)
// é a interface que se comunica com o controller
// a implementação é feita no pacote services dentro do domain

public interface PedidoService {

		// requisição: deverá receber uma requisição com as informações: cliente, data/hora, valor, status
		// resposta: id do pedido e os demais campos
		// não precisa passar cada campo, chama o DTO request e retorna o Pedidoresponse (campos definido lá)
		// porque não pode deixar transparente os campos que compoe a entidade
		PedidoResponse criar(PedidoRequest request);
		
		PedidoResponse alterar(UUID id, PedidoRequest request);
		
		PedidoResponse excluir(UUID id);
		
		// devolver os pedidos paginados, no máximo 25 registros
		// pageable; o controlador passa parametros para paginar e devolver
		// Page retorna várias informações da paginação
		Page <PedidoResponse> consultar(Pageable pageable);
		
		PedidoResponse obter(UUID id);

}
