package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.valid(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTopUpFirstCard() {
        val dashboardPage = new DashboardPage();
        int expected = dashboardPage.getCardBalance("92df3f1c-a033-48e6-8390-206f6b1f56c0") + 1;
        dashboardPage.topUpCard1();
        val transferPage = new TransferPage();
        transferPage.transfer("1", "5559 0000 0000 0002");
        int firstCardBalance = dashboardPage.getCardBalance("92df3f1c-a033-48e6-8390-206f6b1f56c0");
        assertEquals(expected, firstCardBalance);
    }

    @Test
    void shouldTopUpSecondCard() {
        val dashboardPage = new DashboardPage();
        int expected = 20000;
        String sum = Integer.toString(dashboardPage.getCardBalance("92df3f1c-a033-48e6-8390-206f6b1f56c0"));
        dashboardPage.topUpCard2();
        val transferPage = new TransferPage();
        transferPage.transfer(sum, "5559 0000 0000 0001");
        int firstCardBalance = dashboardPage.getCardBalance("0f3f5c2a-249e-4c3d-8287-09f7a039391d");
        assertEquals(expected, firstCardBalance);
    }
}
