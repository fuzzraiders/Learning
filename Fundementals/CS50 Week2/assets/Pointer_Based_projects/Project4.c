/* 4: Dynamic Memory Allocation

--> Concepts: malloc, pointers, dynamic arrays
--> Project: Program that creates a dynamic array based on user input and displays it.

*/
#include <stdio.h>
#include <stdlib.h>

int main() 
{
    int *ptr, n;

    printf("Enter number of elements: ");
    scanf("%d", &n);

    ptr = (int*)malloc(n * sizeof(int));

    for(int i=0;i<n;i++)
        scanf("%d", &ptr[i]);

    for(int i=0;i<n;i++)
        printf("%d ", ptr[i]);

    free(ptr);
    return 0;
}
