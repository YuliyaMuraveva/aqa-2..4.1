package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amount = $("[data-test-id=amount] input");
    private SelenideElement from = $("[data-test-id=from] input");
    private SelenideElement transfer = $("[data-test-id=action-transfer]");

    public TransferPage() {
        transfer.shouldBe(Condition.visible);
    }

    public DashboardPage transfer(String sum, String card) {
        amount.sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        amount.setValue(sum);
        from.sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        from.setValue(card);
        transfer.click();
        return new DashboardPage();
    }
}
