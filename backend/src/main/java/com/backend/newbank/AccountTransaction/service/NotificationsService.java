package com.backend.newbank.AccountTransaction.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.newbank.AccountTransaction.DTO.NotificationDTO.ResponseNotificationDTO;
import com.backend.newbank.AccountTransaction.entity.Account;
import com.backend.newbank.AccountTransaction.entity.Notification;
import com.backend.newbank.AccountTransaction.repository.AccountRepository;
import com.backend.newbank.AccountTransaction.repository.NotificationRespository;
import com.backend.newbank.exception.EmptyListException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationsService {

    @Autowired
    private NotificationRespository notificationRespository;

    @Autowired
    private AccountRepository accountRepository;

    public List<ResponseNotificationDTO> getNotifications() {
        Long accountId = (long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Notification> notifications = notificationRespository.findByAccountId(accountId);

        if (notifications.isEmpty()) {
            throw new EmptyListException("Nenhuma notificação encontrada");
        }

        return notifications.stream()
                .map(notification -> new ResponseNotificationDTO(
                        notification.getId(),
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendNotification(String title, String message) {
        Long account_id = (long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Account account = accountRepository.findById(account_id)
                        .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));

        Notification notification = new Notification();

        notification.setAccount(account);
        notification.setTitle(title);
        notification.setMessage(message);

        notificationRespository.save(notification);
    }

    @Transactional
    public void clearNotifications() {
        Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationRespository.deleteAllByAccountId(accountId);
    }

    @Transactional
    public void excludeNotification(Long notification_id) {
        notificationRespository.deleteById(notification_id);
    }
}
