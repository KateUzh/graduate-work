package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.annotation.Generated;
import java.util.Objects;

/**
 * DTO для комментария к объявлению.
 * Содержит информацию об авторе, тексте и времени создания.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public class Comment {

    private @Nullable Integer author;
    private @Nullable String authorImage;
    private @Nullable String authorFirstName;
    private @Nullable Long createdAt;
    private @Nullable Integer pk;
    private @Nullable String text;

    /**
     * Fluent-метод для установки id автора комментария.
     *
     * @param author id автора
     * @return текущий объект Comment
     */
    public Comment author(@Nullable Integer author) {
        this.author = author;
        return this;
    }

    /**
     * Возвращает id автора комментария.
     *
     * @return id автора
     */
    @Schema(name = "author", description = "id автора комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("author")
    public @Nullable Integer getAuthor() {
        return author;
    }

    public void setAuthor(@Nullable Integer author) {
        this.author = author;
    }

    /**
     * Fluent-метод для установки ссылки на аватар автора.
     *
     * @param authorImage ссылка на аватар
     * @return текущий объект Comment
     */
    public Comment authorImage(@Nullable String authorImage) {
        this.authorImage = authorImage;
        return this;
    }

    /**
     * Возвращает ссылку на аватар автора.
     *
     * @return ссылка на аватар
     */
    @Schema(name = "authorImage", description = "ссылка на аватар автора комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("authorImage")
    public @Nullable String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(@Nullable String authorImage) {
        this.authorImage = authorImage;
    }

    /**
     * Fluent-метод для установки имени автора.
     *
     * @param authorFirstName имя автора
     * @return текущий объект Comment
     */
    public Comment authorFirstName(@Nullable String authorFirstName) {
        this.authorFirstName = authorFirstName;
        return this;
    }

    /**
     * Возвращает имя автора комментария.
     *
     * @return имя автора
     */
    @Schema(name = "authorFirstName", description = "имя создателя комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("authorFirstName")
    public @Nullable String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(@Nullable String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    /**
     * Fluent-метод для установки времени создания комментария.
     *
     * @param createdAt время создания в миллисекундах с 01.01.1970
     * @return текущий объект Comment
     */
    public Comment createdAt(@Nullable Long createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Возвращает время создания комментария.
     *
     * @return время в миллисекундах с 01.01.1970
     */
    @Schema(name = "createdAt", description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("createdAt")
    public @Nullable Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable Long createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Fluent-метод для установки id комментария.
     *
     * @param pk id комментария
     * @return текущий объект Comment
     */
    public Comment pk(@Nullable Integer pk) {
        this.pk = pk;
        return this;
    }

    /**
     * Возвращает id комментария.
     *
     * @return id комментария
     */
    @Schema(name = "pk", description = "id комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("pk")
    public @Nullable Integer getPk() {
        return pk;
    }

    public void setPk(@Nullable Integer pk) {
        this.pk = pk;
    }

    /**
     * Fluent-метод для установки текста комментария.
     *
     * @param text текст комментария
     * @return текущий объект Comment
     */
    public Comment text(@Nullable String text) {
        this.text = text;
        return this;
    }

    /**
     * Возвращает текст комментария.
     *
     * @return текст комментария
     */
    @Schema(name = "text", description = "текст комментария", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("text")
    public @Nullable String getText() {
        return text;
    }

    public void setText(@Nullable String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(author, comment.author) &&
                Objects.equals(authorImage, comment.authorImage) &&
                Objects.equals(authorFirstName, comment.authorFirstName) &&
                Objects.equals(createdAt, comment.createdAt) &&
                Objects.equals(pk, comment.pk) &&
                Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, authorImage, authorFirstName, createdAt, pk, text);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Comment {\n");
        sb.append("    author: ").append(toIndentedString(author)).append("\n");
        sb.append("    authorImage: ").append(toIndentedString(authorImage)).append("\n");
        sb.append("    authorFirstName: ").append(toIndentedString(authorFirstName)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    pk: ").append(toIndentedString(pk)).append("\n");
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
