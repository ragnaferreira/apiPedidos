package br.com.cotiinformatica.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.domain.dtos.requests.PedidoRequest;
import br.com.cotiinformatica.domain.dtos.responses.PedidoResponse;
import br.com.cotiinformatica.domain.interfaces.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// Camada de apresentação (web), lida com solicitações http e devolve respostas http
// @RestController: anotação obrigatória para cada classe de controle que será publicado como endpoint
// @RequestMapping: Mapeia o endereço do serviço que será disponibilizado
// POST/PUT/ DELETE ou GET ->incluir/alterar/excluir
// Toda API deve retornar valor, não pode ser void
// Rodar API: não tem interface com usuário, botão direito em apipedido, run as sprint boot app
// Bilbioteca para documentação: swagger, gera documentação do código da api. OPEN API
// o swagger, gera documentação dos métodos, http://localhost:8081/swagger-ui/index.html
// @tag e Operation: colocar documentação
// Postman: usado para testar a API. Abre o aplicativo do Postman e configura as API que quer testar.
// Controler precisa acessar o dominio através da Interface pedidoService (melhor do que acessar classes) isso se chama inversão de dependencia

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(
			name="Controle de Pedidos", 
			description = "Serviços para gerenciamento de solicitações de pedidos."
	)
public class PedidosController {
	// construir um primeiro controlador
	// usar anotações
	// Controlador são serviços que serão disponibilizados, endpoint (endereço), nivel mais alto
	
	// Aqui que informo que o controller vai acessar a interface (service)
	// anotação para inicializar um atributo de uma classe automática (injeção de dependencia), o próprio springboot já insere todas as configurações e preferencias necessárias
	// para que um objeto seja inicializado, não precisa fazer isso manualmente
	
	
	@Autowired 
	private PedidoService pedidoService;

	//Identifica que é um método post
	@Operation (summary="Cadastro de solicitação de pedido.",
				description = "Cria uma nova solicitação de pedido")
	@PostMapping
	public ResponseEntity<PedidoResponse> post(@RequestBody @Valid PedidoRequest request) { // todo método post, recebe valor no corpo da requisição, @RequestBody: enviado no corpo da requisição tag <body> (json)
		var response = pedidoService.criar(request);
		return ResponseEntity.status(201).body(response); 
		//O status 201, "Created", na comunicação HTTP, indica que a solicitação foi bem-sucedida e resultou na criação de um ou mais novos recursos.
	}
	// ********************************************************************************************************************
	// put: recebe todos os campos do objeto
	@Operation (summary="Alteração de solicitação de pedido.",
				description = "Modifica uma nova solicitação de pedido existente")
	@PutMapping("{id}")  // passa no endereço, indicado pelo @pathvariable
	public ResponseEntity<PedidoResponse> put(@PathVariable UUID id, @RequestBody @Valid PedidoRequest request) { // recebe pedidorequest, também precisa receber o id vem no endereços
		var response = pedidoService.alterar(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(response); 
	}
	
	// ********************************************************************************************************************
	@Operation (summary="Exclui um pedido.",
			description = "Remove uma nova solicitação de pedido existente")
	@DeleteMapping("{id}")
	public ResponseEntity<PedidoResponse> delete(@Valid @PathVariable UUID id) {
		var response = pedidoService.excluir(id);
		return ResponseEntity.status(HttpStatus.OK).body(response); 
	}
	
	// ********************************************************************************************************************
	// @RequestParam define parametro na url opcionais
	@Operation (summary="Consulta de vários pedidos.",
			description = "Retorna uma lista de solicitações de pedido.")
	@GetMapping
	public ResponseEntity<Page<PedidoResponse>> getAll(@RequestParam(defaultValue = "0") int page,  // a page começa com "0"
											@RequestParam(defaultValue = "25") int size,
											@RequestParam(defaultValue = "id") String sortBy){
		
		var pageable = PageRequest.of(page, size, Sort.by(sortBy));
		var response = pedidoService.consultar(pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	// ********************************************************************************************************************
	// @RequestParam define parametro na url opcionais
	@Operation (summary="Obter Solicitação de pedido por Id.",
			description = "Retorna pedido através do Id.")
	@GetMapping("{id}")
	public ResponseEntity<PedidoResponse> getbyId(@PathVariable UUID id) {
		
		var response = pedidoService.obter(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
}
