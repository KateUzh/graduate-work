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
 * DTO для списка объявлений (Ads).
 * Содержит общее количество объявлений и список объектов Ad.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public class Ads {

    private @Nullable Integer count;

    @Valid
    private List<@Valid Ad> results = new ArrayList<>();

    /**
     * Устанавливает общее количество объявлений.
     *
     * @param count общее количество объявлений
     * @return текущий объект Ads
     */
    public Ads count(@Nullable Integer count) {
        this.count = count;
        return this;
    }

    /**
     * Возвращает общее количество объявлений.
     *
     * @return общее количество объявлений
     */
    @Schema(name = "count", description = "общее количество объявлений", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("count")
    public @Nullable Integer getCount() {
        return count;
    }

    /**
     * Устанавливает общее количество объявлений.
     *
     * @param count общее количество объявлений
     */
    public void setCount(@Nullable Integer count) {
        this.count = count;
    }

    /**
     * Устанавливает список объявлений.
     *
     * @param results список объявлений
     * @return текущий объект Ads
     */
    public Ads results(List<@Valid Ad> results) {
        this.results = results;
        return this;
    }

    /**
     * Добавляет одно объявление в список results.
     *
     * @param resultsItem объявление для добавления
     * @return текущий объект Ads
     */
    public Ads addResultsItem(Ad resultsItem) {
        if (this.results == null) {
            this.results = new ArrayList<>();
        }
        this.results.add(resultsItem);
        return this;
    }

    /**
     * Возвращает список объявлений.
     *
     * @return список объявлений
     */
    @Valid
    @Schema(name = "results", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("results")
    public List<@Valid Ad> getResults() {
        return results;
    }

    /**
     * Устанавливает список объявлений.
     *
     * @param results список объявлений
     */
    public void setResults(List<@Valid Ad> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ads ads = (Ads) o;
        return Objects.equals(count, ads.count) &&
                Objects.equals(results, ads.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, results);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Ads {\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    results: ").append(toIndentedString(results)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Преобразует объект в строку с отступами для удобного форматирования.
     *
     * @param o объект для преобразования
     * @return отформатированная строка
     */
    private String toIndentedString(Object o) {
        if (o == null) return "null";
        return o.toString().replace("\n", "\n    ");
    }
}
