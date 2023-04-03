package com.example.boe.Form;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
public class ImportDto {
    @Nullable
    private Integer classId;
    @Nullable
    private List<Integer> stuIds;
}
