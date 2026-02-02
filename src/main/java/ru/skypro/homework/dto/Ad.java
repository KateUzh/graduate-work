package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.annotation.Generated;
import java.util.Objects;

/**
 * DTO для объявления (Ad).
 * Используется для передачи данных объявления между клиентом и сервером.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public class Ad {

    private @Nullable Integer author;
    private @Nullable String image;
    private @Nullable Integer pk;
    private @Nullable Integer price;
    private @Nullable String title;

    /**
     * Устанавливает id автора объявления.
     *
     * @param author id автора
     * @return текущий объект Ad
     */
    public Ad author(@Nullable Integer author) {
        this.author = author;
        return this;
    }

    /**
     * Возвращает id автора объявления.
     *
     * @return id автора
     */
    @Schema(name = "author", description = "id автора объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("author")
    public @Nullable Integer getAuthor() {
        return author;
    }

    /**
     * Устанавливает id автора объявления.
     *
     * @param author id автора
     */
    public void setAuthor(@Nullable Integer author) {
        this.author = author;
    }

    /**
     * Устанавливает ссылку на изображение объявления.
     *
     * @param image ссылка на изображение
     * @return текущий объект Ad
     */
    public Ad image(@Nullable String image) {
        this.image = image;
        return this;
    }

    /**
     * Возвращает ссылку на изображение объявления.
     *
     * @return ссылка на изображение
     */
    @Schema(name = "image", description = "ссылка на картинку объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("image")
    public @Nullable String getImage() {
        return image;
    }

    /**
     * Устанавливает ссылку на изображение объявления.
     *
     * @param image ссылка на изображение
     */
    public void setImage(@Nullable String image) {
        this.image = image;
    }

    /**
     * Устанавливает id объявления.
     *
     * @param pk id объявления
     * @return текущий объект Ad
     */
    public Ad pk(@Nullable Integer pk) {
        this.pk = pk;
        return this;
    }

    /**
     * Возвращает id объявления.
     *
     * @return id объявления
     */
    @Schema(name = "pk", description = "id объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("pk")
    public @Nullable Integer getPk() {
        return pk;
    }

    /**
     * Устанавливает id объявления.
     *
     * @param pk id объявления
     */
    public void setPk(@Nullable Integer pk) {
        this.pk = pk;
    }

    /**
     * Устанавливает цену объявления.
     *
     * @param price цена объявления
     * @return текущий объект Ad
     */
    public Ad price(@Nullable Integer price) {
        this.price = price;
        return this;
    }

    /**
     * Возвращает цену объявления.
     *
     * @return цена объявления
     */
    @Schema(name = "price", description = "цена объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("price")
    public @Nullable Integer getPrice() {
        return price;
    }

    /**
     * Устанавливает цену объявления.
     *
     * @param price цена объявления
     */
    public void setPrice(@Nullable Integer price) {
        this.price = price;
    }

    /**
     * Устанавливает заголовок объявления.
     *
     * @param title заголовок
     * @return текущий объект Ad
     */
    public Ad title(@Nullable String title) {
        this.title = title;
        return this;
    }

    /**
     * Возвращает заголовок объявления.
     *
     * @return заголовок объявления
     */
    @Schema(name = "title", description = "заголовок объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("title")
    public @Nullable String getTitle() {
        return title;
    }

    /**
     * Устанавливает заголовок объявления.
     *
     * @param title заголовок объявления
     */
    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return Objects.equals(author, ad.author) &&
                Objects.equals(image, ad.image) &&
                Objects.equals(pk, ad.pk) &&
                Objects.equals(price, ad.price) &&
                Objects.equals(title, ad.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, image, pk, price, title);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Ad {\n");
        sb.append("    author: ").append(toIndentedString(author)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    pk: ").append(toIndentedString(pk)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) return "null";
        return o.toString().replace("\n", "\n    ");
    }
}
