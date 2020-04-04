#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <string.h>

int hexatoint(char c) {
  if ((c>='a') && (c<='f'))
    return c - 'a' + 10;
  if ((c>='A') && (c<='F'))
    return c - 'A' + 10;
  return c - '0';
}
 
void decode(char *s) {
  int i = 0, j;
  while (s[i] != 0) {
    if (s[i] == '+')
      s[i] = ' ';
    if (s[i] == '%') {
      char c = 16 * hexatoint(s[i+1]) + hexatoint(s[i+2]);
      s[i] = c;
      j = i + 1;
      do {
        s[j] = s[j + 2];
        j++;
      } while (s[j] != 0);
    }
    i++;
  }  
}
 
int main() {
  int length =  strlen(getenv("QUERY_STRING"));//calculam lungimea stringului
  char* s = (char*)malloc(length+1);//alocam memorie
  strcpy(s,getenv("QUERY_STRING"));//copiem in s

  int index = 0;//stringul are formatul name=value , parsam dupa '='
  while(s[index]!='=')
	index++;
  index++;

  char* string = (char*)malloc(length-index+1);//string va contine doar
  strcpy(string,s+index);//partea de value
  free(s);
  
  printf("Content-type: text/html\n\n");
  printf("Original string: %s<br>\n", string);
  decode(string);
  printf("Decoded string: %s", string);
  free(string);
}
