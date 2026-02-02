package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.annotation.Generated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * DTO для создания или обновления объявления.
 * Содержит заголовок, цену и описание объявления.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public class CreateOrUpdateAd {

    private @Nullable String title;
    private @Nullable Integer price;
    private @Nullable String description;

    /**
     * Fluent-метод для установки заголовка объявления.
     *
     * @param title заголовок объявления
     * @return текущий объект CreateOrUpdateAd
     */
    public CreateOrUpdateAd title(@Nullable String title) {
        this.title = title;
        return this;
    }

    /**
     * Возвращает заголовок объявления.
     *
     * @return заголовок
     */
    @Size(min = 4, max = 32)
    @Schema(name = "title", description = "заголовок объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("title")
    public @Nullable String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    /**
     * Fluent-метод для установки цены объявления.
     *
     * @param price цена объявления
     * @return текущий объект CreateOrUpdateAd
     */
    public CreateOrUpdateAd price(@Nullable Integer price) {
        this.price = price;
        return this;
    }

    /**
     * Возвращает цену объявления.
     *
     * @return цена объявления
     */
    @Min(0)
    @Max(10000000)
    @Schema(name = "price", description = "цена объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("price")
    public @Nullable Integer getPrice() {
        return price;
    }

    public void setPrice(@Nullable Integer price) {
        this.price = price;
    }

    /**
     * Fluent-метод для установки описания объявления.
     *
     * @param description описание объявления
     * @return текущий объект CreateOrUpdateAd
     */
    public CreateOrUpdateAd description(@Nullable String description) {
        this.description = description;
        return this;
    }

    /**
     * Возвращает описание объявления.
     *
     * @return описание
     */
    @Size(min = 8, max = 64)
    @Schema(name = "description", description = "описание объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("description")
    public @Nullable String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateOrUpdateAd that = (CreateOrUpdateAd) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(price, that.price) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price, description);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CreateOrUpdateAd {\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
