package com.cspi.backend.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

// Getter, Builder(원하지 않은 수정을 방지하기 위해 Setter 대신 사용), ToString, AllArgsConstructor
//JsonInclude.include.NON_NULL
@Getter
@ToString
@Builder
public class Todo {
    private Long id;
    private String status;
    private String todoText;
    private String category;
    private String startDate;
    private String endDate;
}
