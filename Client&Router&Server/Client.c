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

#define RouterPort 8880 // defining the port.
#define ServerPort 8888 // Setting port
#define ClientPort 7777 // Client port.
#define LEN 512 // Defining length.

void die(char *s)
{
    perror(s);
    exit(1);
}
int main(int argc, char *argv[])
{
  int socket_fd;
  struct sockaddr_in  dest, s_in;
  struct hostent *hostptr;
  struct {char message[LEN]; int num; } msg;
  
  socket_fd = socket (AF_INET, SOCK_DGRAM, 0); // Openning UDP socket.
   memset((char *) &s_in,0, sizeof(s_in));  /* They say you must do this    */
// Listening...
  s_in.sin_family = AF_INET;
  s_in.sin_addr.s_addr = htonl(INADDR_ANY);    /* WILDCARD */
  s_in.sin_port = htons(ClientPort);
  
  // Output socket
   memset((char *) &dest,0, sizeof(dest)); // zero out the structure 
   hostptr = gethostbyname(argv[1]); // Getting the localhost when we run the program
   dest.sin_family =  AF_INET; // IP
   memcpy(hostptr->h_addr, (char *)&dest.sin_addr, hostptr->h_length); // Applying address in socket
   dest.sin_port = htons(RouterPort); // Setting port
  while(1) 
  {
        printf("Enter message : ");
        gets(msg.message);
        msg.num = 1;
         
        //send the message
        if (sendto(socket_fd, &msg, sizeof(msg) , 0 , (struct sockaddr *) &dest, sizeof(dest))==-1)
        {
            die("sendto()");
        }
         
        //receive a reply and print it
        //try to receive some data, this is a blocking call
        if (recvfrom(socket_fd, &msg, sizeof(msg), 0, (struct sockaddr *) &s_in, sizeof(s_in)) == -1)
        {
            die("recvfrom()");
        }
        puts(msg.message);
  }
  return 0;
}