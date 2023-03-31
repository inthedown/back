package com.example.boe.Form;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;

@Data
public class ClassesParam {
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String className;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Integer current;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Integer size;
}
