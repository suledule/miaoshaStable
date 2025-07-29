package com.imooc.miaoshaproject.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ZhihuArticleCrawler2023 {

    // 浏览器驱动路径（根据实际位置修改）
    private static final String DRIVER_PATH = "C:\\tools\\chromedriver.exe";

    // 知乎API URL模板
    private static final String API_URL_TEMPLATE =
            "https://www.zhihu.com/api/v4/articles/%s?include=title,author,content,created,comment_count,reward_info,excerpt";

    // API请求头
    private static final Map<String, String> API_HEADERS = new HashMap<>();
    static {
        API_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        API_HEADERS.put("Referer", "https://www.zhihu.com/");
        API_HEADERS.put("X-Requested-With", "fetch");
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);

        // 爬取专栏文章（API方式）
        String articleId = "12345678"; // 替换为实际文章ID
        ArticleData apiData = fetchViaAPI(articleId);
        if (apiData != null) {
            System.out.println("==== API方式获取 ====");
            printArticleData(apiData);
        }

        // 爬取问题回答（Selenium方式）
        String answerUrl = "https://www.zhihu.com/question/12345678/answer/87654321";
        ArticleData seleniumData = fetchViaSelenium(answerUrl);
        if (seleniumData != null) {
            System.out.println("\n==== Selenium方式获取 ====");
            printArticleData(seleniumData);
        }
    }


    private static ArticleData fetchViaAPI(String articleId) {
        try {
            String apiUrl = String.format(API_URL_TEMPLATE, articleId);

            // 发送API请求
            Document jsonResponse = Jsoup.connect(apiUrl)
                    .headers(API_HEADERS)
                    .ignoreContentType(true)
                    .timeout(10000)
                    .get();

            // 解析JSON响应
            return parseAPIResponse(jsonResponse.text(), articleId);
        } catch (Exception e) {
            System.err.println("API请求失败: " + e.getMessage());
            return null;
        }
    }


    private static ArticleData parseAPIResponse(String json, String articleId) {
        try {
            ArticleData data = new ArticleData();

            // 实际项目中使用JSON库如Jackson/Gson
            // 这里简化解析过程
            String title = extractFromJSON(json, "\"title\":\"(.*?)\"");
            String content = extractFromJSON(json, "\"content\":\"(.*?)\"");

            // 处理JSON转义字符
            title = unescapeJsonString(title);
            content = unescapeJsonString(content);

            data.title = title;
            data.content = content;
            data.url = "https://zhuanlan.zhihu.com/p/" + articleId;

            return data;
        } catch (Exception e) {
            System.err.println("API解析错误: " + e.getMessage());
            return null;
        }
    }


    private static ArticleData fetchViaSelenium(String url) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // 新版无头模式
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // 设置中文语言环境
        options.addArguments("--lang=zh-CN");

        // 绕过反爬检测
        options.setExperimentalOption("excludeSwitches",
                new String[]{"enable-automation"});
        options.addArguments("--disable-blink-features=AutomationControlled");

        WebDriver driver = null;
        try {
            driver = new ChromeDriver(options);
            driver.get(url);

            // 隐藏WebDriver特征
            hideWebDriverDetection(driver);

            // 显式等待页面加载
            WebDriverWait wait = new WebDriverWait(driver, 15);

            // 等待主要内容区域加载
//            if (url.contains("/answer/")) {
//                wait.until(ExpectedConditions.presenceOfElementLocated(
//                        By.cssSelector("div[class*=AnswerCard]")));
//            } else {
//                wait.until(ExpectedConditions.presenceOfElementLocated(
//                        By.cssSelector("div[class*=Post-Main]")));
//            }

            // 关闭登录弹窗（如果出现）
            closeLoginPopup(driver);

            // 模拟滚动加载完整内容
            simulateScrolling(driver, 5);

            // 获取处理后的HTML
            String html = driver.getPageSource();

            // 用Jsoup解析
            Document doc = Jsoup.parse(html);
            doc.setBaseUri(url);

            return parseSeleniumContent(doc, url);
        } catch (Exception e) {
            System.err.println("Selenium爬取失败: " + e.getMessage());
            return null;
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }


    private static void hideWebDriverDetection(WebDriver driver) {
        // 删除navigator.webdriver属性
//        driver.executeScript(
//                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
//
//        // 修改浏览器特征
//        driver.executeScript(
//                "window.navigator.chrome = {runtime: {}, etc: 'etc'};");
    }

    private static void closeLoginPopup(WebDriver driver) {
//        try {
//            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
//            shortWait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("button[class*=Modal-closeButton]"))).click();
//        } catch (Exception e) {
//            // 未出现弹窗则忽略
//        }
    }


    private static void simulateScrolling(WebDriver driver, int times) {
        for (int i = 0; i < times; i++) {
            //driver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                Thread.sleep(1500); // 等待内容加载
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    private static ArticleData parseSeleniumContent(Document doc, String url) {
        ArticleData data = new ArticleData();
        data.url = url;

        // 解析标题
        Element titleElement = doc.selectFirst("h1[class*=QuestionHeader-title], h1[class*=Post-Title]");
        if (titleElement != null) {
            data.title = titleElement.text();
        }

        // 解析作者
        Element authorElement = doc.selectFirst("meta[itemprop=name]");
        if (authorElement != null) {
            data.authorName = authorElement.attr("content");
        }

        // 解析内容（不同页面结构）
        Element contentElement = null;
        if (url.contains("/answer/")) {
            contentElement = doc.selectFirst("div[class*=-answer] div[class*=RichText]");
        } else {
            contentElement = doc.selectFirst("article div[class*=Post-RichText]");
        }

        if (contentElement != null) {
            // 提取干净的HTML（去除知乎特定的包裹元素）
            contentElement.select("div[class*=RichText-Editor]").unwrap();
            contentElement.select("span[class*=Image]").unwrap();

            data.content = contentElement.html();
        }

        // 解析点赞数
        Element voteElement = doc.selectFirst("button[class*=VoteButton]");
        if (voteElement != null) {
            String text = voteElement.text();
            data.voteCount = normalizeNumber(text);
        }

        return data;
    }


    private static String extractFromJSON(String json, String pattern) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }


    private static String unescapeJsonString(String str) {
        if (str == null) return "";
        return str.replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }

    private static int normalizeNumber(String text) {
        if (text.contains("K")) {
            return (int) (Double.parseDouble(text.replace("K", "")) * 1000);
        } else if (text.contains("万")) {
            return (int) (Double.parseDouble(text.replace("万", "")) * 10000);
        } else if (!text.isEmpty()) {
            return Integer.parseInt(text.replace(",", ""));
        }
        return 0;
    }

    private static void printArticleData(ArticleData data) {
        System.out.println("标题: " + (data.title != null ? data.title : "N/A"));
        System.out.println("URL: " + data.url);
        System.out.println("作者: " + (data.authorName != null ? data.authorName : "N/A"));
        System.out.println("点赞数: " + data.voteCount);
        System.out.println("内容预览: " +
                (data.content != null ?
                        data.content.substring(0, Math.min(100, data.content.length())) + "..." :
                        "N/A"));
    }

    static class ArticleData {
        String title;
        String authorName;
        String url;
        int voteCount;
        String content;
    }
}
