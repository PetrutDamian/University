#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
 
struct data {
  int nr;
  int tries;
};
 
int getIdFromCookie() {
  int id;
  char str[200];
  strcpy(str,getenv("HTTP_COOKIE"));
  sscanf(str,"userKey=%d",&id);
  return id;
}
 
int getNumberFromQueryString() {
  char buffer[2048];
  int nr;
  strcpy(buffer, getenv("QUERY_STRING"));
  sscanf(buffer, "nr=%d", &nr);
  return nr;  
}
 
int init() { 
  int r, id;
  int code;
  char filename[100];
  struct data d;
  
  srand(getpid());
  r = random() % 100;
 
  do {
    id = random();
    sprintf(filename, "/tmp/%d.txt", id);
    code = creat(filename, O_CREAT | O_EXCL | 0600);
  }
  while (code < 0);
 
  d.nr = r;
  d.tries = 0;
  write(code, &d, sizeof(d));
  close(code);
  
  return id;
}
 
void destroy(int id) {
  char filename[100];
  sprintf(filename, "/tmp/%d.txt", id);
  unlink(filename);
}
 
int getNumberFromFile(int id) {
  char filename[100];
  int fd;
  
  sprintf(filename, "/tmp/%d.txt", id);
  struct data d;
  fd = open(filename, O_RDWR);
  if (fd < 0)
    return -1;
  read(fd, &d, sizeof(d));
  d.tries++;
  lseek(fd, 0, SEEK_SET);
  write(fd, &d, sizeof(d));  
  close(fd);
  return d.nr;
}
 
int getNoOfTries(int id) {
  char filename[100];
  int fd;
  sprintf(filename, "/tmp/%d.txt", id);
  struct data d;
  fd = open(filename, O_RDONLY);
  read(fd, &d, sizeof(d));
  close(fd);
  return d.tries;    
}
 
int isNewUser() {
  int userID=-1;
  char str[200]=""; 
  if(getenv("HTTP_COOKIE")==NULL)
	return 1;
  if (strcmp("",getenv("HTTP_COOKIE"))==0)
	return 1;
   	
  strcpy(str,getenv("HTTP_COOKIE"));
  sscanf(str,"userKey=%d",&userID);
  if (userID==-1)
	return 1;
  return 0;
}
 
void printForm() {//formularul nu mai are inputul hidden cu id-ul
  printf("<form action='http://www.scs.ubbcluj.ro/~pair2588/cgi-bin/problema8.cgi' method='get'>\n");
  printf("Nr: <input type='text' name='nr'><br>\n");
  printf("<input type='submit' value='Trimite'>\n");
  printf("</form>");
}
 
int main() {
  int id, status;
  printf("Content-type: text/html\n");
  if (isNewUser()) {
    id = init();//daca e user nou trimitem un cookie cu id-ul userului in raspuns    
    status = 0;
    printf("Set-Cookie: userKey=%d\n\n",id);//session cookie  
  }
  else {

    int nr, nr2;//daca user-ul este unul nou
    id = getIdFromCookie();//luam id-ul din cookie
    nr = getNumberFromQueryString();
    nr2 = getNumberFromFile(id);
    if (nr2 == -1)
      status = 1;
    else if (nr == nr2){//daca a ghicit atunci resetam cookie-ul
      status = 2;
      printf("Set-Cookie: userKey=-1\n"); 
	}
    else if (nr < nr2)
      status = 3;
    else if (nr > nr2)
       status = 4;
    printf("\n"); 
               
  }

  printf("<html>\n<body>\n");
  
  switch (status) {
    case 0 : printf("Ati inceput un joc nou.<br>\n"); printForm(); break;
    case 1 : printf("Eroare. Click <a href= 'http://www.scs.ubbcluj.ro/~pair2588/cgi-bin/problema8.cgi'>here</a> for a new game!"); break;
    case 2 : printf("Ati ghicit din %d incercari. Click <a href='http://www.scs.ubbcluj.ro/~pair2588/cgi-bin/problema8.cgi'>here</a> for a new game!", getNoOfTries(id)); destroy(id); break;
    case 3 : printf("Prea mic!<br>\n"); printForm(); break;
    case 4 : printf("Prea mare!<br>\n"); printForm();break;
  }
  
  printf("</body></html>\n");

}


