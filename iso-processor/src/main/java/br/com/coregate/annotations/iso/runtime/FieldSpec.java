package br.com.coregate.annotations.iso.runtime;


import br.com.coregate.annotations.iso.annotations.VarType;

public final class FieldSpec {
    public final int number;
    public final int length;
    public final VarType varType;
    public final boolean padLeft;
    public final char padChar;
    public final String format;

    public FieldSpec(int number, int length, VarType varType, boolean padLeft, char padChar, String format) {
        this.number = number; this.length = length; this.varType = varType;
        this.padLeft = padLeft; this.padChar = padChar; this.format = format;
    }
}
