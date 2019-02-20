package io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Decoder {
	// attributes
	private List<Symbol> _symbols;
	private int _symbolCount;
	private Tree _tree;
	
	// bit source from boiler
	private InputStreamBitSource _bitStream;
	
	// constructor
	public Decoder(InputStream stream) throws IOException, InsufficientBitsLeftException{
		
		_symbols = new ArrayList<Symbol>();
		_bitStream = new InputStreamBitSource(stream);
		
		// iterate through for lengths
		int i = 0;
		while(i < 256) {
			Symbol current = new Symbol();
			current.set_symbol(i);
			current.set_length(_bitStream.next(8)); // sections of 8
			_symbols.add(current);
			i++;
		}
		
		int temp[] = new int[4];
		_symbolCount = 0;
		
		// iterate through for different symbols
		int j = 0;
		while (j < 4) {
			int current = _bitStream.next(8);
			temp[j] = current << 8 * (4 - j - 1);
			_symbolCount += temp[j];
			j++;
		}
		
		// sort and create new tree with symbols
		Collections.sort(_symbols);
		_tree = new Tree(_symbols);
	}
	
	// create output stream
	public OutputStream decode(String output) throws InsufficientBitsLeftException, IOException {
		return _tree.decode(_symbols, _symbolCount, _bitStream, output);
	}
	
	// getters
	public List<Symbol> get_list() {
		return _symbols;
	}
}
