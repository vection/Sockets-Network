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

#define SERVER "127.0.0.1"
#define ServerPort 8888 // Setting port
#define ClientPort 7777 // Client port.
#define RouterPort 8880 // Router port
#define LEN 512 // defining the length.

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
  int socket_fd, fsize,recv_len;
  struct sockaddr_in s_in, si_out;
  struct {char message[LEN]; int num;} msg;
  msg.num = 2;
  socket_fd = socket (AF_INET, SOCK_DGRAM, 0); // Creating UDP socket

  // Set UDP details
  memset((char *) &s_in,0, sizeof(s_in));  /* They say you must do this    */
  // Listen socket details.
  s_in.sin_family = AF_INET;
  s_in.sin_addr.s_addr = htonl(INADDR_ANY);    /* WILDCARD */
  s_in.sin_port = htons(ServerPort);
 
   // output of destination socket
  si_out.sin_family = AF_INET;
  si_out.sin_addr.s_addr = htonl(INADDR_ANY);    /* WILDCARD */
  si_out.sin_port = htons(RouterPort);
  //printf(", RECV_UDP , Local socket is:%s\n", (char *)&s_in.sin_family);
  fflush(stdout);
   
   // Bind the socket to port
  /*if(bind(socket_fd, (struct sockaddr *)&s_in, sizeof(s_in)) == -1) {
    die("bind");
  }*/
  while(1) 
  {
    printf("Enter message : ");
        gets(msg.message);
         
        //send the message
        if (sendto(socket_fd, &msg, sizeof(msg) , 0 , (struct sockaddr *) &s_in, sizeof(s_in))==-1)
        {
            die("sendto()");
        }
         
        //receive a reply and print it.
        //try to receive some data, this is a blocking call
        if (recvfrom(socket_fd, &msg, sizeof(msg), 0, (struct sockaddr *) &si_out, fsize) == -1)
        {
            die("recvfrom()");
        }
        puts(msg.message);
        fflush(stdout);
  }/*
  while(1) {
    printf("Enter message:");
    gets(msg.message);
    sendto(socket_fd,&msg,sizeof(msg),0,(struct sockaddr *)&s_in,
                  sizeof(s_in));
    printf("Waiting for data\n");
    fflush(stdout);
    fsize = sizeof(si_out);
    // Recive packet
    if(recv_len = recvfrom(socket_fd,&msg,sizeof(msg),0, (struct sockaddr *) &si_out, fsize) == -1) 
    {
        die("recvfrom()");
    }
    // printsin(&si_out, "UDP-SERVER:", "Local socket is:");
    // Printing details of packet
    printf("Received packet from %s:%d\n", inet_ntoa(si_out.sin_addr), ntohs(si_out.sin_port));
    
    fflush(stdout);
  }*/
  return 0;
}
