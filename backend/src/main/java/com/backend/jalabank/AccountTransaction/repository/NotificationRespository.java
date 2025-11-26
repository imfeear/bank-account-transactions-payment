package com.backend.jalabank.AccountTransaction.repository;

import com.backend.jalabank.AccountTransaction.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRespository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccountId(Long id);
    void deleteAllByAccountId(Long id);
}
