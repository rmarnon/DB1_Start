package com.db1.cidadesapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db1.cidadesapi.entities.Agencia;
import com.db1.cidadesapi.entities.Cliente;
import com.db1.cidadesapi.entities.Conta;
import com.db1.cidadesapi.enums.EstadoConta;
import com.db1.cidadesapi.repositories.ContaRepository;

@Service
public class ContaService {

	@Autowired
	private AgenciaService agenciaService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private ContaRepository contaRepository;
	
	public Conta criar(Double saldo, Long agenciaId, Long clienteId) {
		Agencia agencia = agenciaService.buscarPorId(agenciaId);
		Cliente cliente = clienteService.buscarPorId(clienteId);
		Conta conta = new Conta(agencia, saldo, cliente);
		return contaRepository.save(conta);
	}
	
	public void deletarTodasAsContas() {
		contaRepository.deleteAll();
	}
	
	public Conta buscarPorId(Long id) {
		Optional<Conta> estado = contaRepository.findById(id);
		return estado.orElseThrow(() -> new RuntimeException("Conta nao encontrada! id: " 
				+ id + ", Tipo: " + Conta.class.getName()));
	}
	
	public List<Conta> retornaTodasAsContas() {
		List<Conta> conta = contaRepository.findAll();
		return conta;
	}
	
	public void sacar(Long id, double valor) {
		Optional<Conta> conta = contaRepository.findById(id);
		conta.ifPresent(con -> {
			con.saque(valor);
			contaRepository.save(con);
		});
	}
	
	public void alteraTipoConta(Long id, String estado) {
		Optional<Conta> conta = contaRepository.findById(id);
		conta.ifPresent(con -> {
			con.alteraTipo(EstadoConta.valueOf(estado));
			contaRepository.save(con);
		});
	}
	
	public void tranfereValores(Conta contaSaida, Conta contaDestino, double valor) {
		Optional<Conta> conta = contaRepository.findById(contaSaida.getId());		
		conta.ifPresent(operacao -> {
			operacao.transferencia(100.00, contaDestino);
			contaRepository.save(operacao);
		});
	}	
	
}
