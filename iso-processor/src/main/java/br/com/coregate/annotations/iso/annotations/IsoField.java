package br.com.coregate.annotations.iso.annotations;

import br.com.coregate.annotations.iso.enums.IsoVarType;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface IsoField {
    int number();
    int length() default -1;       // para FIXED
    String format() default "";    // opcional (datas, etc.)
    boolean padLeft() default true;
    char padChar() default '0';
    IsoVarType varType() default IsoVarType.FIXED;
}
