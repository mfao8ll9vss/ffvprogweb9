package com.fatec.scel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.fatec.scel.mantemLivro.model.Livro;
import com.fatec.scel.mantemLivro.ports.LivroRepository;

@SpringBootTest
class REQ01CadastrarLivroTests {
	@Autowired
	LivroRepository repository;
	Livro livro;
	private Validator validator;
	private ValidatorFactory validatorFactory;

	@Test
	void ct01_cadastrar_livro_com_sucesso() {
		repository.deleteAll();
		// Dado – que o atendente tem um livro nao cadastrado
		livro = new Livro("1111", "Engenharia de Software", "Pressman");
		// Quando – o atendente cadastra um livro com informações validas
		repository.save(livro);
		// Então – o sistema valida os dados E permite a consulta do livro.
		assertEquals(1, repository.count());
	}

	@Test
	void ct02_cadastrar_livro_com_titulo_invalido() {
		// Dado - que o atendente tem um livro nao cadastrado
		livro = new Livro("1111", "", "Pressman");
		// Quando - o usuario nao informa o titulo do livro
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Livro>> violations = validator.validate(livro);
		// Entao - o sistema rejeita o cadastro
		assertFalse(violations.isEmpty());
	}

	@Test
	void ct03_cadastrar_livro_com_titulo_invalido() {
		// Dado - que o atendente tem um livro nao cadastrado
		livro = new Livro("1111", "", "Pressman");
		// Quando - o usuario nao informa o titulo do livro
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Livro>> violations = validator.validate(livro);
		// Entao - o sistema rejeita o cadastro
		assertEquals("Titulo deve ter entre 1 e 50 caracteres", violations.iterator().next().getMessage());
	}

	@Test
	void ct04_quando_isbn_cadastrado_rejeita_o_cadastro() {
		// Dado – que o ISBN do livro ja está cadastrado
		repository.deleteAll();
		livro = new Livro("4444", "Teste de Software", "Delamaro");
		repository.save(livro);
		// Quando – o usuário registra as informações do livro e confirma a operação
		livro = new Livro("4444", "Teste de Software", "Delamaro");
		// Então – o sistema valida as informações E retorna violação de integridade.
		try {
			repository.save(livro);
		} catch (DataIntegrityViolationException e) {
			//assertEquals("could not execute statement", e.getMessage().substring(0, 27));
			//assertEquals(1, repository.count());
		}
	}
}
