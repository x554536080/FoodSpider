
import Entities.FoodItem;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTool {


    /*从分类页获取caipu的Id*/
    static void findCaipuId(String htmlInput) {
        String caipuRegex = "(caipu/)(\\d+)/";
        Pattern pattern = Pattern.compile(caipuRegex);
        Matcher matcher = pattern.matcher(htmlInput);
        while (matcher.find()) {
            FoodSpider.caipuIds.add(matcher.group(2));
        }
    }

    /*获得具体caipu里面的名称*/
    static String findCaipuName(String htmlInput) {
        String caipuRegex2 = "type=\"text\"\\s+value=\"([\\u4e00-\\u9fa5]*)\"";

        String caipuRegex = "speech\\s+value=\"([\\u4e00-\\u9fa5]*)\"";
        Pattern pattern = Pattern.compile(caipuRegex2);
        Matcher matcher = pattern.matcher(htmlInput);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "未分类";
    }

    /*获取具体caipu里面的食物信息*/
    static ArrayList<FoodItem> findFoodItem(String htmlInput) {
        String caipuRegex1 = "\"/zuofa/(\\d+)\"";

        String caipuRegex = "(zuofa/)(\\d+)\"\\stitle=\"([\\u4e00-\\u9fa5]*)";
        Pattern pattern = Pattern.compile(caipuRegex1);
        Matcher matcher = pattern.matcher(htmlInput);
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        while (matcher.find()) {
            foodItems.add(new FoodItem("https://www.xinshipu.com/zuofa/" + matcher.group(1)));
        }
        return foodItems;
    }

    /*从食物item中获取需要的信息*/
    static FoodItem findFoodInfo(String htmlInput, String type) {
        String fId, fname, ftag, fintro;
        String idRegex = "name=\"shipuid\"\tvalue=\"(\\d+)\"";
        String nameRegex = "\"Recipe\",\\s+\"name\":\\s+\"([^/>]*)\",";
        //"name=\"shipuname\"\tvalue=\"([^/>]*)\"";
        String tagRegex = "关键词((.*)([\\u4e00-\\u9fa5]*)).*clearfix\\s+store-block";
        String tagRegex1 = "[^\\u4e00-\\u9fa5]*?([\\u4e00-\\u9fa5]+)";
        String infoRegex = "\"description\":\\s*\"([^\"]*)";
        Pattern pattern = Pattern.compile(infoRegex);
        Matcher matcher = pattern.matcher(htmlInput);
        fintro = "这道菜目前还没有介绍哦！";
        if (matcher.find()) {
            fintro = matcher.group(1);
            if (fintro.equals("")) fintro = "这道菜目前还没有介绍哦！";
            if (!checkIfExistChineseCharacter(fintro)) fintro = "这道菜目前还没有介绍哦！";
        }
        pattern = Pattern.compile(idRegex);
        matcher = pattern.matcher(htmlInput);
        if (matcher.find()) {
            fId = matcher.group(1);
        } else return null;
        pattern = Pattern.compile(nameRegex);
        matcher = pattern.matcher(htmlInput);
        if (matcher.find()) {
            fname = matcher.group(1);
        } else return null;
        pattern = Pattern.compile(tagRegex);
        matcher = pattern.matcher(htmlInput);
        ftag = "";
        String temp = "";
        if (matcher.find()) {
            temp = matcher.group(1).replace(" ", "");
            temp.replace("\t", "");
            pattern = Pattern.compile(tagRegex1);
            matcher = pattern.matcher(temp);
            while (matcher.find()) {
//                ftag = matcher.group(1);
                ftag += matcher.group(1) + "%";
            }
        }
        return new FoodItem(fId, fname, type, ftag, fintro);
    }

    /*返回食物item页面里的图片地址*/
    static String findFoodPicId(String input) {
        String regex = "image\":\\s+\"([^\"]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else return null;
    }

    /*用来检测是否包含了汉字*/
    static boolean checkIfExistChineseCharacter(String s) {
        return !(s.length() == s.getBytes().length);
    }

    /*用来检测是否全为汉字*/
    static boolean checkIfAllChineseCharacter(String s) {
        String reg = "[\\u4e00-\\u9fa5]+";
        return s.matches(reg);
    }


    public static void main(String[] args) {
        try {
            String q = "";
            String r = "(.*?hh)";
            String i = "xds12151315hh1241223hh1231224hh123123xds";
            Pattern pattern = Pattern.compile(r);
            Matcher matcher = pattern.matcher(i);
            while (matcher.find()) {
                q += matcher.group() + "%";
            }
            FoodItem foodItem = findFoodInfo(HttpHelpTool.getGHtmlCode("https://www.xinshipu.com/zuofa/694467"), "家常菜");
//            String i = findFoodPicId(HttpHelpTool.getGHtmlCode("https://www.xinshipu.com/zuofa/694467"));
            int i1 = 0;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}