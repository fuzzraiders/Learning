/* 5: Simple Student Record System

--> Concepts: Structure pointers, memory handling
--> Project: Store and display student details using structure pointers.

*/
#include <stdio.h>

struct Student {
    int id;
    char name[20];
};

int main() 
{
    struct Student s;
    struct Student *ptr = &s;

    printf("Enter ID: ");
    scanf("%d", &ptr->id);

    printf("Enter Name: ");
    scanf("%s", ptr->name);

    printf("ID: %d\nName: %s", ptr->id, ptr->name);
    return 0;
