package br.com.cotiinformatica.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.domain.entities.Pedido;

// está dizendo que é repositorio, vai herdar métodos para acessar BD gerenciado pelo SpringData
// informa o que a classe está representando, todos herdam de @Component
// informa a entidade e a chave primária
// DDD Domains Driven Design - isola a lógica e regra de negócio na parte central do projeto chamado domínio
// As entidades ficam no domínio porque fazem parte da lógica da api
// Serviços de domínio: interfaces e classes responsáveis por implementar cada operação/lógica de negócio envolvendo as entidades
// Exemplo: pedidos -> preciso de criar pedido, alterar, excluir, consulta, obter pedido.
// Primeiro cria uma interface (contrato) que defina estas operações, esta interface fica dentro do domain
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

}
