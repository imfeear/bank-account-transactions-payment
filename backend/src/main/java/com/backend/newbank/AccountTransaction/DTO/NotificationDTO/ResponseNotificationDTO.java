package com.backend.newbank.AccountTransaction.DTO.NotificationDTO;

import java.sql.Timestamp;

public record ResponseNotificationDTO(Long id, String title, String message, Timestamp timestamp) {
}
