package io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Tree {

	public Tree(List<Symbol> symbols) {
		// set codes starting at 0
		symbols.get(0).set_code(0);
		
		// iterate through to set codes
		for (int i = 1; i < symbols.size(); i++) {
			// add 1 each time while iterating through
			symbols.get(i).set_code(symbols.get(i - 1).get_code() + 1);
			// if length is greater than length of previous
			if (symbols.get(i).get_length() > symbols.get(i - 1).get_length()) {
				// set differential to difference in lengths
				int differential = symbols.get(i).get_length() - symbols.get(i - 1).get_length();
				// shift left by differential
				symbols.get(i).set_code(symbols.get(i).get_code() << differential);
			}
		}
	}

	public OutputStream decode(List<Symbol> symbols, int symbolCount, InputStreamBitSource bitStream,
			String output) throws InsufficientBitsLeftException, IOException {
		OutputStream out = new FileOutputStream(output);
		// variables for amount decoded, placeholder, length
		int numberDecoded = 0;
		int codeOfCurrent = 0;
		int length = 0;
		
		// loop until all symbols are decoded
		while (numberDecoded < symbolCount) {
			codeOfCurrent = codeOfCurrent << 1;
			codeOfCurrent = codeOfCurrent | bitStream.next(1);
			length++;
			
			// iterate to find the matching symbol
			int i = 0;
			while (i < 255) {
				if(codeOfCurrent == symbols.get(i).get_code() &&
						length == symbols.get(i).get_length()) {
					out.write(symbols.get(i).get_symbol());
					numberDecoded++;
					codeOfCurrent = 0;
					length = 0;
				}
				i++;
			}
		}
		return out;
	}

}
