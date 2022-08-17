package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[type=\"text\"]").setValue(registeredUser.getLogin());
        $("[type=\"password\"]").setValue(registeredUser.getPassword());
        $(".button").click();
        $(withText("Личный кабинет")).shouldBe((Condition.visible));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[type=\"text\"]").setValue(notRegisteredUser.getLogin());
        $("[type=\"password\"]").setValue(notRegisteredUser.getPassword());
        $(".button").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или" +
                " пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[type=\"text\"]").setValue(blockedUser.getLogin());
        $("[type=\"password\"]").setValue(blockedUser.getPassword());
        $(".button").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[type=\"text\"]").setValue(wrongLogin);
        $("[type=\"password\"]").setValue(registeredUser.getPassword());
        $(".button").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или" +
                " пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[type=\"text\"]").setValue(registeredUser.getLogin());
        $("[type=\"password\"]").setValue(wrongPassword);
        $(".button").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или" +
                " пароль"));
    }
}
