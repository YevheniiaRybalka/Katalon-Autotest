import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling

TestObject getByXpath(String xpath) {
	TestObject to = new TestObject(xpath)
	to.addProperty("xpath", ConditionType.EQUALS, xpath)
	return to
}

// --- ЛОКАТОРИ ЕЛЕМЕНТІВ ---
TestObject inputUsername      = getByXpath("//input[@data-test='username']")
TestObject inputPassword      = getByXpath("//input[@data-test='password']")
TestObject btnLogin           = getByXpath("//input[@data-test='login-button']")

TestObject titleProducts      = getByXpath("//span[@data-test='title' and text()='Products']")
TestObject secondProductLink  = getByXpath("(//div[@data-test='inventory-item-name'])[2]")
TestObject btnAddFirstProduct = getByXpath("//button[@data-test='add-to-cart-sauce-labs-backpack']")

// Клікаємо безпосередньо по бейджу кошика (він гарантовано є, бо ми додали 2 товари)
TestObject cartBadge          = getByXpath("//span[@data-test='shopping-cart-badge']")

TestObject btnBackToProducts  = getByXpath("//button[@data-test='back-to-products']")
TestObject btnAddToCartDetail = getByXpath("//button[@data-test='add-to-cart']")

TestObject titleYourCart      = getByXpath("//span[@data-test='title' and text()='Your Cart']")
TestObject cartItemsList      = getByXpath("//div[@data-test='inventory-item']")


// --- ВИКОНАННЯ ТЕСТ-КЕЙСУ ---

try {
	// 1. Відкрити браузер та перейти на сайт
	WebUI.openBrowser('')
	WebUI.maximizeWindow()
	WebUI.navigateToUrl('https://www.saucedemo.com/')

	// 2. Ввести валідний логін та пароль, натиснути Login
	WebUI.setText(inputUsername, 'standard_user')
	WebUI.setText(inputPassword, 'secret_sauce')
	WebUI.click(btnLogin)

	// 3. Assertion: переконатися, що відбувся перехід на сторінку товарів
	WebUI.waitForElementVisible(titleProducts, 10, FailureHandling.STOP_ON_FAILURE)

	// 4. Відкрити сторінку другого товару
	WebUI.click(secondProductLink)

	// 5. Assertion: переконатися, що відбувся перехід на сторінку товару
	WebUI.waitForElementVisible(btnBackToProducts, 10, FailureHandling.STOP_ON_FAILURE)
	WebUI.verifyMatch(WebUI.getUrl(), '.*inventory-item\\.html.*', true)

	// 6. Додати товар у кошик
	WebUI.click(btnAddToCartDetail)

	// 7. Повернутися на попередню сторінку
	WebUI.back()

	// 8. Assertion: переконатися, що відбувся перехід на сторінку товарів
	WebUI.waitForElementVisible(titleProducts, 10, FailureHandling.STOP_ON_FAILURE)

	// 9. Додати інший товар у кошик
	WebUI.click(btnAddFirstProduct)

	// Перевіряємо, що лічильник оновився до "2" перед тим, як переходити в кошик
	WebUI.verifyElementText(cartBadge, '2')

	// 10. Скролимо вгору та клікаємо по бейджу кошика для переходу на сторінку Cart
	WebUI.scrollToPosition(0, 0)
	WebUI.click(cartBadge)

	// 11. Assertion: перевірити, що сторінка кошика відображена
	WebUI.waitForElementVisible(titleYourCart, 10, FailureHandling.STOP_ON_FAILURE)
	WebUI.verifyMatch(WebUI.getUrl(), '.*cart\\.html.*', true)

	// 12. Assertion: перевірити, що кількість товару в кошику відповідає доданій кількості (2 товари)
	int actualItemsCount = WebUI.findWebElements(cartItemsList, 5).size()
	assert actualItemsCount == 2 : "Очікувалося 2 товари в кошику, але знайдено: ${actualItemsCount}"

} finally {
	// 13. Закрити браузер
	WebUI.closeBrowser()
}
