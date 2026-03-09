/*3: String Length Calculator

--> Concepts: Character pointers, string traversal

--> Project: Program to calculate the length of any input string using pointers.

*/
#include <stdio.h>

int main() 
{
    char str[100];
    printf("Enter a string: ");
    scanf("%s", str);

    char *ptr = str;
    int count = 0;

    while(*ptr != '\0') {
        count++;
        ptr++;
    }

    printf("Length = %d", count);
    return 0;
