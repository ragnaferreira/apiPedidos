package br.com.cotiinformatica.domain.services;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.components.MessageProducerComponent;
import br.com.cotiinformatica.domain.dtos.requests.PedidoRequest;
import br.com.cotiinformatica.domain.dtos.responses.PedidoResponse;
import br.com.cotiinformatica.domain.entities.Pedido;
import br.com.cotiinformatica.domain.exceptions.PedidoNaoEncontradoException;
import br.com.cotiinformatica.domain.interfaces.PedidoService;
import br.com.cotiinformatica.repositories.PedidoRepository;

// Classe de lógica de negócio
// Intermediaria entre a controller e persistencia (repositórios)
// classe que implementa agluma funcionalidade e todos os métodos que estão na interface
// clicando no nome da classe aparece a opção para puxar todos os métodos da interface
// Injeção de Dependencia: quando for usar, por exemplo a ModelMapper, o spring estancia e já configura o que for necessário
// ModeMapper: facilita copia de DTO para entidade
// Deixar a service o mais enxuto possivel

//@Validated
@Service
public class PedidoServiceImpl implements PedidoService{

	// alterado de private para public para usar nos testes
	@Autowired // springboot instanciar este cara sem precisar manualmente
	public PedidoRepository pedidoRepository;
	
	// pedindo para o framework me dar uma instancia do ModelMapper
	@Autowired
	public ModelMapper modelMapper;
	
	@Autowired
	public MessageProducerComponent messageProducerComponent;
	
	// pega do DTO - > Entidade (grava)
	// pega Entidade -> DTO (mostra)
	
	@Override
	public PedidoResponse criar(PedidoRequest request) {  //@valid passar pelas validações do DTO pedidorequest
		
		// TODO Auto-generated method stub
		// criar pedido no BD, gerando o ID e campos obrigatórios
		// usar a biblioteca SpringBootValidation (vem do Jacarta) para implementar toda a lógica de validação dos objetos
		// instala pelo pom.xml como dependencia
		// Conferir se os dados estão preenchidos corretamente, feito no dto request
		// para a validação funcionar, deve-se habilitar o uso das validações (anotação validated na classe) e o @valid em cada método
		// respositorio é uma outra camada do projeto, uma interface
		
		// *****************************  Sem Model Mapper ******************************************
		/*
		var pedido = new Pedido();
		pedido.setCliente(request.getCliente());
		pedido.setDataHora(LocalDate.parse(request.getDataHora(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay());
		pedido.setValor(BigDecimal.valueOf(request.getValor()));
		pedido.setStatus(StatusPedido.values()[request.getStatus()]);  //associa o inteiro que veio com o status do enum
		
		pedidoRepository.save(pedido);
		
		var response = new PedidoResponse();
		response.setId(pedido.getId());
		response.setCliente(pedido.getCliente());
		response.setDataHora(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(pedido.getDataHora()));
		
		var statusPedidoResponse = new StatusPedidoResponse();
		statusPedidoResponse.setId(pedido.getStatus().ordinal());
		statusPedidoResponse.setNome(pedido.getStatus().toString());
		response.setStatus(statusPedidoResponse);
		
		return response;
		*/
		
		// **************************** com ModelMapper *******************************************
		//pegar os dados do request e gerar o objeto pedido
		// neste caso cria um objeto novo
		var pedido = modelMapper.map(request, Pedido.class);
		
		pedidoRepository.save(pedido);
		
		messageProducerComponent.send(pedido);
		
		// preciso retornar o pedidoresponse
		return modelMapper.map(pedido, PedidoResponse.class);
		// **************************** com ModelMapper *******************************************
		
	}

	@Override
	public PedidoResponse alterar(UUID id, PedidoRequest request) {
		// TODO Auto-generated method stub
		// primeiro busca o pedido pelo ID
		var pedido = pedidoRepository.findById(id)  // metodo já existente da classe repository, retorna optional
					.orElseThrow(() -> new PedidoNaoEncontradoException(id));  // orelsethrow se ele não encontrar retorna uma exceção, não precisa de if e else
					// dá erro e sai saiu do método, não precisa do else
		
		// não quero criar um objeto novo, vou copiaro request para o pedido existente
		// copia do request para o pedido, mantem as referencias, usa o mesmo objeto, por isso não tem o .class
		modelMapper.map(request, pedido);
		
		pedidoRepository.save(pedido);
		
		return modelMapper.map(pedido, PedidoResponse.class);
	}

	@Override
	public PedidoResponse excluir(UUID id) {
		// TODO Auto-generated method stub
		var pedido = pedidoRepository.findById(id)  // metodo já existente da classe repository, retorna optional
				.orElseThrow(() -> new PedidoNaoEncontradoException(id)); 
		
		pedidoRepository.delete(pedido);
		return modelMapper.map(pedido, PedidoResponse.class);
	}

	@Override
	public Page <PedidoResponse> consultar(Pageable pageable){	
		// pageable: pode ordenar de diferentes campos
		var pedidos = pedidoRepository.findAll(pageable);
		
		// preciso copiar os objetos pedido para pedidoresponse, que é o objeto que disponibilizo para fora
		// método "map" parecido com "for"
		return pedidos.map(pedido -> modelMapper.map(pedido, PedidoResponse.class));
		
	}
	

	@Override
	public PedidoResponse obter(UUID id) {
		var pedido = pedidoRepository.findById(id)  // metodo já existente da classe repository, retorna optional
				.orElseThrow(() -> new PedidoNaoEncontradoException(id));
		
		return modelMapper.map(pedido, PedidoResponse.class);
	}

}
