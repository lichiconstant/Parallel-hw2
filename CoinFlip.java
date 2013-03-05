import java.util.Random;

public class CoinFlip implements Runnable{
	
	private int thread_id;    // Variable containing specific id of this thread.
	private int single_heads;
	public static int total_flips = 0;
	public static int total_heads = 0;
	public static int single_flips = 0;
	
	 // Run: overides Runnabale.Run, thread entry point
	public void run ()
	{
		//System.out.format("Start %d thread%n", thread_id);
		Random coin = new Random ();
		for(int i = 0 ; i < single_flips ; i++)
			if( coin.nextInt() >= 0 )
				single_heads++;
		
		synchronized(CoinFlip.class)
		{
			total_heads += single_heads;
		}
		//System.out.format("Thread %d Finishes %d flips%n", thread_id, single_flips);
	}
	
	CoinFlip(int thread_id_)
	{
		thread_id = thread_id_;
		single_heads = 0;
	}
	
	public static void main(String[] args) {
		//int thread_num = 2;
		//int flip_total = 10000000;
		
		if ( args.length != 2 ) 
	    	{
	     	 	System.out.println("Usage: CoinFlip #threads #iterations");
	      		return;
	    	} 
		
		int thread_num = Integer.parseInt(args[0]);
		int flip_total = Integer.parseInt(args[1]);
		
		//System.out.println("CoinFlip Experiment by " + thread_num + " threads and " + flip_total +" flip trials!");
		
		total_flips = flip_total;
		single_flips = (int)((double)(total_flips/thread_num));
		
		//System.out.format("Initializing %d Threads...%n", thread_num);
		long runstart = System.currentTimeMillis();
		
		Thread[] th = new Thread[thread_num];
		// Starup Cost for each thread
		//long startup_cost_t1 = System.currentTimeMillis();
		for( int id = 0 ; id < thread_num; id++ )	
		{
			th[id] = new Thread( new CoinFlip(id));
			th[id].start();
		}
		//long startup_cost = System.currentTimeMillis() - startup_cost_t1;
		//System.out.println("Startup Cost Time: " + startup_cost + "ms");

		//System.out.format("%d Threads Initialization Completed...%n", thread_num);
		
		// Await the completion of all threads
	    for ( int i=0; i<thread_num; i++ )
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
	    long elapsed = System.currentTimeMillis() - runstart;
	    
	    System.out.println(total_heads + " heads in " + total_flips + " tosses.");
	    System.out.println("Elapsed time: " + elapsed + "ms");
		
		/*/ Starup Cost for each thread
		System.out.println("Startup Cost Experiment...");
		long startup_cost_t1 = System.currentTimeMillis();
		int iter = 10000;
		for( int i = 0 ; i < iter; i++ )	
			new Thread( new CoinFlip(0));
		long startup_cost = System.currentTimeMillis() - startup_cost_t1;
		System.out.println("Startup Cost Time: " + (double)startup_cost/iter + "ms");
		*/
	}

}
