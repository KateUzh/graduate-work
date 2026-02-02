package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.annotation.Generated;
import java.util.Objects;

/**
 * DTO расширенного представления объявления.
 * Используется для отображения детальной информации об объявлении,
 * включая данные автора и контактную информацию.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-22T22:29:14.541627300+03:00[Europe/Moscow]", comments = "Generator version: 7.16.0")
public class ExtendedAd {

    private @Nullable Integer pk;
    private @Nullable String authorFirstName;
    private @Nullable String authorLastName;
    private @Nullable String description;
    private @Nullable String email;
    private @Nullable String image;
    private @Nullable String phone;
    private @Nullable Integer price;
    private @Nullable String title;

    /**
     * Fluent-метод для установки идентификатора объявления.
     *
     * @param pk идентификатор объявления
     * @return текущий объект ExtendedAd
     */
    public ExtendedAd pk(@Nullable Integer pk) {
        this.pk = pk;
        return this;
    }

    /**
     * Возвращает идентификатор объявления.
     *
     * @return id объявления
     */
    @Schema(name = "pk", description = "id объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("pk")
    public @Nullable Integer getPk() {
        return pk;
    }

    /**
     * Устанавливает идентификатор объявления.
     *
     * @param pk id объявления
     */
    public void setPk(@Nullable Integer pk) {
        this.pk = pk;
    }

    /**
     * Fluent-метод для установки имени автора объявления.
     *
     * @param authorFirstName имя автора
     * @return текущий объект ExtendedAd
     */
    public ExtendedAd authorFirstName(@Nullable String authorFirstName) {
        this.authorFirstName = authorFirstName;
        return this;
    }

    /**
     * Возвращает имя автора объявления.
     *
     * @return имя автора
     */
    @Schema(name = "authorFirstName", description = "имя автора объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("authorFirstName")
    public @Nullable String getAuthorFirstName() {
        return authorFirstName;
    }

    /**
     * Устанавливает имя автора объявления.
     *
     * @param authorFirstName имя автора
     */
    public void setAuthorFirstName(@Nullable String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    /**
     * Fluent-метод для установки фамилии автора объявления.
     *
     * @param authorLastName фамилия автора
     * @return текущий объект ExtendedAd
     */
    public ExtendedAd authorLastName(@Nullable String authorLastName) {
        this.authorLastName = authorLastName;
        return this;
    }

    /**
     * Возвращает фамилию автора объявления.
     *
     * @return фамилия автора
     */
    @Schema(name = "authorLastName", description = "фамилия автора объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("authorLastName")
    public @Nullable String getAuthorLastName() {
        return authorLastName;
    }

    /**
     * Устанавливает фамилию автора объявления.
     *
     * @param authorLastName фамилия автора
     */
    public void setAuthorLastName(@Nullable String authorLastName) {
        this.authorLastName = authorLastName;
    }

    /**
     * Fluent-метод для установки описания объявления.
     *
     * @param description описание объявления
     * @return текущий объект ExtendedAd
     */
    public ExtendedAd description(@Nullable String description) {
        this.description = description;
        return this;
    }

    /**
     * Возвращает описание объявления.
     *
     * @return описание объявления
     */
    @Schema(name = "description", description = "описание объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("description")
    public @Nullable String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание объявления.
     *
     * @param description описание объявления
     */
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     * Fluent-метод для установки email автора объявления.
     *
     * @param email email автора
     * @return текущий объект ExtendedAd
     */
    public ExtendedAd email(@Nullable String email) {
        this.email = email;
        return this;
    }

    /**
     * Возвращает email автора объявления.
     *
     * @return email автора
     */
    @Schema(name = "email", description = "логин автора объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("email")
    public @Nullable String getEmail() {
        return email;
    }

    /**
     * Устанавливает email автора объявления.
     *
     * @param email email автора
     */
    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    /**
     * Fluent-метод для установки ссылки на изображение объявления.
     *
     * @param image ссылка на изображение
     * @return текущий объект ExtendedAd
     */
    public ExtendedAd image(@Nullable String image) {
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
     * Fluent-метод для установки телефона автора объявления.
     *
     * @param phone телефон автора
     * @return текущий объект ExtendedAd
     */
    public ExtendedAd phone(@Nullable String phone) {
        this.phone = phone;
        return this;
    }

    /**
     * Возвращает телефон автора объявления.
     *
     * @return телефон автора
     */
    @Schema(name = "phone", description = "телефон автора объявления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("phone")
    public @Nullable String getPhone() {
        return phone;
    }

    /**
     * Устанавливает телефон автора объявления.
     *
     * @param phone телефон автора
     */
    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    /**
     * Fluent-метод для установки цены объявления.
     *
     * @param price цена объявления
     * @return текущий объект ExtendedAd
     */
    public ExtendedAd price(@Nullable Integer price) {
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
     * Fluent-метод для установки заголовка объявления.
     *
     * @param title заголовок объявления
     * @return текущий объект ExtendedAd
     */
    public ExtendedAd title(@Nullable String title) {
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
        ExtendedAd that = (ExtendedAd) o;
        return Objects.equals(pk, that.pk)
                && Objects.equals(authorFirstName, that.authorFirstName)
                && Objects.equals(authorLastName, that.authorLastName)
                && Objects.equals(description, that.description)
                && Objects.equals(email, that.email)
                && Objects.equals(image, that.image)
                && Objects.equals(phone, that.phone)
                && Objects.equals(price, that.price)
                && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, authorFirstName, authorLastName, description, email, image, phone, price, title);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ExtendedAd {\n");
        sb.append("    pk: ").append(toIndentedString(pk)).append("\n");
        sb.append("    authorFirstName: ").append(toIndentedString(authorFirstName)).append("\n");
        sb.append("    authorLastName: ").append(toIndentedString(authorLastName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
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
