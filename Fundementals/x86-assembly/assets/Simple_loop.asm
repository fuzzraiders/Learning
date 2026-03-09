section .text
global _start

_start:
    mov rcx, 5

loop_start:
    dec rcx
    jnz loop_start

    mov rax, 60
    xor rdi, rdi
    syscall
