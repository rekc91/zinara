#!/bin/bash
#set -o verbose
#set -o xtrace

bits=$2
file=$1

if test -z $1; then
    echo No se dieron argumentos
    exit 1
fi

if test "$bits" != "64" -a "$bits" != "32" -a "$bits" != ""; then
    echo Arquitectura no soportada
    exit 1
fi

nasm -g -f elf${bits} $1 && \
nasm -g -f elf${bits} asm_io.asm && \
gcc -o ${file%".asm"} ${file%".asm"}.o asm_io.o -lc
