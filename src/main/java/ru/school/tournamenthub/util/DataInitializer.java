package ru.school.tournamenthub.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.school.tournamenthub.model.entity.User;
import ru.school.tournamenthub.model.enums.UserRole;
import ru.school.tournamenthub.repository.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createAdminUser();
    }

    private void createAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@tournamenthub.ru");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);
            admin.setFullName("Администратор Системы");
            admin.setIsActive(true);

            userRepository.save(admin);
            log.info("Создан администратор: admin / admin123");
        } else {
            log.info("Администратор уже существует");
        }
    }
}