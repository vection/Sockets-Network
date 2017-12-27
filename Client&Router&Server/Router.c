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


#define ServerPort 8888 // Setting port
#define ClientPort 7777 // Client port.
#define RouterPort 8880 // Router port
#define LEN 512


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
double r2() // rand fuction.
{
     srand(time(NULL));
     return(rand() % 2);
}

int main(int argc, char *argv[])
{
  int socket_fd, fsize, socket_fd2;
  struct sockaddr_in s_in, si_out, dest;
  struct {char message[LEN]; int num;} msg;
  socket_fd = socket (AF_INET, SOCK_DGRAM, 0); // Creating UDP socket

  // Set UDP details
  memset((char *) &s_in,0, sizeof(s_in));  /* They say you must do this    */
 // Listening 
  s_in.sin_family = AF_INET;
  s_in.sin_addr.s_addr = htonl(INADDR_ANY);    /* WILDCARD */
  s_in.sin_port = htons(RouterPort);

  //printf(", RECV_UDP , Local socket is:%s\n", (char *)&s_in.sin_family);
  //fflush(stdout);
   
   // Bind the socket to port
  if(bind(socket_fd, (struct sockaddr *)&s_in, sizeof(s_in)) == -1) {
    die("bind");
  }
  while(1) {
    printf("Router is ON\n"); 
    fflush(stdout);
    fsize = sizeof(si_out);
    // Recive packet
    recvfrom(socket_fd,&msg,sizeof(msg),0, (struct sockaddr *) &si_out, fsize);
     printf("Received packet from %s:%d\n", inet_ntoa(si_out.sin_addr), ntohs(si_out.sin_port));
    // Printing details of packet
    if(msg.num == 1) // If recive packet from client. 
    {
        double x;
        printf("From client:\n");
       printf("Received packet from %s:%d\n", inet_ntoa(si_out.sin_addr), ntohs(si_out.sin_port));
       printf("Got data from : %s", msg.message);
       socket_fd2 = socket (AF_INET, SOCK_DGRAM, 0); // Openning UDP socket.
       memset((char *) &dest,0, sizeof(dest)); // zero out the structure 
       dest.sin_family =  AF_INET; // IP
       dest.sin_port = htons(ServerPort); // Setting port
       printf("Enter x:\n");
       scanf("%lf", &x);
       if(r2() < x) 
       {
           msg.num = 2;
          sendto(socket_fd2,&msg,sizeof(msg),0,(struct sockaddr *)&dest,
                  sizeof(dest));
                  
          printf("Packet sent to server.");      
       }
    }
    else  if(msg.num == 2) // If recive packet from server.
    {
         printf("From Server:\n");
       printf("Received packet from %s:%d\n", inet_ntoa(si_out.sin_addr), ntohs(si_out.sin_port));
       socket_fd = socket (AF_INET, SOCK_DGRAM, 0); // Openning UDP socket.
       memset((char *) &dest,0, sizeof(dest)); // zero out the structure 
       dest.sin_family =  AF_INET; // IP
       dest.sin_port = htons(ClientPort); // Setting port
       dest.sin_addr.s_addr = htonl(INADDR_ANY);
       msg.num = 1;
       sendto(socket_fd,&msg,sizeof(msg),0,(struct sockaddr *)&dest,
                  sizeof(dest));
    }
    fflush(stdout);
  }
  return 0;
}
