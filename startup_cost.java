import java.util.Random;

public class startup_cost implements Runnable{
	
	private int thread_id;    // Variable containing specific id of this thread.
	private int single_heads;
	public static int total_flips = 0;
	public static int total_heads = 0;
	public static int single_flips = 0;
	private Random coin = new Random();	
	 // Run: overides Runnabale.Run, thread entry point
	public void run ()
	{
		//Nothing because we need to leave startup cost as the only part
		;
	}
	
	startup_cost(int thread_id_)
	{
		thread_id = thread_id_;
		single_heads = 0;
	}
	
	public static void main(String[] args) {
		
		if ( args.length != 1 ) 
		{
     	 	System.out.println("Usage: CoinFlip #threads_num");
      		return;
    	} 
		
		int thread_num = Integer.parseInt(args[0]);
		//int thread_num = 8;
		single_flips = 0;
		int iter_num = 1000;
		
		//System.out.format("Initializing %d Threads...%n", thread_num);
		long runstart = System.currentTimeMillis();
		
		for( int iter = 0 ; iter < iter_num ; iter++)
		{
			Thread[] th = new Thread[thread_num];
			
			for( int id = 0 ; id < thread_num; id++ )	
			{
				th[id] = new Thread( new CoinFlip(id));
				th[id].start();	//nearly nothing for start
			}
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
		}
    	long elapsed = System.currentTimeMillis() - runstart;
    	double startup_cost = (double)elapsed / iter_num;
    	System.out.println(thread_num + " threads Startup Cost: " + startup_cost + "ms");
		
	}

}
