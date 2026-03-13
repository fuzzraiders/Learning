![header](assets/badges/learning-header.svg)

![metadata](assets/badges/learning-metadata.svg)

![division](assets/badges/learning-division.svg)


# 📌 Overview

> **“Memory is just a bunch of bytes, and a pointer is simply the address of one of those bytes."**
> — Mr.David J. Malan

This write-up covers **CS50x Week 4 – Memory**, one of the most important weeks in the course.

Until now, most of our programs worked without thinking deeply about **where variables are stored inside the computer**. 

Week 4 introduces powerful concepts that allow programmers to work **closer to the hardware level**.

Key ideas include:

* Representing numbers using **hexadecimal**
* Understanding **memory and pointers**
* Navigating memory with **pointer arithmetic**
* Understanding how **strings exist in memory**
* Allocating memory manually using **`malloc`**
* Detecting memory errors using **Valgrind**
* Understanding **garbage values and overflow**

These concepts are fundamental because they explain **how programs store, access, and manipulate data in memory**.

---

# 📚 Table of Contents

1. [Hexadecimal](#1️⃣-hexadecimal)
2. [Memory](#2️⃣-memory)
3. [Pointers](#3️⃣-pointers)
4. [Pointer Arithmetic](#4️⃣-pointer-arithmetic)
5. [Strings](#5️⃣-strings)
6. [Copying Strings & `malloc`](#6️⃣-copying-strings--malloc)
7. [Valgrind](#7️⃣-valgrind)
8. [Garbage Values](#8️⃣-garbage-values)
9. [Overflow](#9️⃣-overflow)
10. [`scanf`](#🔟-scanf)
11. [What This Week Teaches](#-what-this-week-teaches)
12. [Conclusion](#-conclusion)

---

# 1️⃣ Hexadecimal

In computer science, several number systems are used.

### Denary

Denary is **base 10**, the number system we normally use.

It contains ten digits:

```
0 1 2 3 4 5 6 7 8 9
```

---

### Binary

Binary is **base 2**, which computers use internally.

It contains only two digits:

```
0 and 1
```

Every piece of data inside a computer is ultimately represented using **binary**.

---

### Hexadecimal

Hexadecimal, often called **hex**, is another number system widely used in computer science.

It contains **16 symbols**:

```
0 1 2 3 4 5 6 7 8 9 A B C D E F
```

The letters represent numbers:

```
A = 10
B = 11
C = 12
D = 13
E = 14
F = 15
```

---

### Hex Numbers Beyond F

After F (15 in decimal), hex continues as:

```
10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F ...
```

💡 Notice something tricky:

If you glance at the hex number `10`, you might wonder:

* Is it **10 in decimal**?
* Or is it **16 in decimal** (because in hex, 10 = 16 in decimal)?

This is **why hexadecimal numbers can be ambiguous at a glance**.

---

### Hexadecimal Notation with `0x`

To avoid confusion, programmers **prefix hex numbers with `0x`**:

![image](assets/cs50Week4%200x%20prefix.png)

Here’s what each part means:

* `0` → just a placeholder, part of the prefix
* `x` → indicates that **what follows is a number in hexadecimal**

So when you see `0x1F`, you immediately know **this is a hex number, not decimal**.


**In short: `0x` = “Hey computer, read this as hexadecimal!”**

---

### Binary → Hex Example

| Denary | Binary | Hex |
| ------ | ------ | --- |
| 0      | 0000   | 0   |
| 1      | 0001   | 1   |
| 2      | 0010   | 2   |
| 3      | 0011   | 3   |
| 4      | 0100   | 4   |
| 5      | 0101   | 5   |
| 6      | 0110   | 6   |
| 7      | 0111   | 7   |
| 8      | 1000   | 8   |
| 9      | 1001   | 9   |
| 10     | 1010   | A   |
| 11     | 1011   | B   |
| 12     | 1100   | C   |
| 13     | 1101   | D   |
| 14     | 1110   | E   |
| 15     | 1111   | F   |

Hexadecimal is useful because **large binary numbers can be represented with fewer digits**.

---

# 2️⃣ Memory

Memory is where **all the program's data lives while it runs**.

* Imagine RAM as a **giant grid of tiny boxes**.


![image](assets/memory%20visu.png)


* Each box is **1 byte**, capable of holding a smal
l piece of data.
* Different types of data take up different amounts of space:

| Type   | Typical Size | Example |
| ------ | ------------ | ------- |
| char   | 1 byte       | 'A'     |
| int    | 4 bytes      | 50      |
| float  | 4 bytes      | 3.14    |
| double | 8 bytes      | 3.14159 |

💡 Key idea: **every variable occupies space in memory**, and the computer keeps track of its **memory address**.

---


### Memory Layout

Memory is divided into **sections**:

* **Stack** – stores local variables and function calls
* **Heap** – stores dynamic memory from `malloc`
* **Free memory** – unused space available to the program

Example:

```
+------------------+    <- High addresses
|      Stack       |
|------------------|
| local variable n |
| pointer p        |
+------------------+
|                  |
|      Heap        |    <- malloc() data
|------------------|
| copied strings   |
+------------------+
|                  |
|      Free        |
+------------------+    <- Low addresses
```

Example: pointer and string in memory:

```
Stack:
0x1000 n = 50
0x1004 p → 0x1000

Heap:
0x2000 'H'
0x2001 'e'
0x2002 'l'
0x2003 'l'
0x2004 'o'
0x2005 '\0'
```

* **Stack** stores local variables and function calls.
* **Heap** stores dynamically allocated memory via `malloc`.
* **Pointers** hold addresses of variables and can point to either stack or heap.
* **Strings/arrays** occupy contiguous memory locations.

> Understanding memory is crucial because **pointers, arrays, strings, and even user input all live somewhere in RAM**.

---

# 3️⃣ Pointers

Don't get scared about the **monster called pointers**, because they are actually quite simple once you understand them — **and David J. Malan makes them as easy as possible to understand in this lecture.**

### What is a pointer?

A **pointer** is a variable that **stores the memory address of another variable**.

Some programming tasks are easier to perform using pointers because they allow us to work **directly with memory locations**.

---

### Example

```c
int n = 50;
int *p = &n;

printf("%p\n", p);
```

Breaking it down:

```c
int n = 50;
```

This creates a variable `n` that stores the value `50`. Every variable exists **somewhere in memory**, for example:

```
n → 0x123
```

---

### Creating a Pointer

```c
int *p = &n;
```

![image](assets/Pointer1.png)

Here:

* `*p` means **p is a pointer to an integer**

* `&n` means **the address of n**

So the pointer `p` stores the **memory address of n**.

![image](assets/Pointer.png)

---

### Memory Representation

```
Address        Value
0x123      50
```

After assigning the pointer:

```
p = &n
```

Memory might look like this:

```
n → 50
p → 0x123
```

So `p` **points to the location of n in memory**.

---

# 4️⃣ Pointer Arithmetic

Pointers allow us to **move through memory**.

This is possible because array elements are stored **next to each other in memory**.

Example:

```c
int numbers[3] = {10, 20, 30};
```

Memory might look like this:

```
Address        Value
0x1000         10
0x1004         20
0x1008         30
```

Each `int` typically uses **4 bytes**, which is why the addresses increase by 4.

---

### Using a Pointer

```c
int *p = numbers;
```

This means `p` points to the **first element of the array**.

```
p → 0x1000
```

If we add 1:

```c
p + 1
```

The pointer moves to the **next integer**, not just one byte:

```
p + 1 → 0x1004
```

Example:

```c
printf("%i\n", *(p + 1));
```

Explanation:

* `p + 1` moves to the next element
* `*` dereferences the pointer

Result:

```
20
```

Pointer arithmetic lets us **walk through arrays using memory addresses**.

---

# 5️⃣ Strings

In C, a **string is actually an array of characters**.

Example:

```c
char name[] = "Emma";
```

In memory this looks like:

![image](assets/string1.png)

The last character `\0` is called the **null terminator**, which tells the program **where the string ends**.

Memory layout:

```
Address        Value
0x2000         'H'
0x2001         'I'
0x2002         '!'
0x2003         '\0'
```

Each character occupies **1 byte**.

---

# 6️⃣ Copying Strings & `malloc`

A common mistake is copying **pointers instead of the actual data**.

Example:

```c
char *s = "hi!";
char *t = s;
```


**You can visualize the above code as follows:**

![image](assets/copy&malloc.png)



Both variables point to **the same memory location**:

```
s → hi!
t → hi!
```

Changing one affects both.

---

### Correct Way Using `malloc`

Allocate new memory:

```c
char *t = malloc(strlen(s) + 1);
```

`+1` is needed for the **null terminator**.

Then copy the string:

```c
strcpy(t, s);
```

Now `s` and `t` exist in **different memory locations**.

---

# 7️⃣ Valgrind

Manual memory management can lead to mistakes like:

* accessing invalid memory
* forgetting to free memory
* reading uninitialized values

Valgrind detects these problems:

```bash
valgrind ./program
```

It checks for:

* memory leaks
* invalid reads/writes
* uninitialized variables

Valgrind is essential for **low-level C programming**.

---

# 8️⃣ Garbage Values

Sometimes variables contain **random leftover data**:

```c
int x;
printf("%i\n", x);
```

Output might be:

```
3829104
```

This happens because the variable was **never initialized**.

Best practice:

```c
int x = 0;
```

---

# 9️⃣ Overflow

Overflow occurs when a variable **exceeds its maximum value**.


# A heap overflow is when you overflow the heap, touching areas of memory you are not supposed to.

**A stack overflow is when too many functions are called, overflowing the amount of memory available.**

Both of these are considered buffer overflows.

Example:

```
maximum value + 1 → overflow
```

When overflow happens, the value **wraps around** and becomes incorrect.

Consequences:

* incorrect calculations
* program crashes
* security vulnerabilities

---

# 🔟 `scanf`

`scanf` allows programs to **read user input**:

```c
int age;
scanf("%i", &age);
```

Notice the `&`:

```
&age
```

This tells the program **where in memory to store the input**.

Example:

```
User enters: 25
```

Memory becomes:

```
age → 25
```

Without `&`, `scanf` would not know **where to store the value**.

---

# 🧠 What This Week Teaches

CS50 Week 4 shows **how computers manage memory**.

Key lessons:

* Hexadecimal represents memory addresses
* Memory stores every variable
* Pointers store addresses of variables
* Pointer arithmetic allows navigation through memory
* Strings are arrays of characters
* `malloc` allows dynamic memory allocation
* Valgrind helps detect memory errors
* Uninitialized variables contain garbage values
* Overflow occurs when limits are exceeded
* `scanf` requires memory addresses to store input

These concepts move programming from **just writing code** to **understanding how the computer actually works**.

---

# 📌 Conclusion

Week 4 of CS50 introduces one of the most powerful ideas in programming: **direct memory management**.

Through pointers and dynamic memory allocation, programmers can:

* control how memory is used
* write efficient programs
* understand how data structures work internally

Understanding memory is a crucial step toward advanced topics such as:

* data structures
* operating systems
* system programming
* performance optimization

> “Ultimately, programming is about solving problems.”
> — David J. Malan

Week 4 reinforces that solving problems in programming often requires understanding **how computers manage memory behind the scenes**.

![disclaimer](assets/badges/fuzzraiders-disclaimer.svg)

# Author: [QQQ](#)

![Ownership](assets/badges/fuzzraiders-Ownership.svg)


