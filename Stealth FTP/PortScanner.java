import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * Port Scanner - Searching for open ports by given IP.
 * @author Aviv
 *
 */
public class PortScanner {
	
	
	public static void main(final String... args) {
		 long startTime = System.currentTimeMillis();
		  final String ip = "127.0.0.1";
		  final int timeout = 200;
		  ScanAll(ip, timeout, 1,10000);
		  long endTime   = System.currentTimeMillis();
          long totalTime = endTime - startTime;
          System.out.println("All scan took : "+totalTime/1000+" Seconds");
		}
	
	/**
	 * ScanAll - with given IP and timeout 10 threads each time checking ports. 
	 * @param ip - IP of checking
	 * @param timeout - timeout of socket.
	 * @throws IOException 
	 */
	public static String ScanAll(final String ip, final int timeout, int min, int max)  {
		File file = new File("Ports.txt");
		String path = file.getAbsolutePath().toString();
		System.out.println("Starting to scan "+ip);
		final ExecutorService es = Executors.newFixedThreadPool(10);
		 final List<Future<ScanResult>> futures = new ArrayList<>();
		 ArrayList<Integer> ports = new ArrayList<>();
		  for (int port = min; port <= max; port++) {
		    futures.add(portIsOpen(es, ip, port, timeout));
		  }
		  es.shutdown();
		  for (final Future<ScanResult> f : futures) {
		    try {
				if (f.get().getStatus()) {		
			        ports.add(f.get().getPort());
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    catch (ExecutionException e) {
				e.printStackTrace();
			}		    
		  }
		  
		 /// Writing to file
		  FileWriter fr = null;
		  try {
			fr = new FileWriter(file);
			for(int i=0; i<ports.size(); i++) {
				fr.write(ports.get(i)+"\n");
			}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  System.out.println("Done");
		  return path;
		  
	}
	/**
	 * CustomScan - Scanning for open ports by most known/used ports
	 * @param ip 
	 * @param threads - how many threads to use.
	 */
	public static String CustomScan(String ip, int threads) {
		File file = new File("Ports.txt");
		String path = file.getAbsolutePath().toString();
		System.out.println("Starting to scan "+ip);
		final ExecutorService es = Executors.newFixedThreadPool(threads);
		 final List<Future<ScanResult>> futures = new ArrayList<>();
		 ArrayList<Integer> ports2 = new ArrayList<>();
		 int[] ports = {20,21,23,25,53,67,68,69,80,110,123,137,138,139,143,161,162,179,389,443,445,636,989,990};
		  for (int i=0; i < ports.length; i++) {
		    futures.add(portIsOpen(es, ip, ports[i], 100));
		  }
		  es.shutdown();
		  for (final Future<ScanResult> f : futures) {
			    try {
					if (f.get().getStatus()) {
					  ports2.add(f.get().getPort());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			  }
		  
		/// Writing to file
		  FileWriter fr = null;
		  try {
			fr = new FileWriter(file);
			for(int i=0; i<ports2.size(); i++) {
				fr.write(ports2.get(i)+"\n");
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		  System.out.println("Done");
		  return path;
	}
	
	/**
	 * Checking if the port is open 
	 * @param es - threads method 
	 * @param ip 
	 * @param port
	 * @param timeout
	 * @return Object of ScanResult - (port, TRUE=OPEN/FALSE=CLOSE)
	 */
	
	/**
	 * Scanning for open ports as random.
	 * @param ip
	 */
	public static String RandomPort(String ip) {
        int port =80;
        Random random = new Random();  
        System.out.println("Starting to scan "+ip);
        File file = new File("Ports.txt");
        String path = file.getAbsolutePath().toString();
		final ExecutorService es = Executors.newFixedThreadPool(20);
		 final List<Future<ScanResult>> futures = new ArrayList<>();
		 ArrayList<Integer> ports = new ArrayList<>();
		  while(port <= 10000) {
			  port += random.nextInt(40);
		    futures.add(portIsOpen(es, ip, port, 100));
		  }
		  es.shutdown();
		  for (final Future<ScanResult> f : futures) {
		    try {
				if (f.get().getStatus()) {		
			        ports.add(f.get().getPort());
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    catch (ExecutionException e) {
				e.printStackTrace();
			}		    
		  }
		  
		 /// Writing to file
		  FileWriter fr = null;
		  try {
			fr = new FileWriter(file);
			for(int i=0; i<ports.size(); i++) {
				fr.write(ports.get(i)+"\n");
			}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  System.out.println("Done");
		  return path;
    }
	
	public static Future<ScanResult> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
		  return es.submit(new Callable<ScanResult>() {
		      @Override public ScanResult call() {
		        try {
		          Socket socket = new Socket();
		          socket.connect(new InetSocketAddress(ip, port), timeout);
		          socket.close();
		          return new ScanResult(port, true);
		        } catch (Exception ex) {
		          return new ScanResult(port, false);
		        }
		      }
		   });
		}

}
