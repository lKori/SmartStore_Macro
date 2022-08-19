package AutoBot_pkg;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class AutoInput {

	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";	// driver ID
//	public static final String WEB_DRIVER_PATH = "C:\\xampp\\htdocs\\study\\JAVA\\PriceRead\\exe\\chromedriver.exe";	// driver path
	public static final String WEB_DRIVER_PATH = ".\\exe\\chromedriver.exe";	// driver path
	public static final String STORE_URL = "https://sell.smartstore.naver.com/#/products/origin-list";
	public static final String BASE_PATH = new File("").getAbsolutePath();
	public static String RESOURCE_PATH = "\\datasheet";
	public static String FILE_NAME = "\\price.xlsx";
	public static String FILE_PATH;
//	public static final String DATASHEET_PATH = "./datasheet/price.xlsx";
	public static FileInputStream FIS = null;
	public static XSSFWorkbook WORKBOOK = null;
	public static XSSFSheet SHEET = null;
	public static Integer SHEET_NUM = 1;	// sheet 1: NVME, sheet 2: Enterprise
	public static Integer PRODUCT_COUNT = 0;
	public static Integer currentIndex = 1;
	public JOptionPane pop = new JOptionPane();
	private static final String NAVER_ID = "";
	private static final String NAVER_PW = "";
	
	public void AutoBot() {
		System.out.println(SHEET_NUM);
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
			
			// browser 실행 시 설정
			ChromeOptions options = new ChromeOptions();
//			options.addArguments("headless");				// browser 보이지 않기
//			options.addArguments("--window-size=x, y");		// 실행되는 browser 크기 지정
//			options.addArguments("--start-maximized");		// 최대화된 상태로 browser 실행
//			options.addArguments("--start-fullscreen");		// 전체화면 상태로 browser 실행
//			options.addArguments("--blink-settings=imagesEnabled=false");	// browser 실행동안 이미지 로딩 안함
//			options.addArguments("--mute-audio");	// browser 음소거
//			options.addArguments("incognito");	// browser 음소거
			
			
			WebDriver driver = new ChromeDriver(options);
			driver.get("https://sell.smartstore.naver.com/#/products/origin-list");
			
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	// 로딩 완료까지 대기
			
			// 네이버 로그인
			driver.findElement(By.cssSelector("body > ui-view.wrap > div.seller-join-wrap > div > div > div > form > div.panel.panel-seller > ul > li:nth-child(2) > a")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	// 로딩 완료까지 대기
			driver.findElement(By.cssSelector("#id")).sendKeys(NAVER_ID);
			driver.findElement(By.cssSelector("#pw")).sendKeys(NAVER_PW);
			driver.findElement(By.cssSelector("#log\\.login")).click();
			System.out.println("login Success");
			
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	// 로딩 완료까지 대기

			// 2차 인증 여부 확인
			String currentURL = driver.getCurrentUrl();
			System.out.println("curURL: " + currentURL);
			System.out.println("storeURL: " + STORE_URL);

			if(currentURL.equals(STORE_URL)) {
				System.out.println("Success");
			} else {
				System.out.println("failed");
			}
			
			// 팝업창 확인
			boolean popDisplay = driver.findElement(By.cssSelector("#seller-content > div.seller-notice.seller-layer-modal.has-close-check-box > div > div > div.modal-body > ncp-manager-notice-view > ng-transclude > button > span")).isDisplayed();
			System.out.println(popDisplay);
			if(popDisplay) {
				driver.findElement(By.cssSelector("#seller-content > div.seller-notice.seller-layer-modal.has-close-check-box > div > div > div.modal-body > ncp-manager-notice-view > ng-transclude > button > span")).click();
			}

			// 전체 상품 버튼 클릭
			driver.findElement(By.cssSelector("#seller-content > ui-view > div > ui-view:nth-child(1) > div.form-section.seller-status > ul > li:nth-child(1) > a")).click();
			System.out.println("all button click Success");

			// 상품 검색 및 가격 변경
			for (int i = 0; i < PRODUCT_COUNT; i++) {
				driver.findElement(By.cssSelector("#seller-content > ui-view > div > ui-view:nth-child(1) > div.panel.panel-seller > form > div.panel-body > div > ul > li:nth-child(6) > div > div > ncp-datetime-range-picker2 > div:nth-child(1) > div > div > button:nth-child(7)")).click();
				String[] product_name = ReadPrice(i);
				
				driver.findElement(By.cssSelector("#prd_name")).clear();
				driver.findElement(By.cssSelector("#prd_name")).sendKeys(product_name[0]);	// 제품 이름 입력
				driver.findElement(By.cssSelector("#seller-content > ui-view > div > ui-view:nth-child(1) > div.panel.panel-seller > form > div.panel-footer > div > button.btn.btn-primary")).click();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	// 로딩 완료까지 대기
				Thread.sleep(500);
				
				Integer isPresent = Integer.valueOf(driver.findElement(By.cssSelector("#seller-content > ui-view > div > ui-view:nth-child(2) > div.panel.panel-seller > div.panel-heading > div.pull-left > h3 > span")).getText());
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	// 로딩 완료까지 대기
				System.out.println("INDEX: " + i);
				if (isPresent == 1) {
					System.out.println("FOUND!");
				} else {
					System.out.println("NOT FOUND!");
				}
				
				if(isPresent != 0) {
					driver.findElement(By.cssSelector("#seller-content > ui-view > div > ui-view:nth-child(2) > div.panel.panel-seller > div.panel-body > div.seller-grid-area > div > div > div > div > div.ag-body-viewport.ag-layout-normal.ag-row-no-animation > div.ag-pinned-left-cols-container > div > div:nth-child(2) > span > button")).click();
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	// 로딩 완료까지 대기
					driver.findElement(By.cssSelector("#prd_price2")).clear();
					driver.findElement(By.cssSelector("#prd_price2")).sendKeys(product_name[1]);	// 제품 가격 수정
					driver.findElement(By.cssSelector("#seller-content > ui-view > div.pc-fixed-area.navbar-fixed-bottom.hidden-xs > div.btn-toolbar.pull-right > div:nth-child(1) > button.btn.btn-primary.progress-button.progress-button-dir-horizontal.progress-button-style-top-line")).click();
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	// 로딩 완료까지 대기
					driver.findElement(By.cssSelector("body > div.modal.fade.seller-layer-modal.in > div > div > div.modal-footer > div > button.btn.btn-default")).click();
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	// 로딩 완료까지 대기
					System.out.println("Save Success");
				}
				System.out.println("진행률: (" + currentIndex + " / " + PRODUCT_COUNT + ")");
				currentIndex++;
			}
			
			System.out.println("가격 변경이 완료되었습니다!");
			pop.showMessageDialog(null, "가격 변경이 완료되었습니다!", "COMPLETE", JOptionPane.INFORMATION_MESSAGE);

			//browser 닫기
//			if(driver != null) {
				// driver.close(); // 창만 종료
				 driver.quit(); // 프로세스까지 전부 종료
//			}
		} catch (Exception e) {
			System.out.println("가격 변경을 완료하지 못했습니다!");
			pop.showMessageDialog(null, "가격 변경을 완료하지 못했습니다!", "ERROR", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public void ProductCheck() {
		// 가격표 엑셀 파일 초기화
		try {
//			FIS = new FileInputStream(DATASHEET_PATH);
			FILE_PATH = BASE_PATH + RESOURCE_PATH + FILE_NAME;
			System.out.println(FILE_PATH);
			FIS = new FileInputStream(FILE_PATH);
			WORKBOOK = new XSSFWorkbook(FIS);
			SHEET = WORKBOOK.getSheetAt(SHEET_NUM);
			PRODUCT_COUNT = SHEET.getPhysicalNumberOfRows();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String[] ReadPrice(int count) {
		String[] product = new String[2];
		
		try {
			XSSFRow row = SHEET.getRow(count);
			if(row != null) {
				for(int columnIndex = 1; columnIndex < 3; columnIndex++) {
					XSSFCell cell = row.getCell(columnIndex);	// 상품 이름 및 가격 읽어오기
					
					switch(cell.getCellType()) {
					case NUMERIC:
						int price = (int) cell.getNumericCellValue();	// 상품 가격
						product[1] = String.valueOf(price);
						System.out.println(product[1]);
						break;
					case STRING:
						product[0] = cell.getStringCellValue() + "";	// 상품 이름
						System.out.println(product[0]);
						break;
					case BLANK:
						System.out.println(count + "Data Blank");
						break;
					case ERROR:
						System.out.println(count + "Data Error");
						break;
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}
}