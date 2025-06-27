package br.com.cotiinformatica.domain.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Parecida com a entidade pedido
// usado para capturar os dados na api
// só precisa de getter e setter (coloca anotação)
// aqui usa a biblioteca de validação de dados usando anotações (sempre jakarta)
// Se der erro de validação dá erro de status 400, precisa tratar com classe global. Escuta a excessão, trata e devolve tratado chamada Error Handling

@Setter
@Getter
public class PedidoRequest {
	
	@NotBlank (message="O nome do Cliente é obrigatório.")  // não pode ser nulo, pode adicionar mensagem, notblank só para texto
	@Size(min=6, max=100, message="O nome do Cliente deve ter entre 6 e 100 caracteres.")  // tamanho do campo 
	private String cliente;
	
	// captura o campo data/hora como string, fica mais simples e depois valida data
	// usar expressão regular para validar data
	@NotBlank(message="A Data é obrigatória.")
	@Pattern(
			//expressão regular
		regexp = "^\\d{4}-\\d{2}-\\d{2}$",
		message="A Data deve estar no formato yyyy-MM-dd."
	)
	private String dataHora;
	
	@NotNull(message="O valor é obrigatório") // NotNull usado para números
	@DecimalMin(value="0.0", inclusive=false, message="O valor deve ser maior que zero.")  // valor mínimo para valor, inclusive=false não vale zero
	private Double valor;
	
	@NotNull(message="O status é obrigatório")
	@Min(value= 0, message="O status mínimo permitido é 0.")
	@Max(value= 4, message="O status máximo permitido é 4.")
	private Integer status;

}
