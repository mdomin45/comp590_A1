package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Entropy {
	private InputStream _firstStream;
	private int[] _ints;
	private BigDecimal[] _decimals;
	private float[] _misc;
	private BigDecimal[] _probabilities;
	private Decoder _decoder;
	
	public Entropy(Decoder d) throws FileNotFoundException {
		_decoder = d;
		_firstStream = new FileInputStream("uncompressed.dat");
		_ints = new int[256];
		_decimals = new BigDecimal[256];
		_misc = new float[256];
		_probabilities = new BigDecimal[256];
		
	}
	
	public void get_entropy() throws IOException {
		// info keeping
		int total = _firstStream.available();
		BigDecimal decimalTotal = new BigDecimal(total);
		
		int i = 0;
		int j = 0;
		int current;
		while (i < total) {
			current = _firstStream.read();
			j = 0;
			while (j < 256) {
				if (current == j) {
					_ints[j]++;
				}
				j++;
			}
			i++;
		}
		
		j = 0;
		while (j < 256) {
			_decimals[j] = new BigDecimal(_ints[j]);
			j++;
		}
		
		// Q2
		List<Integer> used = new ArrayList<Integer>();
		
		i = 0;
		while (i < 256) {
			_probabilities[i] = _decimals[i].divide(decimalTotal, 15, RoundingMode.HALF_UP);
			byte b = (byte) i;
			float temp = _probabilities[i].floatValue();
			_misc[i] = (float) -(Math.log10(temp)/Math.log10(2.0));
			if ( _probabilities[i].compareTo(new BigDecimal(0)) == 1) {
				used.add(i);
			}
			i++;
		}
		
		// Q3
		float entropy = 0;
		i = 0;
		while (i < 256) {
			if (used.contains(i)) {
				entropy += _probabilities[i].floatValue() * _misc[i];
			}
			i++;
		}
		System.out.println("#3 = " + entropy);
		
		// Q4
		float compressed = 0;
		i = 0;
		while (i < 256) {
			if (used.contains(i)) {
				compressed += _probabilities[i].floatValue() * _decoder.get_list().get(i).get_length();
			}
			i++;
		}
		System.out.println("#4 = " + compressed);
		
		
	}
}
