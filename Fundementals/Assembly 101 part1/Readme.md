![header](assets/badges/learning-header.svg)

![metadata](assets/badges/learning-metadata.svg)

![division](assets/badges/learning-division.svg)

# 🧠 x86 Assembly — Part 1 (Foundations)
## 📌 Overview
“Assembly is where you stop guessing and start understanding what the computer is actually doing under the hood.”

This write-up covers **Part 1 of the Assembly 101 course provided by TCM Academy**, focusing on core low-level concepts required before moving into exploit development.

Until now, most programming—both high-level and low-level languages—hides how things work internally. Conversely, assembly removes that abstraction and shows:

- How the CPU executes instructions  
- How data is stored in registers and memory  
- How programs actually run step-by-step  

These concepts are fundamental for:
- Exploit development  
- Reverse engineering  
- Debugging  

## ⚠️ **Note:** This write-up is not just informational. Throughout the sections, questions are provided to help test your understanding and reinforce the concepts learned.

---
## 📚 Table of Contents

1. [Course Intro](#1️⃣-course-intro)  
2. [Intro to Computer Engineering](#2️⃣-intro-to-computer-engineering)  
3. [8086 Development Setup](#3️⃣-8086-development-setup)  
4. [Registers, MOV and Interrupts](#4️⃣-registers-mov-and-interrupts)  
5. [Interacting With Memory](#5️⃣-interacting-with-memory)  
6. [Arithmetic Operations](#6️⃣-arithmetic-operations)  
7. [Accepting User Input](#7️⃣-accepting-user-input)  
8. [Conditional Statements](#8️⃣-conditional-statements)  
9. [Loops](#9️⃣-loops)  
10. [Functions](#🔟-functions)
 


---

## 1️⃣ Course Intro
What Is This Course?

The Assembly 101 course by TCM Security introduces x86 assembly and focuses on understanding how programs actually run at the CPU level.

What You Learn
How instructions are executed step-by-step
How low-level code differs from high-level languages
Why understanding assembly is important for security
Key Idea

High-level code:

x = a + b

Assembly breaks it down:

mov ax, a
add ax, b

You control every instruction the CPU executes.

💡 Question: Why does breaking a simple operation into multiple instructions help you better understand program execution?



---



## 2️⃣ Intro to Computer Engineering

### Numbering Systems

**Decimal (Base 10):**


0 1 2 3 4 5 6 7 8 9
**(human-friendly)**


**Hexadecimal (Base 16):**


0 1 2 3 4 5 6 7 8 9 A B C D E F


![image](<assets/Hex & decimal.webp>)



💡 Hex is commonly used to represent memory addresses.

**Binary (Base 2):**


0 1
**(computer-native)**



**💡 Question:** Convert the decimal number 27 to binary and hexadecimal.

---

### How Processors Work

CPU follows a cycle which looks like this:


![alt text](assets/cycle.webp)


Example flow:

```


Instruction → CPU → Registers → Result


```

**💡 Question:** What happens during the decode stage of the CPU instruction cycle?

---

### Intel 8086

- 16-bit processor  
- Uses registers and segmentation  
- Foundation of modern x86  

**💡 Question:** Name two features of the 8086 that are important for understanding modern x86 architecture.

---

## 3️⃣ 8086 Development Setup

Environment setup includes:
- Ubuntu VM  
- 8086 emulator  
- Required tools  

![alt text](assets/flow.webp)


**💡 Question:** Why is an emulator useful when learning assembly before working on real hardware?

---

## 4️⃣ Registers, MOV and Interrupts

### Registers

```

AX → Accumulator
BX → Base
CX → Counter
DX → Data

```

Think of registers like small, fast storage boxes:

```

AX = [ 5 ]
BX = [ 3 ]

````

**💡 Question:** If AX = 10 and BX = 5, what will BX hold after `mov bx, ax`?

---

### MOV Instruction

```asm
mov ax, 5  ; now the value of AX register is 5
mov bx, ax ; BX = AX, so BX = 5 and AX = 5
````

```
Before: BX = ?
After : BX = 5
```

**💡 Question:** Explain in your own words what the `mov` instruction does.

---

### Interrupts

```asm
mov ah, 4Ch
int 21h
```

Used for system operations like exiting programs.

**💡 Question:** What role does an interrupt play in assembly programming?

---

## 5️⃣ Interacting With Memory

### Memory Layout

```
+------------------+  ← High Address
|      Stack       |
|------------------|
| Local Variables  |
+------------------+
|                  |
|      Heap        |
|------------------|
| Dynamic Memory   |
+------------------+
|                  |
|      Free        |
+------------------+  ← Low Address
```

---

### Endianness

Little-endian (x86):

```
Value: 0x1234
Memory: 34 12
```

**💡 Question:** How would the value 0xABCD be stored in memory on a little-endian machine?

---

### Stack (LIFO) — Last-In-First-Out


           STACK (LIFO)

        +--------------+   ← Top (High Address)
        |      C       |   ← Last pushed
        +--------------+
        |      B       |
        +--------------+
        |      A       |   ← First pushed
        +--------------+   ← Bottom (Low Address)

        Push → ↑ (adds to top)
        Pop  → ↓ (removes from top)


**Think of it as a stack of plates, as shown in the following image:**

![alt text](assets/Last-in-first-out.webp)

**💡 Question:** If the stack currently holds A, B, C (C on top) and we push D, which element will be popped first?

---

### Memory Access

```
Address    Value
0x1000     10
0x1004     20
```

**💡 Question:** How can pointers be used to access the value at memory address 0x1004?

---

## 6️⃣ Arithmetic Operations

```asm
mov ax, 5
add bx, ax
sub ax, bx
mul bx
div bx
```

**💡 Question:** What happens to the Carry Flag when adding two numbers that exceed the register size?

---

### Flags

```
Carry Flag → overflow in addition
Borrow Flag → overflow in subtraction
```

**💡 Question:** Give an example of an operation that sets the Borrow Flag.

---

### Bitwise Logic

AND
OR  
XOR 

AND ( ∧ ) → Both must be true (1)
 Think: “I’ll go out only if it’s sunny AND I’m free.”

OR ( ∨ ) → At least one is true (1)
 Think: “I’ll go out if it’s sunny OR I’m free (or both).”

XOR ( ⊕ ) → Exactly one is true, not both
 Think: “I’ll go out if it’s sunny OR I’m free, but not both together.”


## AND ( ∧ )
Output is 1 only if both inputs are 1

| A | B | A AND B |
|---|---|---------|
| 0 | 0 |    0    |
| 0 | 1 |    0    |
| 1 | 0 |    0    |
| 1 | 1 |    1    |

---

## OR ( ∨ )
Output is 1 if at least one input is 1

| A | B | A OR B |
|---|---|--------|
| 0 | 0 |   0    |
| 0 | 1 |   1    |
| 1 | 0 |   1    |
| 1 | 1 |   1    |

---

## XOR ( ⊕ )
Output is 1 if inputs are different

| A | B | A XOR B |
|---|---|---------|
| 0 | 0 |    0    |
| 0 | 1 |    1    |
| 1 | 0 |    1    |
| 1 | 1 |    0    |

**💡 Question:** What result do you get if you XOR a register with itself?

---

## 7️⃣ Accepting User Input

Program reads input from the user:

```
User → Keyboard → Program → Memory
```

**💡 Question:** Why does assembly need to know the memory location when storing user input?

---

## 8️⃣ Conditional Statements

### CMP

```asm
cmp ax, bx
```

### JMP

```asm
jmp label
```

### Flow Example

```
cmp ax, bx
↓
equal? → YES → jump
        NO  → continue
```



for those of you who might be more visual when it comes to learning, here's a flow chart that represents the same exact program 

![alt text](assets/flowchart.webp)

**💡 Question:** If AX = 5 and BX = 5, will the program jump after `cmp ax, bx`? Explain.

---

## 9️⃣ Loops

```
Start → Execute → Check → Repeat
```

```asm
loop label
```

**💡 Question:** Explain how a `loop` instruction uses the CX register in x86 assembly.

---

### String Operations

```
LODS → Load data
STOS → Store data
```

**💡 Question:** What is the difference between `LODS` and `STOS` instructions?

---

## 🔟 Functions

Functions break code into reusable blocks:

```
Main → Function → Return
```

Benefits:

* Cleaner code
* Reusability
* Better structure

**💡 Question:** Why are functions important in assembly programs, even though code can be written sequentially?

---

## 🧠 What This Part Teaches

* How the CPU executes instructions
* How registers store data
* How memory is structured
* How programs control flow

---

## 📌 Conclusion

Part 1 builds the **foundation of assembly programming**.

After this, you can:

* Understand low-level execution
* Work with registers and memory
* Write basic assembly programs


## As a wise Person once said “Once you understand assembly, the computer stops being a black box.”


![disclaimer](assets/badges/fuzzraiders-disclaimer.svg)

# Author: [QQQ](#)

![Ownership](assets/badges/fuzzraiders-Ownership.svg)
