package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO для создания или обновления комментария.
 * Содержит текст комментария.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public class CreateOrUpdateComment {

    private String text;

    /**
     * Пустой конструктор.
     */
    public CreateOrUpdateComment() {
        super();
    }

    /**
     * Конструктор с обязательными параметрами.
     *
     * @param text текст комментария
     */
    public CreateOrUpdateComment(String text) {
        this.text = text;
    }

    /**
     * Fluent-метод для установки текста комментария.
     *
     * @param text текст комментария
     * @return текущий объект CreateOrUpdateComment
     */
    public CreateOrUpdateComment text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Возвращает текст комментария.
     *
     * @return текст комментария
     */
    @NotNull
    @Size(min = 8, max = 64)
    @Schema(name = "text", description = "текст комментария", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * Устанавливает текст комментария.
     *
     * @param text текст комментария
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateOrUpdateComment that = (CreateOrUpdateComment) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CreateOrUpdateComment {\n");
        sb.append("    text: ").append(toIndentedString(text)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Преобразует объект в строку с отступами.
     *
     * @param o объект
     * @return форматированная строка
     */
    private String toIndentedString(Object o) {
        if (o == null) return "null";
        return o.toString().replace("\n", "\n    ");
    }
}
