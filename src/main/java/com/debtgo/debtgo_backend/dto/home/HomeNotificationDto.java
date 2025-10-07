package com.debtgo.debtgo_backend.dto.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeNotificationDto {
    private Long id;
    private String type; // INFO, ALERT, etc.
    private String message;
    private Date date;
    private boolean read;
}