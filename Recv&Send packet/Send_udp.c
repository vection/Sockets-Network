#include <sys/types.h>
#include <netinet/in.h>
#include <inttypes.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <strings.h>
#include <unistd.h>
#include <string.h>
//#include<winsock2.h>

#define Port 8888 // defining the port.

int main(int argc, char *argv[])
{
  int socket_fd;
  struct sockaddr_in  dest;
  struct hostent *hostptr;
  struct { char head; long body; char tail; char name[10];} msgbuf;

  socket_fd = socket (AF_INET, SOCK_DGRAM, 0); // Openning UDP socket.
  memset((char *) &dest,0, sizeof(dest)); // zero out the structure 
  hostptr = gethostbyname(argv[1]); // Getting the localhost when we run the program
  dest.sin_family =  AF_INET; // IP
  memcpy(hostptr->h_addr, (char *)&dest.sin_addr, hostptr->h_length); // Applying address in socket
  dest.sin_port = htons(Port); // Setting port
   
  // Any message :
  msgbuf.head = '<';
  msgbuf.body = htonl(getpid()); /* IMPORTANT! */
  msgbuf.tail = '>';
  printf("Enter your name\n");
  gets(msgbuf.name);

// Sending socket
  sendto(socket_fd,&msgbuf,sizeof(msgbuf),0,(struct sockaddr *)&dest,
                  sizeof(dest));

  return 0;
}