package br.com.coregate.infrastructure.iso8583.annotations.iso;

import java.lang.annotation.*;
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface IsoMessage {
    String mti();
}

