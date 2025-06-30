package br.com.cotiinformatica.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.github.javafaker.Faker;

import br.com.cotiinformatica.components.MessageProducerComponent;
import br.com.cotiinformatica.domain.dtos.requests.PedidoRequest;
import br.com.cotiinformatica.domain.dtos.responses.PedidoResponse;
import br.com.cotiinformatica.domain.entities.Pedido;
import br.com.cotiinformatica.domain.exceptions.PedidoNaoEncontradoException;
import br.com.cotiinformatica.repositories.PedidoRepository;

public class PedidoServiceImplTest {
	
	//atributos, todas as classes que serão testada
	private PedidoServiceImpl pedidoServiceImpl;
	private PedidoRepository pedidoRepository;
	private ModelMapper modelMapper;
	private MessageProducerComponent messageProducerComponent;
	

	//prepara para rodar como uma classe de teste
	// antes de rodar qualquer método da classe de teste, ele roda este setup
	@BeforeEach
	void setup() {
		
		//mocar (imitar) os comportamentos das classes que estão vinculadas ao service
		// a ideia do mock é testar a service, as demais classes serão mocadas
		// para que os atributos da classe possam ser mocados, os atributos devem ser publicos ou ter get e set
		pedidoRepository = mock(PedidoRepository.class);
		messageProducerComponent = mock(MessageProducerComponent.class);
		modelMapper = new ModelMapper();
		
		//instanciar classe de servico para ser testada
		pedidoServiceImpl = new PedidoServiceImpl();
		
		//adiciona as dependencias da classe service (criadas pelo mock)
		pedidoServiceImpl.pedidoRepository = pedidoRepository;
		pedidoServiceImpl.messageProducerComponent = messageProducerComponent;
		pedidoServiceImpl.modelMapper = modelMapper;
	}
	
	//anotação do junit para qualificar um método como rotina de teste, o junit só executa métodos com esta anotação
	@Test
	@DisplayName("Deve criar pedido com sucesso")  // aparece no console e relatorio, ao inves do nome do método
	void testCriarPedidoComSucesso() {
		
		var request= gerarPedidoRequest();
		var pedido = gerarPedido(UUID.randomUUID(), request);
		
		//quando o método save for chamdo com um pedido então devolva o pedido que eu criei acima (para não gravar no banco) 
		when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
		
		//executa o metodo da service e captura a resposta
		var response = pedidoServiceImpl.criar(request);
		
		//Asserções (verificações do teste=> resultado esperado X resultado obtido)
		assertNotNull(response);  // não pode ter retornado null
		assertEquals(request.getCliente(), response.getCliente()); // se valor que enviei é igual ao valor que retornou
		
		// confirmar que o metodo, send da mensageria, foi chamado exatamente um vez no componente messageproducerComponente
		// se passou pelo menos 1 vez
		verify(messageProducerComponent, times(1)).send(any(Pedido.class));  // usa o copilot para criar estas sintaxes de teste	
		
	}
	@Test
	@DisplayName("Deve alterar pedido com sucesso.")
	void testAlterarPedidoComSucesso() {
		UUID id = UUID.randomUUID();
       PedidoRequest request = gerarPedidoRequest();
       Pedido pedido = gerarPedido(id, request);
       when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
       when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
       PedidoResponse response = pedidoServiceImpl.alterar(id, request);
       assertNotNull(response);
       assertEquals(id, response.getId());
       assertEquals(request.getCliente(), response.getCliente());
	}
	
	@Test
	@DisplayName("Deve lançar erro se tentar alterar pedido não encontrado.")
	void testAlterarPedidoNaoEncontrado() {
       UUID id = UUID.randomUUID();
       PedidoRequest request = gerarPedidoRequest();
       when(pedidoRepository.findById(id)).thenReturn(Optional.empty());
       assertThrows(PedidoNaoEncontradoException.class, () -> {
           pedidoServiceImpl.alterar(id, request);
       });
	}
	@Test
	@DisplayName("Deve excluir pedido com sucesso.")
	void testExcluirPedidoComSucesso() {
		
	   UUID id = UUID.randomUUID();
       PedidoRequest request = gerarPedidoRequest();
       Pedido pedido = gerarPedido(id, request);
       when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
       PedidoResponse response = pedidoServiceImpl.excluir(id);
       assertEquals(id, response.getId());
       assertEquals(request.getCliente(), response.getCliente());
      
       verify(pedidoRepository, times(1)).delete(pedido);		
	}
	
	@Test
	@DisplayName("Deve lançar erro se tentar excluir pedido não encontrado.")
	void testExcluirPedidoNaoEncontrado() {
       UUID id = UUID.randomUUID();
       when(pedidoRepository.findById(id)).thenReturn(Optional.empty());
       assertThrows(PedidoNaoEncontradoException.class, () -> {
           pedidoServiceImpl.excluir(id);
       });
	}
	@Test
	@DisplayName("Deve obter pedido com sucesso.")
	void testObterPedidoComSucesso() {
		 UUID id = UUID.randomUUID();
		 PedidoRequest request = gerarPedidoRequest();
	     Pedido pedido = gerarPedido(id, request);
	     when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
	     PedidoResponse response = pedidoServiceImpl.obter(id);
	     assertNotNull(response);
	     assertEquals(id, response.getId());		
	     assertEquals(request.getCliente(), response.getCliente());
	}
	
	@Test
	@DisplayName("Deve lançar erro se tentar obter pedido não encontrado.")
	void testObterPedidoNaoEncontrado() {
       UUID id = UUID.randomUUID();
       when(pedidoRepository.findById(id)).thenReturn(Optional.empty());
       assertThrows(PedidoNaoEncontradoException.class, () -> {
           pedidoServiceImpl.obter(id);
       });
	}
	@Test
	@DisplayName("Deve consultar pedidos com sucesso.")
	void testConsultarPedidoComSucesso() {
		PedidoRequest request1 = gerarPedidoRequest();
	    PedidoRequest request2 = gerarPedidoRequest();
	    Pedido pedido1 = gerarPedido(UUID.randomUUID(), request1);
	    Pedido pedido2 = gerarPedido(UUID.randomUUID(), request2);
	    List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);
	    Pageable pageable = PageRequest.of(0, 2);
	    Page<Pedido> page = new PageImpl<>(pedidos, pageable, pedidos.size());
	    when(pedidoRepository.findAll(pageable)).thenReturn(page);
	    Page<PedidoResponse> responsePage = pedidoServiceImpl.consultar(pageable);
	    assertNotNull(responsePage);
	    assertEquals(2, responsePage.getContent().size());
	    assertEquals(pedido1.getCliente(), responsePage.getContent().get(0).getCliente());
	    assertEquals(pedido2.getCliente(), responsePage.getContent().get(1).getCliente());
	    verify(pedidoRepository, times(1)).findAll(pageable);		
	}
	
	//Método para gerar dados de um nova requisição de pedido (dto)
	private PedidoRequest gerarPedidoRequest() {
						
		var request = new PedidoRequest();
		var faker = new Faker();
		
		request.setCliente(faker.name().fullName());
		request.setDataHora("2025-07-01");
		request.setValor(faker.number().randomDouble(2, 1, 1000));
		request.setStatus(faker.number().numberBetween(0, 4));
		
		return request;
	}
	
	//Método para gerar dados de um novo pedido (entidade)
	private Pedido gerarPedido(UUID id, PedidoRequest request) {
		var pedido = modelMapper.map(request, Pedido.class);
		pedido.setId(id);
		return pedido;
	}
}


