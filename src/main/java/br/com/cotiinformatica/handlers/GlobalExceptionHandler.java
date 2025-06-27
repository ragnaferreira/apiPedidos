package br.com.cotiinformatica.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import br.com.cotiinformatica.domain.exceptions.PedidoNaoEncontradoException;

// classe para tratar erros globalmente
// criar um tratamento de erro global, quando der pedido não encontrado exception deve dar erro 404

@ControllerAdvice
public class GlobalExceptionHandler {
	// metodos para tratar cada tipo de erro no projeto
	// trata sempre que der esse erro MethodArgumentNotValidException
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(
			MethodArgumentNotValidException exception,
			WebRequest request
			) {
		
		var erros = exception.getBindingResult()
					.getFieldErrors()
					.stream()
					.map(error -> "Campo: '" + error.getField() + "' : " + error.getDefaultMessage())
					.collect(Collectors.toList());
		
		var body = new HashMap<String, Object>();
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("erros", erros);
		
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	// na service lança o erro e o handler trata!!!
	// senao teria que colocar trycat em todo o código
	// trata antes da api retornar o erro ao cliente
	// <Map<String, Object>> = json
	@ExceptionHandler(PedidoNaoEncontradoException.class)
	public ResponseEntity<Map<String, Object>> handlePedidoNaoEncontradoException(
			PedidoNaoEncontradoException exception,
			WebRequest request
			) {
		
		var body = new HashMap<String, Object>();
		body.put("status", HttpStatus.NOT_FOUND.value());
		body.put("erros", exception.getMessage());
		
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

}
