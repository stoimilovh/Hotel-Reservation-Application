package mk.ukim.finki.wp.kol2024g1;

import lombok.Getter;
import mk.ukim.finki.wp.exam.util.ExamAssert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

@Getter
public class ItemsPage extends AbstractPage {

    private WebElement guestName;

    private WebElement roomType;

    private WebElement hotel;

    private WebElement filter;

    @FindBy(css = "tr[class=item]")
    private List<WebElement> rows;

    @FindBy(css = ".delete-item")
    private List<WebElement> deleteButtons;

    @FindBy(className = "edit-item")
    private List<WebElement> editButtons;

    @FindBy(css = ".extend-item")
    private List<WebElement> extendButtons;

    @FindBy(css = ".add-item")
    private List<WebElement> addButton;

    public ItemsPage(WebDriver driver) {
        super(driver);
    }

    public static ItemsPage to(WebDriver driver) {
        get(driver, "/?pageSize=20&pageNum=1");
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public static ItemsPage to(WebDriver driver, int pageSize, int pageNum) {
        get(driver, "/?pageSize=" + pageSize + "&pageNum=" + pageNum);
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public ItemsPage filter(String guestName, String roomType, String hotel) {
        System.out.println(driver.getCurrentUrl());
        this.guestName.sendKeys(guestName);
        Select select = new Select(this.roomType);
        select.selectByValue(roomType);
        Select selectHotel = new Select(this.hotel);
        selectHotel.selectByValue(hotel);
        this.filter.click();
        String url = "/?guestName=" + guestName + "&roomType=" + roomType + "&hotel=" + hotel;
        assertRelativeUrl(this.driver, url.replaceAll(" ", "+"));
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public void assertButtons(int deleteButtonsCount, int editButtonsCount, int addButtonsCount, int extendButtonsCount) {
        ExamAssert.assertEquals("delete btn count", deleteButtonsCount, this.getDeleteButtons().size());
        ExamAssert.assertEquals("edit btn count", editButtonsCount, this.getEditButtons().size());
        ExamAssert.assertEquals("add btn count", addButtonsCount, this.getAddButton().size());
        ExamAssert.assertEquals("extend btn count", extendButtonsCount, this.getExtendButtons().size());
    }

    public boolean assertItems(int expectedItemsNumber) {
        return ExamAssert.assertEquals("Number of items", expectedItemsNumber, this.getRows().size());
    }
}
