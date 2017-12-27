#include <sys/types.h>
#include <netinet/in.h>
#include <inttypes.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <strings.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>


#define Port 8888 // Setting port
/*
void printsin(struct sockaddr_in *sin, char *pname, char* msg) {
  printf("%s\n", pname);
  printf("%s :%s:%d", msg,inet_ntoa(si_out.sin_addr), ntohs(si_out.sin_port));
}*/

void printsin(struct sockaddr_in *s, char *pname, char *msg) {

  printf("%s\n", pname);
  printf("%s ", msg);
  printf("IP:%s port: %d", inet_ntoa(s->sin_addr), s->sin_port);
  printf("\n");
}

// Killing program
void die(char *s)
{
    perror(s);
    exit(1);
}

int main(int argc, char *argv[])
{
  int socket_fd, fsize;
  struct sockaddr_in s_in, si_out;
  struct { char head; long  body; char tail; char name[10];} msg;

  
  socket_fd = socket (AF_INET, SOCK_DGRAM, 0); // Creating UDP socket

  // Set UDP details
  memset((char *) &s_in,0, sizeof(s_in));  /* They say you must do this    */

  s_in.sin_family = AF_INET;
  s_in.sin_addr.s_addr = htonl(INADDR_ANY);    /* WILDCARD */
  s_in.sin_port = htons(Port);

 
   
  //printf(", RECV_UDP , Local socket is:%s\n", (char *)&s_in.sin_family);
  //fflush(stdout);
   
   // Bind the socket to port
  if(bind(socket_fd, (struct sockaddr *)&s_in, sizeof(s_in)) == -1) {
    die("bind");
  }
  while(1) {
    printf("Waiting for data\n"); 
    fflush(stdout);
    fsize = sizeof(si_out);
    // Recive packet
    recvfrom(socket_fd,&msg,sizeof(msg),0, (struct sockaddr *) &si_out, fsize);
     printsin(&si_out, "UDP-SERVER:", "Local socket is:");
    // Printing details of packet
    printf("Received packet from %s:%d\n", inet_ntoa(si_out.sin_addr), ntohs(si_out.sin_port));
     printf("Got data :%c: :%ld: :%c:\n",msg.head,(long) msg.body,msg.tail);
    
    printf("Your name is: %s\n", msg.name);    
    fflush(stdout);
  }
  return 0;
}
