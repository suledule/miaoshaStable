package com.imooc.miaoshaproject.crawler;

import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ZhihuCrawler {

    // 用户代理池（防止单一UA被封）
    private static final String[] USER_AGENTS = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/119.0"
    };

    // 知乎登录Cookie（从浏览器复制）
    private static final String ZHIHU_COOKIE = "_xsrf=66pFs6FRwTWmD0ruZdYcvOjzYFPh358e; _zap=5131bec0-5ad3-4706-aa78-74272c1a13a0; d_c0=MeKTygtnqBqPTvIZlOGMtbSNIgUo4xGWXdQ=|1750757900; __snaker__id=XEgyNmb5yPaVGT5c; gdxidpyhxdE=qMkjmuS34a8a0KBWeVuuPG%2FjiG%2B6qV%5CAZ9jobaXeHxaOcZXUm%2F2fttsrO6Oa%5CMctCyLSmyzZqCComMjZpmUO6hxZdCu1cd29nZ0J9n%2BAt9aooVWCsxWj03pGPrKKrKSkYSBX6r3iOiQoKLuE73tlSturmWahpG2cjPDOVDKBVUffiTqU%3A1752806519190; captcha_session_v2=2|1:0|10:1752805623|18:captcha_session_v2|88:UHlQREdwak9TYUpTWm5BaDh1UWhkRjl1RmJWQ1lRS1R2dVovalRseVE1amFXSHZrVXo2dTdsWWFmYk5OODhINw==|317636cb6dfe11099a09bf9b363ff55a4c374a3c593456342b81698aa42b670a; captcha_ticket_v2=2|1:0|10:1752805636|17:captcha_ticket_v2|728:eyJ2YWxpZGF0ZSI6IkNOMzFfRTVIOGVqcVdzZU9SWU1FUEk0Y3FmaCpaODNtajQwSTZ4QzVUeGtwaDVoanNlLkNWT1JySjZQbG01UFUzaGJXMUdmbHZQMkhnblRHQ0l0VHhSUTU0VGFTaERYdUlHVlNna3pZaVpMNE5OMGJVZFJqZGR1VXJITi5XS3NyMWdWUXNic1VsVVpGKmhYalFLbnRSOG8yREgqcXd2Q1VJeVdtYWs1WHg0NjBEcHZtZjBLbVp5UW45OSpMd2gqVnNfb21JbXMuQ2dHUmg5Q1N0OGVkKlFGTUdKVC5QM096UDFXSkc0SndQazZMZG1kSWpXUGwyRkExZnhmOXh2eTBFRzNRTEM5SEhwclF6b3AySGw1SjVyQ0EwZ1VlT1NZNVlONmxtS0JIem0ualprdTJwT254ZV91NWRaSDRFbUQ2a29zVEN2aFlVcThucnU5OENkUHouemZGdmhjNUwyc2dQMDVvZTlsbWtETzJvSDBZbXlGWDl6UEcwdndJQWdienhQOHNhWDJZLndmZFdUNEIxbzA1MnBOX0xJY21kczJLY254KjZSTDhOU2JvMG84SGdOYSo5U2VuVk0wdEszNkh3SE1HSXFHTmhmdE1PYlBDcGxIaGlncXRqTk9Ha0tJazZfWmt0NXAzaEUydEFkY0gucm9sQ29MNXhsaHVzTWM4aU5zV2hOKk9sY003N192X2lfMSJ9|7d638de55f7b5cf7bc76192207a97260d098a5f0cb026393894ecc0f9685bcdb; z_c0=2|1:0|10:1752805688|4:z_c0|92:Mi4xanp6RENnQUFBQUF4NHBQS0MyZW9HaVlBQUFCZ0FsVk5JZjltYVFEMTVWdklPWFpPakNMZ2RPX1RGVWNKcHUtQUNB|559cdae9b6186d162cc0e3b94b48897d20abd4335e398096e697609f45315518; q_c1=f7db7bd5164341e5a25a11dc3c94717c|1753093038000|1753093038000; __zse_ck=004_CTdfOzTdg=Oj9pZF4vsRN0U1yjqklQW9fViypMP0hmnboUJlgVcZvUh3zHFpfmtXUpBKJDkuQjcntqFNCGTD1eaGj=nLrtswFldhQThOYPIjXe4LMNe592dV3pulS/Cm-k8BPeeczwbuXAb3iu8XElSBNKrlF4sZUR8r2LsIC53NyqvwzqSX7gUIt029mMvfh87H5r6CBU9xpV+qwm6u+TCUQr/IQDoQR5b9Fuu69DkBZOYulGuvLyl+1nmt6/afn; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1753162962,1753335213,1753697532,1753767869; HMACCOUNT=3FE14D004BEE8D25; BEC=5ee33e0856ed13c879689106c041a08d; tst=r; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1753776635; SESSIONID=xGjzxwUhSRpN0RZAV77FxbWPgz9Sy3lsvl4n4iHLXJr; JOID=Vl0VBkoPLCK6JoamIZ-vNWop4Ao_dm5NxFvw9kxZf1fxEtH9Zfatq9MigqYgsbmF1tF0yBgfoA2AssssZvl951Y=; osd=UFsSA08JKiW_I4CgJpqqM2wu5Q85cGlIwV328UlceVH2F9T7Y_GortUkhaMlt7-C09Ryzh8apQuGtc4pYP964lM=";

    // 文章ID到URL的映射（支持专栏和回答）
    private static final Map<String, String> ARTICLE_TYPES = new HashMap<>();
    static {
        ARTICLE_TYPES.put("p", "https://zhuanlan.zhihu.com/p/"); // 专栏文章
        ARTICLE_TYPES.put("answer", "https://www.zhihu.com/question/"); // 问题回答
    }

    // https://www.zhihu.com/question/826050927/answer/1933261927208972608
    public static void main(String[] args) {
        String articleUrl = getArticleUrl("826050927/1933261927208972608", "answer");
        Document article = fetchArticle(articleUrl);

        if (article != null) {
            System.out.println("article:"+ JSON.toJSONString(article));
            ArticleData data = parseArticle(article);
            printArticleData(data);
        }
    }

    /**
     * 构建文章URL
     */
    private static String getArticleUrl(String id, String type) {
        String baseUrl = ARTICLE_TYPES.get(type);
        if (baseUrl == null) {
            throw new IllegalArgumentException("不支持的知乎文章类型: " + type);
        }
        return type.equals("answer") ? baseUrl + id.split("/")[0] + "/answer/" + id.split("/")[1] : baseUrl + id;
    }

    /**
     * 获取知乎文章内容
     */
    private static Document fetchArticle(String url) {
        try {
            // 随机选择用户代理
            int randomIndex = (int) (Math.random() * USER_AGENTS.length);

            return Jsoup.connect(url)
                    .header("Cookie", ZHIHU_COOKIE)
                    .header("User-Agent", USER_AGENTS[randomIndex])
                    .header("Referer", "https://www.zhihu.com/")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            System.err.println("获取知乎文章失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 解析知乎文章内容
     */
    private static ArticleData parseArticle(Document doc) {
        ArticleData data = new ArticleData();

        // 文章标题
        Element titleElement = doc.selectFirst("h1[class^=Post-Title]");
        if (titleElement != null) {
            data.title = titleElement.text();
        }

        // 作者信息
        Element authorElement = doc.selectFirst("a[href*=/people/], a[href*=/org/]");
        if (authorElement != null) {
            data.authorName = authorElement.text();
            data.authorUrl = authorElement.absUrl("href");
        }

        // 文章内容
        Element contentElement = doc.selectFirst("div[class^=Post-RichText]");
        if (contentElement != null) {
            data.content = contentElement.html(); // 保留HTML格式
        }

        // 发布时间
        Element timeElement = doc.selectFirst("div[class^=ContentItem-time] a");
        if (timeElement != null) {
            data.publishTime = timeElement.text();
        }

        // 点赞数
        Element voteElement = doc.selectFirst("button[class^=VoteButton]");
        if (voteElement != null) {
            String voteText = voteElement.text();
            data.voteCount = normalizeNumber(voteText);
        }

        return data;
    }

    /**
     * 格式化数字（将K、万等转换为整数）
     */
    private static int normalizeNumber(String text) {
        if (text.contains("K")) {
            return (int) (Double.parseDouble(text.replace("K", "")) * 1000);
        } else if (text.contains("万")) {
            return (int) (Double.parseDouble(text.replace("万", "")) * 10000);
        } else if (!text.isEmpty()) {
            return Integer.parseInt(text);
        }
        return 0;
    }

    /**
     * 打印文章数据
     */
    private static void printArticleData(ArticleData data) {
        System.out.println("==== 知乎文章详情 ====");
        System.out.println("标题: " + (data.title != null ? data.title : "N/A"));
        System.out.println("作者: " + (data.authorName != null ? data.authorName : "N/A"));
        System.out.println("作者主页: " + (data.authorUrl != null ? data.authorUrl : "N/A"));
        System.out.println("发布时间: " + (data.publishTime != null ? data.publishTime : "N/A"));
        System.out.println("点赞数: " + data.voteCount);
        System.out.println("\n文章内容:");
        System.out.println(data.content != null ? data.content : "N/A");
        System.out.println("====================");
    }

    /**
     * 文章数据结构
     */
    static class ArticleData {
        String title;
        String authorName;
        String authorUrl;
        String publishTime;
        String content;
        int voteCount;
    }
}