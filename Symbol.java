package io;

import java.math.BigDecimal;

public class Symbol implements Comparable {
	// attributes
	private BigDecimal _probability;
	private int _length;
	private int _symbol;
	private int _code;
	
	// constructor
	public Symbol() {
		_symbol = 0;
		_code = 0;
		_length = 0;
		_probability = new BigDecimal(0);
	}
	
	// comparator
	@Override
	public int compareTo(Object o) {
		Symbol s = (Symbol) o;
		if (s._length > this._length) {
			return -1;
		}
		else if (s._length < this._length) {
			return 1;
		}
		else if (s._symbol > this._symbol) {
			return -1;
		}
		
		return 1;
	}
	
	// getters
	public BigDecimal get_probability() {
		return _probability;
	}
	
	public int get_length() {
		return _length;
	}
	
	public int get_symbol() {
		return _symbol;
	}
	
	public int get_code() {
		return _code;
	}
	
	// setters
	public void set_probability(BigDecimal n) {
		_probability = n;
	}
	
	public void set_length(int q) {
		_length = q;
	}
	
	public void set_symbol(int q) {
		_symbol = q;
	}
	
	public void set_code(int q) {
		_code = q;
	}
}
