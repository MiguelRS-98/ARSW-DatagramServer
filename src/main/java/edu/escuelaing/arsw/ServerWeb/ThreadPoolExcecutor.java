package edu.escuelaing.arsw.ServerWeb;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class generates the threads used for the multi-operation of the HTTP Server.
 * @author Miguel Angel Rodriguez Siachoque
 * @author Luis Daniel Benavides Navarro
 */
public class ThreadPoolExcecutor 
{
    /**
     * This main method creates enough threads for that operation.
     * @param args The client-initiated threads.
     */
    public static void main(String[] args) 
    {
        ExecutorService ThreadPool = Executors.newSingleThreadExecutor();
	int i= 0;
	while(i<100) {
            ThreadPool.execute(new HttpServer());
            i++;
	}
	ThreadPool.shutdown();
    }
}