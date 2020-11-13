import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHelpTool {

    public static void main(String[] args) {
        try {
            System.out.println(getGHtmlCode2("https://www.xinshipu.com/caipu/17844/?page=1"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /*测试url是否有效*/
    static boolean urlIsValid(String httpUrl) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(httpUrl).openConnection();
        int state = connection.getResponseCode();
        if (!(state == 200)) {
            return false;
        } else return true;
    }

    /*另一种获取源代码*/
    static String getGHtmlCode2(String httpUrl) throws Exception {
        URL url = new URL(httpUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url
                .openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream(), "UTF-8"));// 得到输入流，即获得了网页的内容
        String line; // 读取输入流的数据，并显示
        String result = "";
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        return result;
    }


    /*获取html代码*/
    static String getGHtmlCode(String httpUrl) throws Exception {
        String content = "";
        URL url = new URL(httpUrl);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(20000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String input;
        while ((input = reader.readLine()) != null) {
            content += input;
        }
        reader.close();
        return content;
    }

    /*根据图片地址下载图片*/
    static void getHtmlPicture(String httpUrl, String filePath, String id) throws Exception {
        URL url = new URL(httpUrl.replace("http","https"));
        BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\XDS\\学习\\毕业设计\\Project\\Spider\\" + filePath + "\\" + id+".jpg"));
        int t;
        while ((t = inputStream.read()) != -1) {
            fileOutputStream.write(t);
        }
        fileOutputStream.close();
        inputStream.close();
    }

    /*从html中获取图片地址*/
    static void get(String url, String filePath) throws Exception {
        String searchImgReg = "(?x)(src|SRC|background|BACKGROUND)=('|\")/?(([\\w-]+/)*([\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";
        String searchImgReg2 = "(?x)(src|SRC|background|BACKGROUND)=('|\")(http://([\\w-]+\\.)+[\\w-]+(:[0-9]+)*(/[\\w-]+)*(/[\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";
        String caipuImageAddress = "(?x)(src|SRC|background|BACKGROUND)=('|\")(//([\\w-]+\\.)+[\\w-]+(:[0-9]+)*(/[\\w-]+)*(/[\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))";

        String content = getGHtmlCode(url);


        Pattern pattern = Pattern.compile(caipuImageAddress);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
//            getHtmlPicture("https:" + matcher.group(3), filePath);
        }
//        pattern = Pattern.compile(searchImgReg2);
//        matcher = pattern.matcher(content);
//        while (matcher.find()) {
//            getHtmlPicture(matcher.group(3), filePath);
//        }
//        pattern = Pattern.compile(searchImgReg);
//        matcher = pattern.matcher(content);
//        while (matcher.find()) {
//            System.out.println(matcher.group(3));
//            getHtmlPicture(url + "/" + matcher.group(3), filePath);
//        }
    }


}