package br.com.cotiinformatica.domain.exceptions;

import java.text.MessageFormat;
import java.util.UUID;

// herda da runtime que é uma classe de erro do java
public class PedidoNaoEncontradoException extends RuntimeException{

	/**
	 * para não dar warning
	 */
	private static final long serialVersionUID = -7315852459674962975L;
	
	private UUID pedidoId;
	
	//construtor
	// toda vez que der erro quero que passe o id que não foi encontrado
	public PedidoNaoEncontradoException(UUID pedidoId) {
		this.pedidoId = pedidoId;
	}
	
	// substituir o metodo original @override
	@Override
	public String getMessage() {
		return MessageFormat.format("O pedido ''{0}'' não foi encontrado.",pedidoId);
	}

}
