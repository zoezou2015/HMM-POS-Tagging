package com.hmm;

public class Node {
	
	private String _tag;
	private Node _parent;
	private String _word;
	private double _prob;
	
	public Node(String tag, String word){
		this(tag, word, null, 0.0);
	}
	
	public Node(String tag, String word, Node parent, double prob){
		this._tag = tag;
		this._word = word;
		this._parent = parent;
		this._prob = prob;
	}

	public String get_tag() {
		return _tag;
	}

	public Node get_parent() {
		return _parent;
	}

	public String get_word() {
		return _word;
	}

	public double get_prob() {
		return _prob;
	}

	public void set_tag(String _tag) {
		this._tag = _tag;
	}

	public void set_parent(Node _parent) {
		this._parent = _parent;
	}

	public void set_word(String _word) {
		this._word = _word;
	}

	public void set_prob(double _prob) {
		this._prob = _prob;
	}


	
}
