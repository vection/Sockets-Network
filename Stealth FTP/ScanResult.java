
public final class ScanResult {
  private final int port;
  private final boolean isOpen;
  
  public ScanResult(int port, boolean isOpen) {
	  this.port = port;
	  this.isOpen = isOpen;
  }
 
  public int getPort() {
	  return this.port;
  }

public boolean getStatus() {
	if(this.isOpen)
	     return true;
	else 
		return false;
}

}