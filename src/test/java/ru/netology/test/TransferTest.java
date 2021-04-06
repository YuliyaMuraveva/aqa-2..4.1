package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    private String firstCardId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    private String secondCardId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.valid(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void asserting() {
        val dashboardPage = new DashboardPage();
        int firstCardBalance = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalance = dashboardPage.getCardBalance(secondCardId);
        if (firstCardBalance != secondCardBalance) {
            int average = (firstCardBalance - secondCardBalance) / 2;
            if (firstCardBalance < secondCardBalance) {
                dashboardPage.topUpCard(1);
                val transferPage = new TransferPage();
                transferPage.transfer(Integer.toString(average), "5559 0000 0000 0002");
            }
            else {
                dashboardPage.topUpCard(2);
                val transferPage = new TransferPage();
                transferPage.transfer(Integer.toString(average), "5559 0000 0000 0001");
            }
        }
    }

    @Test
    void shouldTopUpFirstCard() {
        val dashboardPage = new DashboardPage();
        int expected = dashboardPage.getCardBalance(firstCardId) + 1;
        dashboardPage.topUpCard(1);
        val transferPage = new TransferPage();
        transferPage.transfer("1", "5559 0000 0000 0002");
        int firstCardBalance = dashboardPage.getCardBalance(firstCardId);
        assertEquals(expected, firstCardBalance);
    }

    @Test
    void shouldTopUpSecondCard() {
        val dashboardPage = new DashboardPage();
        int expected = 20000;
        dashboardPage.topUpCard(2);
        val transferPage = new TransferPage();
        transferPage.transfer("10000", "5559 0000 0000 0001");
        int secondCardBalance = dashboardPage.getCardBalance(secondCardId);
        assertEquals(expected, secondCardBalance);
    }

    @Test
    void shouldGetNotification() {
        val dashboardPage = new DashboardPage();
        dashboardPage.topUpCard(2);
        val transferPage = new TransferPage();
        transferPage.transfer("10001", "5559 0000 0000 0001");
        $("[data-test-id=error-notification]").$(withText("Ошибка")).shouldBe(Condition.visible);
    }
}
