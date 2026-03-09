section .text
global _start

_start:
    mov rax, 5
    mov rbx, 7
    add rax, rbx      ; rax = 12

    mov rax, 60
    xor rdi, rdi
    syscall
