package com.example.myuser_management_ms.dto;

import com.example.myuser_management_ms.utils.Status;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuccessDto<T> {

    Status status;
    T data;

    public SuccessDto(Status status, T data) {
        this.status = status;
        this.data = data;
    }
}
