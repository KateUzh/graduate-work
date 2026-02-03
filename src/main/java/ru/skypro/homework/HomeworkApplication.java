package ru.skypro.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot приложения для работы с объявлениями.
 * Предоставляет REST API для управления объявлениями, пользователями и комментариями.
 */
@SpringBootApplication
public class HomeworkApplication {
  /**
   * Точка входа в приложение.
   *
   * @param args аргументы командной строки
   */
  public static void main(String[] args) {
    SpringApplication.run(HomeworkApplication.class, args);
  }
}
