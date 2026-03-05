![header](assets/badges/learning-header.svg)
![metadata](assets/badges/learning-metadata.svg)
![division](assets/badges/learning-division.svg)

👋 Introduction

# Hi, I’m _QQQ,_ a proud member of FuzzRaiders.

This is my first write-up, documenting my progress through CS50 Week 1 and my foundation in C programming. It reflects my focus on understanding how computers execute instructions at a low level and building strong programming discipline from the ground up, and I’m happy to share this journey and what I’ve learned so far.

## 📌 Overview

This write-up covers **CS50 Week 1**, where the focus shifts from visual programming to understanding how computers execute instructions at a low level using the C language.

Unlike high-level abstractions, C forces you to confront:

- How memory works
- How code becomes machine instructions
- Why precision and syntax matter
- How logic controls execution

Because computers operate strictly on defined instructions, even small mistakes in structure or types can break a program.

# In this write-up, we cover

- Computational thinking fundamentals
- Source code vs machine code
- Compilation workflow
- Header files and declarations
- Variables and static typing
- Conditionals and logical flow
- Loops and repetition
- Functions and abstraction

---

## 🛠 Core Concepts & Tools

The following core components define the C development workflow:

```
C language          → Structured low-level programming
gcc                 → Compilation into machine code
stdio.h             → Standard input/output functions
Terminal            → Program execution
Binary (0/1)       → CPU-level instruction format
```

---

## 🧭 Walkthrough

### 1️⃣ Computational Mindset

As emphasized by David J. Malan:

# “Computers are really just dumb machines.”

Computers:

- Do not think
- Do not assume
- Do not interpret intent
- Only execute what is explicitly written

C makes this reality obvious. Every variable, every type, every instruction must be explicitly defined.

This is where disciplined programming begins.

---

### 2️⃣ Source Code vs Machine Code

C programs start as **source code**, which is human-readable:

```c
#include <stdio.h>

int main(void)
{
    printf("Hello\n");
    return 0;
}
```

However, CPUs cannot understand this format directly.

They only execute **machine code**, represented in binary:

```
01010100 01101000 01100101 ...
```

The transformation process is:

```
Source Code → Compiler → Machine Code → CPU Execution
```

Without compilation (using `gcc`), the program cannot run.

---

### 3️⃣ Header Files & Declarations

C requires explicit declarations.

Example:

```c
#include <stdio.h>
```

This tells the compiler where functions like:

- `printf()`
- `scanf()`

are defined.

If omitted, the compiler does not recognize those functions.

This reinforces a core principle:

**The computer only knows what you explicitly tell it.**

---

## 📦 Variables & Data Types

### Static Typing

C is **statically typed**, meaning every variable must declare its type before use.

Correct:

```c
int age = 23;
```

Incorrect:

```c
age = 23; // Compilation error
```

The compiler enforces type discipline at build time.

---

### Common Data Types

| Type   | Typical Size | Purpose                   |
| ------ | ------------ | ------------------------- |
| char   | 1 byte       | Single character          |
| bool   | 1 byte       | True / False              |
| int    | 4 bytes      | Whole numbers             |
| float  | 4 bytes      | Decimal numbers           |
| double | 8 bytes      | Higher precision decimals |

⚠ Exact sizes may vary by architecture.

Understanding size matters because everything occupies memory.

---

## 🔀 Conditionals

Conditionals control decision-making logic.

```c
int x = 1;
int y = 2;

if (x > y)
{
    printf("x is greater\n");
}
else
{
    printf("x is NOT greater\n");
}
```

Execution flow:

- Evaluate condition
- If true → execute first block
- If false → execute `else` block

There is no guessing. Only boolean evaluation.

---

## 🔁 Loops & Repetition

Automation eliminates repetition.

Instead of:

```c
printf("Hi\n");
printf("Hi\n");
printf("Hi\n");
```

Use structured looping.

---

### While Loop

```c
int i = 0;

while (i < 3)
{
    printf("Hi\n");
    i++;
}
```

Process:

1. Check condition
2. Execute block
3. Update variable
4. Repeat

Stops only when condition becomes false.

---

### For Loop

More compact structure:

```c
for (int i = 0; i < 3; i++)
{
    printf("Hi\n");
}
```

Structure:

- Initialization
- Condition
- Update

All in one line.

---

## 🧩 Functions & Abstraction

Functions break problems into smaller reusable parts.

Example:

```c
#include <stdio.h>

void say_hello(void)
{
    printf("Hello!\n");
}

int main(void)
{
    say_hello();
}
```

Execution order:

- Program starts in `main()`
- `say_hello()` is called
- Function executes
- Control returns to `main()`

This demonstrates abstraction — separating logic into manageable components.

---

## 🧠 What This Week Teaches

- Computers only execute explicit instructions
- C enforces strict syntax and type rules
- Compilation bridges human-readable code and binary
- Memory and data types define program behavior
- Logic structures determine execution flow
- Breaking problems into smaller functions simplifies complexity

---

## 📌 Conclusion

CS50 Week 1 introduces programming discipline at a foundational level.

C does not hide how machines operate. It exposes:

- Memory structure
- Type constraints
- Execution flow
- Precision requirements

Individually, these concepts seem simple.

Together, they form the foundation of low-level programming and systems understanding.

# Clear logic, strict structure, zero assumptions.

![disclaimer](assets/badges/fuzzraiders-disclaimer.svg)

# Author: [QQQ](#)

![Ownership](assets/badges/fuzzraiders-Ownership.svg)
