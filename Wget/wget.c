#include <sys/types.h>
#include <unistd.h>          
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <signal.h>    
#include <sys/socket.h>
#include <sys/un.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <netinet/tcp.h>

#define PORT 80
#define BUFFER_SIZE 1024

int main(int argc, char* argv[])
{ 
  // Defien variables
  struct addrinfo* res;
  char* hostname;
  char* hostaddr;
  struct sockaddr_in* saddr;
  int sock; 
  struct sockaddr_in s_out; 
  int count;
  char value;
  char* finalValue;
  char* protocol;
  char* host;
  char* port;
  char* path;
  char buffer[BUFFER_SIZE];

  // Check if hostname was sent
  if (argc != 2) {
    perror("Usage: net_client.o <hostname>\n");
    exit(1);
  }

  // Get hostname
  hostname = argv[1];
  
  // Get address by hostname string
  if (0 != getaddrinfo(hostname, NULL, NULL, &res)) {
    fprintf(stderr, "Error in resolving hostname %s\n", hostname);
    exit(1);
  }

  // Resolve host address as string
  saddr = (struct sockaddr_in*)res->ai_addr;
  hostaddr = inet_ntoa(saddr->sin_addr);

  printf("Trying to get information from hostname: %s\n", hostname);
  
  sock = socket(AF_INET, SOCK_STREAM, 0);
  if (sock < 0)
    { perror ("Error opening channel");
      close(sock);
      exit(1);
    }
      

  // Define properties required to connect to the socket
  memset((char *) &s_out,0, sizeof(s_out));
  s_out.sin_family = AF_INET; 
  s_out.sin_addr.s_addr = inet_addr(hostaddr); 
  s_out.sin_port = htons(PORT);


  // Connect to the socket, if return 0 then succeeded, if not, print error
  if (connect(sock, (struct sockaddr *)&s_out, sizeof(s_out)) < 0)
    { perror ("Error while connecting");
      close(sock);
      exit(1);
    }
    
    write(sock, "GET /\r\n", strlen("GET /\r\n"));  
	bzero(buffer, BUFFER_SIZE);
	
	while(read(sock, buffer, BUFFER_SIZE - 1) != 0){
		fprintf(stderr, "%s", buffer);
		bzero(buffer, BUFFER_SIZE);
	}

	shutdown(sock, SHUT_RDWR); 
	close(sock); 

	return 0;
} 
