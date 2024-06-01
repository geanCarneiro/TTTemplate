package com.gean.tttemplate.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ListaNomeada{
	
	private String nome;
	private List<Object> lista;
	
	public ListaNomeada() {
		this.nome = "[N/D]";
		this.lista = Arrays.asList();
	}
	
	public ListaNomeada(String nome, List<Object> lista) {
		this();
		this.setNome(nome);
		this.setLista(lista);
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public List<Object> getLista() {
		return lista;
	}
	
	@JsonIgnore
	public List<String> getListaAsString() {
		if(lista.size() > 0 && lista.get(0) instanceof LinkedHashMap) {
			return lista.stream()
					.map(o -> ((Map)o).get(nome).toString())
					.collect(Collectors.toList());
		}
		
		
		return lista.stream()
				.map(String::valueOf)
				.collect(Collectors.toList());
	}
	
	public void setLista(List<Object> lista) {
		this.lista = lista;
	}
	
	public Object getValue(int i) {
		return this.lista.get(i);
	}
	
	public String getAsString(int i) {
		return String.valueOf(getValue(i));
	}
	
}
