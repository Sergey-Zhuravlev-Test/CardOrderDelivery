package ru.netology;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;


public class OrderCardDeliveryTest {
    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String text = newDate.format(formatter);
    LocalDate parsedDate = LocalDate.parse(text, formatter);

    @BeforeEach
    void Setup() {
        open("http://localhost:7777/");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }


    @Test
    void shouldSendOrderForDeliveryCard() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").setValue(parsedDate.format(formatter));
        $("[data-test-id='name'] input").setValue("Петров-Горин Эдуард");
        $("[data-test-id='phone'] input").setValue("+79351112233");
        $("[data-test-id='agreement']").click();
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__title").shouldHave(text("Успешно"));
        $(".notification__content").shouldHave(text("Встреча успешно забронирована на " + parsedDate.format(formatter)));
    }

    @Test
    void shouldFillCity() {
        $("[data-test-id=date] input").setValue(parsedDate.format(formatter));
        $("[data-test-id='name'] input").setValue("Петров-Горин Эдуард");
        $("[data-test-id='phone'] input").setValue("+79351112233");
        $("[data-test-id='agreement']").click();
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }


    @Test
    void shouldFillAvailableCity() {
        $("[data-test-id='city'] input").setValue("Выборг");
        $("[data-test-id=date] input").setValue(parsedDate.format(formatter));
        $("[data-test-id='name'] input").setValue("Петров-Горин Эдуард");
        $("[data-test-id='phone'] input").setValue("+79351112233");
        $("[data-test-id='agreement']").click();
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }


    @Test
    void shouldFillDate() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='name'] input").setValue("Петров-Горин Эдуард");
        $("[data-test-id='phone'] input").setValue("+79351112233");
        $("[data-test-id='agreement']").click();
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(text("Неверно введена дата"));

    }

    @Test
    void shouldFillCorrectDate() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").setValue("01.01.2000");
        $("[data-test-id='name'] input").setValue("Петров-Горин Эдуард");
        $("[data-test-id='phone'] input").setValue("+79351112233");
        $("[data-test-id='agreement']").click();
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));

    }

    @Test
    void shouldFillName() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").setValue(parsedDate.format(formatter));
        $("[data-test-id='phone'] input").setValue("+79351112233");
        $("[data-test-id='agreement']").click();
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFillCorrectName() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").setValue(parsedDate.format(formatter));
        $("[data-test-id='name'] input").setValue("Van");
        $("[data-test-id='phone'] input").setValue("+79351112233");
        $("[data-test-id='agreement']").click();
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldFillPhone() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").setValue(parsedDate.format(formatter));
        $("[data-test-id='name'] input").setValue("Петров-Горин Эдуард");
        $("[data-test-id='agreement']").click();
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFillCorrectPhone() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").setValue(parsedDate.format(formatter));
        $("[data-test-id='name'] input").setValue("Петров-Горин Эдуард");
        $("[data-test-id='phone'] input").setValue("3332211");
        $("[data-test-id='agreement']").click();
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldFillAgreement() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").setValue(parsedDate.format(formatter));
        $("[data-test-id='name'] input").setValue("Петров-Горин Эдуард");
        $("[data-test-id='phone'] input").setValue("+79351112233");
        $x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text").shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }


}
