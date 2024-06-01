package com.gean.tttemplate.utils;

import java.util.Arrays;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javafx.scene.control.TreeItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class TweetPresetNode {
	
	private String nome;
	private String preset;
	private TweetPresetNode parent;
	private List<TweetPresetNode> presets;
	private boolean midia;
	
	public TweetPresetNode() {
		
	}
	
	public TweetPresetNode(String nome, String preset, boolean midia) {
		this();
		this.setNome(nome);
		this.setPreset(preset);
		this.setMidia(midia);
	}
	
	public TweetPresetNode(String nome, List<TweetPresetNode> presets) {
		this();
		this.setNome(nome);
		this.setPresets(presets);
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
	
	public List<TweetPresetNode> getPresets() {
		return presets;
	}
	
	public void setPresets(List<TweetPresetNode> presets) {
		this.presets = presets;
		
		if(this.presets != null) {
			for(TweetPresetNode node : this.presets) {
				node.parent = this;
			}
		}
	}
	
	public void addPreset(TweetPresetNode node) {
		if (this.presets == null) this.presets = Arrays.asList();
		
		this.presets.add(node);
		node.parent = this;
	}
	
	public boolean isMidia() {
		return midia;
	}
	
	public void setMidia(boolean midia) {
		this.midia = midia;
	}
	
	@JsonIgnore	
	public TweetPresetNode getParent() {
		return parent;
	}
	
	public TweetPreset toTweetPreset() {
		TweetPreset out = new TweetPreset();
		out.setNome(this.getNome());
		out.setPreset(this.getPreset());
		out.setMidia(this.isMidia());
		out.setParent(this.getParent() == null ? null : this.getParent().toTweetPreset());
		
		return out;
	}
	
	public TreeItem<TweetPreset> toTreeItem(){
		TreeItem<TweetPreset> out = new TreeItem<TweetPreset>(this.toTweetPreset());
		if(this.getPresets() != null) {
			for(TweetPresetNode node : this.getPresets()) {
				out.getChildren().add(node.toTreeItem());
			}
		}
		return out;
		
	}
        
        public MutableTreeNode toTreeNode(){
            DefaultMutableTreeNode out = new DefaultMutableTreeNode(this.toTweetPreset());
            if(this.getPresets() != null) {
                this.getPresets().forEach(preset -> out.add(preset.toTreeNode()));
            }
            return out;
        }
}
