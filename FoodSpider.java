
import Entities.FoodItem;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodSpider {

    static String fPath = "spider"; // 储存网页文件的目录名
    static String homePage = "https://www.xinshipu.com/chuyoufenlei/";
    static ArrayList<String> caipuIds = new ArrayList<>();

    static private ArrayList<String> arrUrls = new ArrayList<String>(); // 存储未处理URL
    static private ArrayList<String> arrUrl = new ArrayList<String>(); // 存储所有URL供建立索引
    static private Hashtable<String, Integer> allUrls = new Hashtable<String, Integer>(); // 存储所有URL的网页号
    static private Hashtable<String, Integer> deepUrls = new Hashtable<String, Integer>(); // 存储所有URL深度

    static synchronized String getAUrl() {
        String tmpAUrl = arrUrls.get(0);
        arrUrls.remove(0);
        return tmpAUrl;
    }

    public static void main(String[] args) {
        try {
            /*对厨友分类的20页进行循环*/
            for (int homePageNumber = 1; homePageNumber <= 20; homePageNumber++) {
                String homePageContent = "";
                homePageContent = HttpHelpTool.getGHtmlCode(homePage + "?page=" + homePageNumber);
                RegexTool.findCaipuId(homePageContent);

//                String tmp = getAUrl();
//                getWebByUrl(tmp, allUrls.get(tmp) + ""); // 对新URL所对应的网页进行抓取
            }

            for (int i = 0; i < 308; i++)
            {
                caipuIds.remove(0);}
            /*针对每个caipuId进行爬取*/
            for (String caipuId : caipuIds) {
                String firstPageOfThisCaipu = HttpHelpTool.getGHtmlCode("https://www.xinshipu.com/caipu/" + caipuId);
                String caipuName = RegexTool.findCaipuName(firstPageOfThisCaipu);
                System.out.println("类别" + caipuName);
                if (caipuName.equals("未分类")) continue;
                MySQLHelper.addingAType(caipuName);
                File fDir = new File("D:\\XDS\\学习\\毕业设计\\Project\\Spider" + "\\" + caipuName);
                if (!fDir.exists()) {
                    fDir.mkdir();
                }

                int caipuPageNumber = 1;
                String caipuPageUrl = "https://www.xinshipu.com/caipu/" + caipuId + "/?page=" + caipuPageNumber;
                ArrayList<FoodItem> foods = new ArrayList<>();
                /*从第一页开始爬取*/
                while (HttpHelpTool.urlIsValid(caipuPageUrl)) {
                    System.out.println(caipuName + "第" + caipuPageNumber + "页");
                    foods.clear();
                    String caipuPageContent = HttpHelpTool.getGHtmlCode(caipuPageUrl);
                    foods = RegexTool.findFoodItem(caipuPageContent);
                    String foodItemContent;
                    /*对该页的所有foodItem进行获取*/
                    for (FoodItem foodItem : foods) {
                        if (HttpHelpTool.urlIsValid(foodItem.url)) {
                            foodItemContent = HttpHelpTool.getGHtmlCode(foodItem.url);
                            FoodItem foodAdder = RegexTool.findFoodInfo(foodItemContent, caipuName);
                            String picUrl = RegexTool.findFoodPicId(foodItemContent);
                            if (foodAdder != null && picUrl != null && RegexTool.checkIfAllChineseCharacter(foodAdder.fname)) {
                                /*数据库添加食物信息，重复判断*/
                                if (MySQLHelper.addFood(foodAdder.fid, foodAdder.fname, foodAdder.ftype, foodAdder.ftag, foodAdder.fintro)) {
                                    /*下载食物图片*/
//                                    HttpHelpTool.getHtmlPicture(picUrl, caipuName, foodAdder.fid);
                                } else MySQLHelper.updatingFoodTags(foodAdder.fid, caipuName);
                            }
                        }
                    }

                    caipuPageNumber++;
                    caipuPageUrl = "https://www.xinshipu.com/caipu/" + caipuId + "/?page=" + caipuPageNumber;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void getHomePageInfo(int pageNumber) {


    }

    static void getWebByUrl(String strUrl, String fileIndex) {
        try {
            URL url = new URL(strUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            InputStream is = url.openStream();
            File file = new File(fPath + "\\" + "pic");
            if (!file.exists()) {
                file.mkdir();
            }
            MySQLHelper helper = new MySQLHelper();/**差一个数据库存储操作*/
            HttpHelpTool.get(strUrl, fPath + "\\" + "pic");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
