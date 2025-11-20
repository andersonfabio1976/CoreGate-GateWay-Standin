package br.com.coregate.annotations.iso.annotations;

import java.lang.annotation.*;
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface IsoMessage {
    String mti();
}

