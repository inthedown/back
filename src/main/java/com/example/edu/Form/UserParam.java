package com.example.edu.Form;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;

/**
 * current
 * :
 * 1
 * role
 * :
 * ""
 * size
 * :
 * 5
 * userName
 * :
 * ""
 */
@Data
public class UserParam {
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String userName;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String roleId;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Integer current;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Integer size;

}
