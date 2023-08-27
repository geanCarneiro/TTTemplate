package main.utils;

import java.util.ArrayList;

public class TweetPreset {
	
	public static final int MAX_TWEET_SIZE = 280;
	
	private String nome;
	private TweetPreset parent;
	private String preset;
	private boolean midia;
	
	public TweetPreset() {
		
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getPreset() {
		return preset;
	}
	
	public void setPreset(String preset) {
		this.preset = preset;
	}
	
	public boolean getMidia() {
		return midia;
	}

	public void setMidia(boolean midia) {
		this.midia = midia;
	}
	
	public TweetPreset getParent() {
		return parent;
	}
	
	public void setParent(TweetPreset parent) {
		this.parent = parent;
	}
	
	public String getPath() {
		ArrayList<String> path = new ArrayList<String>();
		
		TweetPreset atual = this;
		do {
			path.add(0, atual.getNome());
			atual = atual.getParent();
		} while (atual != null);
		
		return String.join(" > ", path);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nome + ": " + this.preset;
	}
	
}
