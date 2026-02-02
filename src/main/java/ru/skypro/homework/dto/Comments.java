package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO для списка комментариев к объявлению.
 * Содержит общее количество комментариев и список объектов Comment.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public class Comments {

    private @Nullable Integer count;

    @Valid
    private List<@Valid Comment> results = new ArrayList<>();

    /**
     * Fluent-метод для установки общего количества комментариев.
     *
     * @param count общее количество
     * @return текущий объект Comments
     */
    public Comments count(@Nullable Integer count) {
        this.count = count;
        return this;
    }

    /**
     * Возвращает общее количество комментариев.
     *
     * @return количество комментариев
     */
    @Schema(name = "count", description = "общее количество комментариев", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("count")
    public @Nullable Integer getCount() {
        return count;
    }

    public void setCount(@Nullable Integer count) {
        this.count = count;
    }

    /**
     * Fluent-метод для установки списка комментариев.
     *
     * @param results список комментариев
     * @return текущий объект Comments
     */
    public Comments results(List<@Valid Comment> results) {
        this.results = results;
        return this;
    }

    /**
     * Добавляет один комментарий в список.
     *
     * @param resultsItem объект комментария
     * @return текущий объект Comments
     */
    public Comments addResultsItem(Comment resultsItem) {
        if (this.results == null) {
            this.results = new ArrayList<>();
        }
        this.results.add(resultsItem);
        return this;
    }

    /**
     * Возвращает список комментариев.
     *
     * @return список Comment
     */
    @Valid
    @Schema(name = "results", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("results")
    public List<@Valid Comment> getResults() {
        return results;
    }

    public void setResults(List<@Valid Comment> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comments comments = (Comments) o;
        return Objects.equals(count, comments.count) &&
                Objects.equals(results, comments.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, results);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Comments {\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    results: ").append(toIndentedString(results)).append("\n");
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
