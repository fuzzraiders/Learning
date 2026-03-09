/* 1: Swap Two Numbers

--> Concepts: Pointers, function arguments by reference
--> Project: Program where the user enters two numbers and swaps them using pointers.

*/

#include <stdio.h>

void swap(int *a, int *b) 
{
    int temp;
    temp = *a;
    *a = *b;
    *b = temp;
}

int main() {
    int x, y;
    printf("Enter two numbers: ");
    scanf("%d %d", &x, &y);

    swap(&x, &y);

    printf("x = %d, y = %d\n", x, y);
    return 0;
}
