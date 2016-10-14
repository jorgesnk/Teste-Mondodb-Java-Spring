package com.teste.controller;

import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teste.config.Configurar;
import com.teste.model.Usuario;

@EnableMongoRepositories
@RestController
public class UsuarioRestController {

	@Autowired
	Configurar con;

	@Transactional
	@RequestMapping(value = "/salvar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> salvar(@RequestBody Usuario usurio) {

		try {

			con.mongoOperations().save(usurio);
			return new ResponseEntity(HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}
	}

	@Transactional
	@RequestMapping(value = "/listar/{nome}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Usuario> listar(@PathVariable String nome) {

		try {
			Query query = new Query(Criteria.where("nome").regex("^" + nome));
			List<Usuario> list = con.mongoOperations().find(query, Usuario.class);
			return list;
		} catch (Exception e) {
			return null;
		}

	}

	@Transactional
	@RequestMapping(value = "/atualizar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void atualizar(@RequestBody String strnome) {

		try {
			System.out.println("passou -1");
			Usuario usuario = new Usuario();
			System.out.println("passou 0");
			JSONObject job = new JSONObject(strnome);
			System.out.println("passou JO");
			Update up = new Update();
			usuario.setNome(job.getString("novo"));

			up.set("nome", usuario.getNome());
			System.out.println("passou 1");
			usuario.setNome(job.getString("nome"));
			Query query = new Query(Criteria.where("nome").is(usuario.getNome()));
			System.out.println("passou 2");
			con.mongoOperations().updateFirst(query, up, Usuario.class);

			System.out.println("passou 3");

		} catch (Exception e) {
			System.out.println("errao");
		}

	}
	
	
	@Transactional
	@RequestMapping(value="/deletar/{nome}",method=RequestMethod.DELETE)
	public void deletar(@PathVariable String nome){
		Query query =new Query(Criteria.where("nome").is(nome));getClass();
		try {
			con.mongoOperations().remove(query, Usuario.class);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
