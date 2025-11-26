package com.backend.newbank.AccountTransaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.newbank.AccountTransaction.DTO.NotificationDTO.ResponseNotificationDTO;
import com.backend.newbank.AccountTransaction.service.NotificationsService;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationsService notificationsService;


    @GetMapping
    public ResponseEntity<List<ResponseNotificationDTO>> getNotifications() {
        List<ResponseNotificationDTO> notifications = notificationsService.getNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearNotifications() {
        notificationsService.clearNotifications();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{notification_id}")
    public ResponseEntity<Void> excludeNotification(@PathVariable Long notification_id) {
        notificationsService.excludeNotification(notification_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
