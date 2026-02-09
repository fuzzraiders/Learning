<div align="left">

<img src="https://img.shields.io/badge/FuzzRaiders_Team_Member-0a66ff?style=flat-square&logo=github" />
<img src="https://img.shields.io/badge/Rootx-0f172a?style=flat-square" />
<img src="https://img.shields.io/badge/🎯%20Role-Reverse Engineering-1e293b?style=flat-square" />
<!-- <img src="https://img.shields.io/badge/📜%20Certification-CEDP_(CyberWarFare_Labs)-334155?style=flat-square" /> -->
<!-- <img src="https://img.shields.io/badge/🟢%20Status-In_Progress-16a34a?style=flat-square" /> -->

</div>

## Environment

<div align="left">

<!-- ![Platform: Hack%20The%20Box](https://img.shields.io/badge/Platform-Hack%20The%20Box-darkgreen)<br> -->
![scope: controlled and non-destructive](https://img.shields.io/badge/scope-controlled%20and%20non%20destructive-red)<br>
![Difficulty: Easy](https://img.shields.io/badge/Difficulty-Easy-blue)<br>

</div>

# Addresses in C: Memory, scanf(), and Segmentation Faults

---

## Overview

This document explains how **addresses work in C**, why they matter, and how incorrect address usage leads to a **segmentation fault**.

The explanation is based on a real debugging session using **GDB**, where a program crashes due to improper use of `scanf()`.

This is not an exploit write-up.
This is **address-level reasoning**, which is the foundation of exploit development.

---

## Core Idea

In C, everything important happens at an **address**.

* Variables live at memory addresses
* The CPU reads and writes using addresses
* Functions like `scanf()` operate on addresses, not values

If a program tries to read or write to an invalid address, the operating system stops it with **SIGSEGV**.

---

## Code Under Analysis

```c
int a;
int b;

printf("Number 1: ");
scanf("%i", a);

printf("Number 2: ");
scanf("%i", b);
```

At first glance, the code looks correct.
It compiles and runs — but crashes at runtime.

---

## Common Misunderstandings

* **“scanf stores input into a variable”**
  ❌ No. `scanf()` writes to an address.

* **“If it compiles, it must be correct”**
  ❌ C does not enforce memory safety.

* **“Segmentation faults are random”**
  ❌ They are precise reactions to invalid memory access.

---

## What Actually Happens in Memory

Step by step:

1. `int a;` reserves space on the **stack**
2. `a` contains an **uninitialized value**
3. That value is treated as an **address**
4. `scanf()` attempts to write input to that address
5. The address is invalid
6. The kernel raises **SIGSEGV**

This line is the root cause:

```c
scanf("%i", a);
```

Because it is effectively interpreted as:

```c
scanf("%i", 0x????????);
```

---

## Why the Address Is Wrong

* `a`  → value stored in variable `a`
* `&a` → address of variable `a`

`scanf()` requires the **address** of where to store data.

Correct usage:

```c
scanf("%i", &a);
scanf("%i", &b);
```

---

## GDB Perspective

From the debugging session:

* Breakpoint hits at `main`
* Program executes `printf`
* User input is accepted
* Crash occurs immediately after `scanf`
* Fault address points inside `libc.so.6`

This confirms:

* The crash happens during a **write operation**
* The destination address is invalid
* This is an address misuse, not a logic error

Segmentation faults are **address violations**, not bugs in `scanf()`.

---

## Memory Context

Relevant memory regions involved:

* **Stack** → local variables (`a`, `b`)
* **libc** → implementation of `scanf`
* **CPU registers** → passing arguments to functions

Passing an incorrect address causes libc to write into memory the program does not own.

---

## Concept Practiced

This example demonstrates:

* Variables are meaningless without addresses
* One missing `&` breaks memory safety
* C trusts the programmer completely
* Segmentation faults are predictable outcomes

This exact mistake is the starting point of:

* buffer overflows
* arbitrary writes
* memory corruption bugs

---

## Reflection

* Address usage became concrete, not abstract
* GDB made the crash understandable
* The importance of `&` is no longer theoretical
* Memory bugs stopped feeling “random”

---

## Summary

This program crashed because `scanf()` was given a **value instead of an address**.
Understanding addresses turns segmentation faults from confusion into clarity.

---

This work is part of FuzzRaiders’ structured hands-on training program, where learning is guided, documented, and continuously reviewed to build practical skills, disciplined methodology, and real-world security readiness.

This work is part of FuzzRaiders’ structured hands-on training program, where learning is guided, documented, and continuously reviewed to build practical skills, disciplined methodology, and real-world security readiness.

## <h1 style="color:red;">Author: RootX  [LinkedIn : Maslah](https://www.linkedin.com/in/maslah-abdi-b773a6382/)</h1>

