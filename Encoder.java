package io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Encoder {
	private List<Symbol> _symbols;
	private List<Node> _nodes;
	private int[] _read;
	
	public Encoder(InputStream stream) throws IOException {
		_symbols = new ArrayList<Symbol>();
		_nodes = new ArrayList<Node>();
		
		int i = 0;
		while (i < 256) {
			Symbol current = new Symbol();
			current.set_symbol(i);
			_symbols.add(current);
			_nodes.add(new Node(current));
			i++;
		}
		
		int[] counts = new int[256];
		BigDecimal[] Counts = new BigDecimal[256];
		int total = stream.available();
		_read = new int[total];
		BigDecimal Total = new BigDecimal(total);
		
		i = 0;
		while (i < total) {
			_read[i] = stream.read();
			for(int j = 0; j < 256; j++) {
				if (_read[i] == j) {
					counts[j]++;
				}
			}
			i++;
		}
		
		i = 0;
		while (i < 256) {
			Counts[i] = new BigDecimal(counts[i]);
			i++;
		}
		
		i = 0;
		while (i < 256) {
			_nodes.get(i).set_probability(Counts[i].divide(Total, 50, RoundingMode.HALF_UP));
			i++;
		}
		
		Collections.sort(_nodes);
	}
	
	public OutputStream encode(String out) throws IOException {
		OutputStream output = new FileOutputStream(out);
		OutputStreamBitSink bitstream = new OutputStreamBitSink(output);
		
		List<Node> leaves = new ArrayList<Node>();
		// keep going if the list still has 1 symbol
		while(_nodes.size() > 1) {
			// grab left node, check if it's leaf
			Node left = _nodes.remove(0);
			if (left.is_leaf()) {
				leaves.add(left);
			}
			// grab right node, check if it's leaf
			Node right = _nodes.remove(0);
			if (right.is_leaf()) {
				leaves.add(right);
			}
			// create new node with probability = sum of two probabilities
			Node current = new Node(
					left,
					right,
					Math.max(left.get_height(), right.get_height()) + 1,
					left.get_probability().add(right.get_probability(), new MathContext(50, RoundingMode.HALF_UP)));
			_nodes.add(current);
			// update the attributes of the left and right children
			left.set_parent(current);
			left.change_parent_status(true);
			right.set_parent(current);
			right.change_parent_status(true);
			current.renew_lengths();
			Collections.sort(_nodes);
		}
		
		// assign lengths by symbol
		int i = 0;
		while (i < 256) {
			_symbols.get(leaves.get(i).get_symbol()).set_length(leaves.get(i).get_length());
			_symbols.get(leaves.get(i).get_symbol()).set_probability(leaves.get(i).get_probability());
			i++;
		}
		Collections.sort(_symbols);
		
		// create new tree with the symbols, creates nodes
		Tree tree = new Tree(_symbols);
		
		i = 0;
		int j = 0;
		while (i < 256) {
			Symbol temp = _symbols.get(i);
			j = 0;
			while (j < 256) {
				if (_symbols.get(j).get_symbol() == i) {
					_symbols.set(i, _symbols.get(j));
					_symbols.set(j, temp);
				}
				j++;
			}
			i++;
		}
		
		i = 0;
		while (i < 256) {
			bitstream.write(_symbols.get(i).get_length(), 8);
			i++;
		}
		bitstream.write(_read.length, 32);
		
		// for question 6 in part 3
		float entropy = 0;		
		i = 0;
		while (i < 256) {
			if (_symbols.get(i).get_probability().compareTo(new BigDecimal(0)) == 1) {
				entropy += _symbols.get(i).get_probability().floatValue() * _symbols.get(i).get_length();
			}
			i++;
		}
		System.out.println("#6 = " + entropy);
		
		// output the codes in symbol order
		i = 0;
		while (i < _read.length) {
			j = 0;
			while (j < 256) {
				if (_read[i] == _symbols.get(j).get_symbol()) {
					bitstream.write(_symbols.get(j).get_code(), _symbols.get(j).get_length());
				}
				j++;
			}
			i++;
		}
		
		// pad the output
		bitstream.padToWord();
		
		return output;
	}
}
