package io;

import java.math.BigDecimal;

public class Node extends Symbol{
	// different nodes
	private Node _parent;
	private Node _rightChild;
	private Node _leftChild;
	
	// attributes
	private boolean _isLeaf;
	private boolean _hasParent;
	private int _height;
	
	// general constructor
	public Node(Node left, Node right, int height, BigDecimal n) {
		// call super constructor
		super();
		
		// set nodes and attributes
		_leftChild = left;
		_rightChild = right;
		_height = height;
		_hasParent = false;
		_isLeaf = false;
		this.set_probability(n);
	}
	
	// root constructor
	public Node(Symbol s) {
		// call super constructor
		super();
		
		// grab symbol, probability
		this.set_symbol(s.get_symbol());
		this.set_probability(s.get_probability());
		
		// set attributes
		_isLeaf = true;
		_hasParent = false;
		_height = 0;
	}
	
	public void renew_lengths() {
		// node is leaf
		if (this._isLeaf == true) {
			this.set_length(this._parent.get_length() + 1);
			return;
		}
		// has a parent
		if (this._hasParent == true) {
			this.set_length(this._parent.get_length() + 1);
		}
		
		// traverse the tree to renew all lengths
		this._leftChild.renew_lengths();
		this._rightChild.renew_lengths();
		return;
	}
	
	@Override
	public int compareTo(Object o) {
		Node n = (Node) o;
		
		// judge by probabilities
		if (this.get_probability().compareTo(n.get_probability()) == -1) {
			return -1;
		}
		else if (this.get_probability().compareTo(n.get_probability()) == 1) {
			return 1;
		}
		// otherwise use height
		else {
			if (n._height > this._height) {
				return -1;
			}
			else if (n._height < this._height) {
				return 1;
			}
			// otherwise equal
			return 0;
		}
	}
	
	// getters
	public Node get_parent() {
		return _parent;
	}
	
	public Node get_left_child() {
		return _leftChild;
	}
	
	public Node get_right_child() {
		return _rightChild;
	}
	
	public int get_height() {
		return _height;
	}
	
	public boolean is_leaf() {
		return _isLeaf;
	}
	
	public boolean has_parent() {
		return _hasParent;
	}
	
	// setters
	public void set_parent(Node n) {
		_parent = n;
	}
	
	public void set_left_child(Node n) {
		_leftChild = n;
	}
	
	public void set_right_child(Node n) {
		_rightChild = n;
	}
	
	public void set_height(int q) {
		_height = q;
	}
	
	public void set_leaf(boolean b) {
		_isLeaf = b;
	}
	
	public void change_parent_status(boolean b) {
		_hasParent = b;
	}
}
