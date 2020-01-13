/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package promo_creation;


import com.opencsv.CSVWriter;
import io.github.bonigarcia.wdm.WebDriverManager;
import okhttp3.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import promo_creation.objects.Credential;
import promo_creation.objects.DirectPromo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static promo_creation.utils.CsvUtil.*;
import static promo_creation.utils.OtpUtil.*;

public class App {


    public static void main(String[] args) throws InterruptedException, IOException {
        printGoVietLogo();
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
        String currentDirectory = System.getProperty("user.dir") + File.separator;
        String inputCsvPath = currentDirectory + "input.csv";
        String credentialCsvPath = currentDirectory + "credential.csv";
        String outputCsvPath = currentDirectory + timeStamp + "_output.csv";
        System.out.println("INIT HOST URL");
        String hostStgUrl = "http://govietstaging-marketing-portal.go-pay.co.id/";
        String hostPrdUrl = "http://gopay-marketing-portal-internal.goviet.golabs.io/";
        String hostUrl = hostPrdUrl;

        //Automatically set up chrome driver
        System.out.println("BEFORE PROCESSING THE CHROME DRIVER");
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        System.out.println("AFTER PROCESSING THE CHROME DRIVER");
        login(credentialCsvPath, hostUrl, driver);
        System.out.println("CALL createDirectPromoApp");
        createDirectPromoApp(inputCsvPath, outputCsvPath, hostUrl, driver);
    }

    private static void login(String credentialCsvPath, String hostUrl, WebDriver driver) throws IOException, InterruptedException {
        driver.get(hostUrl);
        System.out.println("BEFORE LOGIN");
        List<Credential> credentials = readCredentialsFromCsv(credentialCsvPath);
        driver.findElement(By.id("username")).sendKeys(credentials.get(0).getUsername());
        driver.findElement(By.id("password")).sendKeys(getOtpCode(credentials.get(0).getKey()));
        driver.findElement(By.name("submit")).click();
        Thread.sleep(2000);
        System.out.println("AFTER LOGIN");
    }

    private static void createDirectPromoApp(String inputCsvPath, String outputCsvPath, String hostUrl, WebDriver driver) throws IOException, InterruptedException {
        //Navigate to web form url first to get correct cookies and authenticity_token
        System.out.println("GET NEW UNIQUE");
        driver.get(hostUrl + "promotions/new_unique");
        Cookie seleniumCookie = driver.manage().getCookieNamed("_admin_portal_session");
        String cookieValue = seleniumCookie.getValue();
        String authenticity_token;
        authenticity_token = driver.findElement(By.xpath("//meta[@name='csrf-token']")).getAttribute("content");
        System.out.println("SET UP COOKIE AND TOKEN");
        List<DirectPromo> directPromoList = readDirectPromoListFromCsv(inputCsvPath);
        System.out.println("WRITE NEXT");
        CSVWriter csvWriter = new CSVWriter(new FileWriter(outputCsvPath, true), ',');
        csvWriter.writeNext(new String[]{"name", "isActive", "description", "voucherBatchId", "lengthOfPromoCode", "numberOfPromoCode",
                "start", "end", "validityType", "enTitle", "enMsg", "viTitle", "viMsg", "deepLink", "includeOperator",
                "excludeOperator", "id"});
        csvWriter.close();
        System.out.println("FOR BEGIN");
        for (DirectPromo directPromo : directPromoList) {
            createDirectPromoHandler(hostUrl, outputCsvPath, driver, cookieValue, authenticity_token, directPromo);
        }
        System.out.println("-------------------------------------");
        System.out.println("DONE!");
        System.out.println("-------------------------------------");
        driver.close();
    }


    private static void createDirectPromoHandler(String hostUrl, String outputCsvPath, WebDriver driver, String cookieValue, String authenticityToken, DirectPromo directPromo) throws IOException, InterruptedException {
        System.out.println("createDirectPromoHandler BEGIN");
        String responseBody = createDirectPromoAPI(hostUrl, authenticityToken, cookieValue, directPromo);
//        Handle sign-on require
        Document html = Jsoup.parse(responseBody);
        System.out.println("try ID BEGIN");
        try {
            String id = html.getElementsContainingText(directPromo.getName()).attr("href").substring(62);
            directPromo.setId(id);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("-------------------------------------");
            System.out.println("ERROR - COULD NOT CREATE DIRECT PROMO\nPLEASE DOUBLE CHECK VOUCHER SYSTEM OR INPUT DATA (TRY TO CREATE IT MANUALLY FIRST!");
            System.out.println("-------------------------------------");
            System.out.println(responseBody);
            System.out.println("-------------------------------------");
            driver.close();
            throw e;
        }
        System.out.println("try ID END");
        System.out.println("Creating ... " + directPromo);
        writeDirectPromoToCsv(outputCsvPath, directPromo);
    }

    public static String createDirectPromoAPI(String hostUrl, String authenticity_token, String cookiesValue, DirectPromo directPromo) throws IOException {
        System.out.println("createDirectPromoAPI BEGIN");
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                "name=\"utf8\"\r\n\r\n✓\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                "name=\"authenticity_token\"\r\n\r\n"+ authenticity_token +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                "name=\"promotion[name]\"\r\n\r\n"+ directPromo.getName() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[is_active]\"\r\n\r\n"+ directPromo.getIsActive() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[description]\"\r\n\r\n"+ directPromo.getDescription() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[voucher_batch_ids]\"\r\n\r\n"+ directPromo.getVoucherBatchId() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[prefix]\"\r\n\r\n"+ directPromo.getPromoCodePrefix() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[promo_code_length]\"\r\n\r\n"+ directPromo.getLengthOfPromoCode() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[num_of_codes]\"\r\n\r\n"+ directPromo.getNumberOfPromoCode() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[start]\"\r\n\r\n"+ directPromo.getStart()  +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[end]\"\r\n\r\n"+ directPromo.getEnd() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[validity_type]\"\r\n\r\n"+ directPromo.getValidityType() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[fixed_duration]\"\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[duration_type]\"\r\n\r\nHours\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[fixed_day]\"\r\n\r\n"+ directPromo.getValidityPeriod() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[translation_en]\"\r\n\r\nen\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[title_en]\"\r\n\r\n"+ directPromo.getEnTitle() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[message_en]\"\r\n\r\n"+ directPromo.getEnMsg() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[translation_lang]\"\r\n\r\nvi\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[title_lang]\"\r\n\r\n"+ directPromo.getViTitle() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[message_lang]\"\r\n\r\n"+ directPromo.getViMsg() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[deep_link]\"\r\n\r\n"+ directPromo.getDeepLink() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[source]\"\r\n\r\nSEGMENTATION_SERVICE\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[include_segments]\"\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[include_segment_operators]\"\r\n\r\n"+ directPromo.getIncludeOperator() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[exclude_segments]\"\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[exclude_segment_operators]\"\r\n\r\n"+ directPromo.getExcludeOperator() +"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"promotion[include_s2ids]\"\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; "+
                "name=\"commit\"\r\n\r\nCreate Unique promotion\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url(hostUrl + "promotions/new_unique")
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("Content-Type", "text/plain")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .addHeader("Accept-Language", "en-US,en;q=0.9,vi;q=0.8,fr;q=0.7,la;q=0.6,it;q=0.5,da;q=0.4,id;q=0.3")
                .addHeader("Cookie", "_admin_portal_session=" + cookiesValue)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("createDirectPromoAPI END");
        return response.body().string();
    }

    private static void printGoVietLogo() {
        System.out.println("////////////////////////////////////////////////\n" +
                "////////////////////////////////////////////////\n" +
                "////////@@@@@@@@@@@@@@@@@@@@@@@@@@@@////////////\n" +
                "//////////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@(////////\n" +
                "/////////////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//////\n" +
                "//////////////////////////////////&@@@@@@@@/////\n" +
                "/////////////@@@@@@@/////@@@@@@@(////@@@@@@@#///\n" +
                "////////////@@@@@@@///@@@@@@/@@@@@@///#@@@@@@///\n" +
                "///////////@@@@@@@///@@@@@@@//@@@@@@(//%@@@@@@//\n" +
                "///&&&&%///@@@@@@///@@@%%%%///%%%%%@@///@@@@@@//\n" +
                "//@@@@@@@//@@@@@@///@@@@&////////@@@@///@@@@@@//\n" +
                "///////////@@@@@@///@@@@@@/////#@@@@@///@@@@@@//\n" +
                "///////////@@@@@@@///@@@@//@@@//@@@@&//(@@@@@@//\n" +
                "////////////@@@@@@@///@@@@@@@@@@@@@///(@@@@@@///\n" +
                "/////////////@@@@@@@/////@@@@@@@#////@@@@@@@%///\n" +
                "/////////@@@@@@@@@@@@@@///////////(@@@@@@@@/////\n" +
                "///////////////%@@@@@@@@@@@@@@@@@@@@@@@@@@//////\n" +
                "//////////////////@@@@@@@@@@@@@@@@@@@@@@////////\n" +
                "/////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#///////////\n" +
                "////////////////////////////////////////////////\n" +
                "////////////////////////////////////////////////");
    }
}