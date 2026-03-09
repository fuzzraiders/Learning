/* 2: Reverse an Array

--> Concepts: Pointer arithmetic, arrays with pointers
--> Project: Program that reverses an array entered by the user using pointers.

*/
#include <stdio.h>

int main() 
{
    int n;
    printf("Enter number of elements: ");
    scanf("%d", &n);

    int arr[n];
    for(int i=0;i<n;i++)
        scanf("%d", &arr[i]);

    int *start = arr;
    int *end = arr + n - 1;
    int temp;

    while(start < end) {
        temp = *start;
        *start = *end;
        *end = temp;
        start++;
        end--;
    }

    for(int i=0;i<n;i++)
        printf("%d ", arr[i]);
    return 0;
}
