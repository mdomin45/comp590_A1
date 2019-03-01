package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
	public static void main(String[] args) throws IOException, InsufficientBitsLeftException {
		
		// section 1
		// grab the compressed data file
		InputStream stream = new FileInputStream("C:\\Users\\matthew\\eclipse-workspace\\My A1\\src\\compressed.dat");
		// decode the  file and output it
		Decoder decoder = new Decoder(stream);
		decoder.decode("uncompressed.dat");
		
		// section 2
		// grab the uncompressed data file
		InputStream secondStream = new FileInputStream("uncompressed.dat");
		// encode the file and output it
		Encoder encoder = new Encoder(secondStream);
		encoder.encode("recompressed.dat");
		// make sure the decoder, encoder worked properly (the decoder should be able to decode what the encoder encoded)
		InputStream thirdStream = new FileInputStream("recompressed.dat");
		Decoder secondDecoder = new Decoder(thirdStream);
		secondDecoder.decode("re_uncompressed.dat");
		
		// section 3
		Entropy entropy = new Entropy(decoder);
		entropy.get_entropy();
		
	}
}
