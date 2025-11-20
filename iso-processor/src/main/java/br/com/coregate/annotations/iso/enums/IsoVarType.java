package br.com.coregate.annotations.iso.enums;

/**
 * Representa o tipo de tamanho do campo ISO8583.
 * FIXED → tamanho fixo
 * LLVAR → prefixo de 2 dígitos indicando tamanho (até 99)
 * LLLVAR → prefixo de 3 dígitos indicando tamanho (até 999)
 */
public enum IsoVarType {
    FIXED,
    LLVAR,
    LLLVAR
}
