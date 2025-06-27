package br.com.cotiinformatica.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.cotiinformatica.domain.StatusPedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

// so coloca @data nao precisa get e set, do lombok
// gera em tempo de compilação, construtor, get, set, to string, equals e hashcode

@Entity  //Classifica como uma tabela do banco de dados 
@Table(name="tb_pedidos") //identifica o nome da tabela
@Data
public class Pedido {

	@Id  //identifica a chave primaria
	@GeneratedValue(strategy = GenerationType.UUID)  // se fosse identity seria numeral sequencial-estratégia para geração da chave primaria
	@Column (name = "id", columnDefinition= "uuid") // identifica o nome do campo, tipo, 
	private UUID id;  // tipo único e universal
	
	@Column  (length= 100, nullable = false)
	private String cliente;
	
	@Temporal(TemporalType.TIMESTAMP)  // como quer gravar a data no banco
	@Column (nullable = false)
	private LocalDateTime dataHora;
	
	@Column (nullable = false, precision =15, scale = 2)
	private BigDecimal valor; // tem que definir o tamanho do campo e a quantidade de casas decimais
	
	@Enumerated(EnumType.ORDINAL)  // identifica o campo com o enumeration existente
	@Column (nullable = false)
	private StatusPedido status;
}


// LomBok: simplifica o código, não precisa de todos os getters e setters exigidos pela javabean
// coloca ele nas dependencias do pom, também precisa instalar o lombok como um plugin no eclipse.

// Usa anotações do spring data jpa para indicar qual tabela é a entidade
// quando não reconhece a classe, vai na raiz do projeto, mavem e update project e force update reinstala as bibliotecas que estão no pom
// banco sempre classe do jacarta