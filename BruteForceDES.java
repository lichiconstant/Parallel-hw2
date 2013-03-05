import javax.crypto.*;

import java.security.*;
import javax.crypto.spec.*;

import java.util.Random;

import java.io.PrintStream;


class BruteForceDES extends SealedDES implements Runnable{
	
	// Cipher for the class
	private int cipher_id = 0;
	
	// Searching range for DES key
	private long min_k;
	private long max_k;
	
	private String knownText;
	SealedObject sldObj;
	
	// create object to printf to the console
	PrintStream printer = new PrintStream(System.out);
	
	public void run()
	{
		//System.out.format("Start %d thread%n", cipher_id);
		long runstart;
		runstart = System.currentTimeMillis();

		for ( long i = min_k; i < max_k; i++ )
		{
			// Set the key and decipher the object
			setKey(i);
			String decryptstr = decrypt(sldObj);
			
			// Does the object contain the known plaintext
			if (( decryptstr != null ) && ( decryptstr.indexOf(knownText) != -1 ))
			{
				//  Remote printlns if running for time.
				//p.printf("Found decrypt key %016x producing message: %s\n", i , decryptstr);
				printer.printf("Thread %d Found decrypt key %d producing message: %s\n", cipher_id, i, decryptstr);
				//System.out.println (  "Found decrypt key " + i + " producing message: " + decryptstr );
			}
			
			// Update progress every once in awhile.
			//  Remote printlns if running for time.
			if ( i % 100000 == 0 )
			{ 
				long elapsed = System.currentTimeMillis() - runstart;
				//System.out.println ("Thread " + cipher_id + " Searched key number " + i + " at " + elapsed + " milliseconds.");
				printer.printf("Thread %d Searched key number %d at %d milliseconds\n", cipher_id, i, elapsed);	
			}
		}
	}
		
	// Constructor: initialize the cipher
	public BruteForceDES(int thread_id, long minKey, long maxKey, String knownText_, SealedObject sldObj_) 
	{
		cipher_id = thread_id;
		min_k = minKey;
		max_k = maxKey;
		knownText = knownText_;
		sldObj = sldObj_;
		
		try 
		{
			des_cipher = Cipher.getInstance("DES");
		} 
		catch ( Exception e )
		{
			System.out.println("Failed to create cipher.  Exception: " + e.toString() +
							   " Message: " + e.getMessage()) ; 
		}
	}
	
	// Program demonstrating how to create a random key and then search for the key value.
	public static void main ( String[] args )
	{
		
		if ( args.length != 2 )
		{
			System.out.println ("Usage: java SealedDES #thread_num #key_size_in_bits");
			return;
		}

		// Get the argument
		long keybits = Long.parseLong ( args[1] );
		int num_threads = Integer.parseInt(args[0]);
			
		//long keybits = 20;
		//int num_threads = 3;
		
		long maxkey = ~(0L);
		maxkey = maxkey >>> (64 - keybits);
		
		// Create a simple cipher
		//SealedDES enccipher = new SealedDES ();
		BruteForceDES enccipher = new BruteForceDES(-1, 0, 0, " ", null);
		
		// Get a number between 0 and 2^64 - 1
		Random generator = new Random ();
		long key =  generator.nextLong();
		
		// Mask off the high bits so we get a short key
		key = key & maxkey;
		
		System.out.println("Generated secret key "+key);
		// Set up a key
		enccipher.setKey ( key ); 
		
		// Generate a sample string
		String plainstr = "Johns Hopkins afraid of the big bad wolf?";
		
		// Encrypt
		//SealedObject sldObj = enccipher.encrypt ( plainstr );
		long runstart;
		runstart = System.currentTimeMillis();

		SealedObject[] sldObj = new SealedObject[num_threads];
		String[] knownText = new String[num_threads];
		long[] index = new long[num_threads+1];
		index[0] = 0;
		long interval = (long)((double)maxkey/num_threads);
		for(int i = 1 ; i <= num_threads; i++ )
		{
			index[i] = index[i-1]+interval;
			sldObj[i-1] = enccipher.encrypt( plainstr );
			knownText[i-1] = "Hopkins";
		}
		index[num_threads] = maxkey;	
			
		Thread[] th = new Thread[num_threads];
		for(int i = 0 ; i < num_threads ; i++ )
		{
			// Create a BruteForceCipher.
			// Search for the right key in certain range
			th[i] = new Thread( new BruteForceDES(i, index[i], index[i+1], knownText[i], sldObj[i]));
			th[i].start();
		}
		
		for(int i = 0 ; i < num_threads ; i++ )
		{
			try
	      	{
		        th[i].join();
	      	}
			catch (InterruptedException e)
			{
				System.out.println("Thread interrupted.  Exception: " + e.toString() +
			                   " Message: " + e.getMessage()) ;
			    return;
			}
		}
		
		// Output search time
		long elapsed = System.currentTimeMillis() - runstart;
		System.out.println ( "Final elapsed time: " + elapsed + " milliseconds.");
	}
}
