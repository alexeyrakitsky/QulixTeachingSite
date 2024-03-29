package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class MessagesPage {
    private static WebDriver driver;
    private static final Logger logger = Logger.getLogger(MessagesPage.class);
    private MessageData messageData;
    private MainPage mainPage;
    private String author;

    public MessagesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(linkText = "Message List")
    private WebElement messageList;

    @FindBy(xpath = ".//body[contains(.,\"Message List\")]")
    private WebElement messageListTable;

    @FindBy(linkText = "New Message")
    private WebElement newMessageButton;

    @FindBy(xpath = ".//form[@action='/QulixTeachingSite/message/save']")
    private WebElement createMessageForm;

    @FindBy(name = "headline")
    private WebElement headline;

    @FindBy(name = "text")
    private WebElement text;

    @FindBy(name = "create")
    private WebElement sumbitButton;

    @FindBy(xpath = ".//h1[contains(text(),'Show Message')]")
    private WebElement showMessagePage;

    @FindBy(xpath = ".//a[contains(.,'View')]")
    private WebElement viewSelectedMessage;

    @FindBy(xpath = ".//h1[contains(.,'Edit Message')]")
    private WebElement editMessageForm;

    @FindBy(name = "_action_save")
    private WebElement submitMessageModification;

    @FindBy(name = "allUsers")
    private WebElement allUsersCheckBox;


    public void initMessageCreation() {
        newMessageButton.click();
        Assert.assertTrue(createMessageForm.isDisplayed());
    }


    public MessageData fillMessageForm(MessageData messageData) {
        headline.clear();
        this.headline.sendKeys(messageData.getName());
        Assert.assertFalse(headline.getAttribute("value").isEmpty());
        text.clear();
        this.text.sendKeys(messageData.getText());
        Assert.assertFalse(text.getAttribute("value").isEmpty());
        this.author = messageData.getAuthor();


        return messageData;

    }

    public void submitMessageCreation() {
        sumbitButton.click();
    }

    public boolean isShowMessageFormDisplayed() {
        return (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(showMessagePage)).isDisplayed();
    }

    public void goToMessageList() {
        messageList.click();
    }


    public List<MessageData> getMessageList() {
        List<MessageData> messages = new ArrayList<>();

        List<WebElement> nameFields = driver.findElements(By.xpath(".//tr/td[2]"));
        List<WebElement> textFields = driver.findElements(By.xpath(".//tr/td[3]"));
        List<WebElement> authorFields = driver.findElements(By.xpath(".//tr/td[4]"));

        for (int i = 0; i < textFields.size(); i++) {
            String name = nameFields.get(i).getText();
            String text = textFields.get(i).getText();
            String author = authorFields.get(i).getText();


            MessageData messageData = new MessageData().withName(name).withText(text).withAuthor(author);
            messages.add(messageData);
        }
        return messages;
    }

    public MessageData showMessage() {
        String name = driver.findElement(By.xpath("(.//td[@class=\"value\"])[1]")).getText();
        String text = driver.findElement(By.xpath("(.//td[@class=\"value\"])[3]")).getText();
        String author = driver.findElement(By.xpath("(.//td[@class=\"value\"])[2]")).getText();

        MessageData showMessageData = new MessageData().withName(name).withText(text).withAuthor(author);
        return showMessageData;
    }

    public void viewCreatedMessage() {
        List<WebElement> messages = driver.findElements(By.xpath(".//a[contains(.,'View')]"));
        messages.get(messages.size() - 1).click();
    }

    public void modifyCreatedMessage() {
        List<WebElement> messages = driver.findElements(By.xpath(".//a[contains(.,'Edit')]"));
        messages.get(messages.size() - 1).click();
        Assert.assertTrue(editMessageForm.isDisplayed());
    }

    public MessageData editMessageForm() {
        String name = driver.findElement(By.xpath(".//input[@name=\"headline\"]")).getAttribute("value");
        String text = driver.findElement(By.xpath(".//input[@name=\"text\"]")).getAttribute("value");

        MessageData editMessageForm = new MessageData().withName(name).withText(text).withAuthor(author);
        return editMessageForm;

    }

    public void submitMessageModification() {
        submitMessageModification.click();
    }


    public void deleteCreatedMessage() {
        List<WebElement> messages = driver.findElements(By.xpath(".//a[contains(.,'Delete')]"));
        messages.get(messages.size() - 1).click();

    }

    public boolean isMessageListTablePresent() {
        return (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(messageListTable)).isDisplayed();
    }

    public void showMessagesOfAllUsers() {
        allUsersCheckBox.click();
    }

    public void deleteTheLatestMessage(Object message) {
        deleteCreatedMessage();
        Assert.assertFalse(getMessageList().contains(message));
    }
}