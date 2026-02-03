package ru.skypro.homework.config;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.entity.UserEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * Реализация UserDetails для Spring Security.
 * Адаптирует сущность UserEntity к интерфейсу UserDetails.
 */
public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    /**
     * Конструктор для создания CustomUserDetails.
     *
     * @param user сущность пользователя
     */
    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    /**
     * Возвращает роли пользователя.
     *
     * @return коллекция ролей с префиксом ROLE_
     */
    @Override
    public Collection getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return зашифрованный пароль
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Возвращает имя пользователя (email).
     *
     * @return email пользователя
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Проверяет, не истек ли срок действия аккаунта.
     *
     * @return всегда true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт.
     *
     * @return всегда true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных.
     *
     * @return всегда true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, активен ли аккаунт.
     *
     * @return всегда true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Возвращает сущность пользователя.
     *
     * @return объект UserEntity
     */
    public UserEntity getUser() {
        return user;
    }

}
