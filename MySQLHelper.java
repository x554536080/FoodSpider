import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLHelper {
    static Connection connection = null;
    static PreparedStatement prepareStatementForAddingFood = null;
    static PreparedStatement prepareStatementForAddingTag = null;
    static PreparedStatement prepareStatementForGettingTagId = null;
    static PreparedStatement prepareStatementForGettingFoodId = null;
    static PreparedStatement prepareStatementForAddingFoodTag = null;
    static PreparedStatement prepareStatementForUpdatingFoodTags = null;
    static PreparedStatement prepareStatementForGettingFoodTags = null;
    static PreparedStatement prepareStatementForAddingAType = null;

    static PreparedStatement preparedStatementForFetchingAllTypes = null;



    static {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);
            String url = "jdbc:mysql://localhost:3306/food_recommend?serverTimezone=UTC";
            String user = "root";
            String password = "554536080";
            connection = DriverManager.getConnection(url, user, password);
            if (!connection.isClosed()) {
                System.out.println("数据库成功连接");
            }
            String addFood = "insert into foods (fid,fname,ftype,ftag,fintro) values (?,?,?,?,?)";
            String addTag = "insert into tags (tname) values (?)";
            String gettingTagId = "select tid from tags where tname = ?";
            String gettingFoodId = "select fid from foods where fname = ?";
            String gettingFoodTags = "select ftag from foods where fid = ?";
            String addFoodTag = "insert into ftags (fid,tid) values (?,?)";
            String updatingFoodTags = "update foods set ftag = ? where fid = ?";
            String addingAType = "insert into ftypes (typename) values (?)";
            String fetchingTypes = "select ftype from foods";


            prepareStatementForGettingFoodId = connection.prepareStatement(gettingFoodId);
            prepareStatementForGettingTagId = connection.prepareStatement(gettingTagId);
            prepareStatementForAddingTag = connection.prepareStatement(addTag);
            prepareStatementForAddingFood = connection.prepareStatement(addFood);
            prepareStatementForAddingFoodTag = connection.prepareStatement(addFoodTag);
            prepareStatementForUpdatingFoodTags = connection.prepareStatement(updatingFoodTags);
            prepareStatementForGettingFoodTags = connection.prepareStatement(gettingFoodTags);
            prepareStatementForAddingAType = connection.prepareStatement(addingAType);

            preparedStatementForFetchingAllTypes = connection.prepareStatement(fetchingTypes);

        } catch (
                Exception e1) {
            e1.printStackTrace();
        }
    }

    static void addingAType(String type){
        try {
            prepareStatementForAddingAType.setString(1,type);
            prepareStatementForAddingAType.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean addFood(String fid, String fname, String ftype, String ftag, String fintro) {
        try {
            prepareStatementForAddingFood.setString(1, fid);
            prepareStatementForAddingFood.setString(2, fname);
            prepareStatementForAddingFood.setString(3, ftype);
            prepareStatementForAddingFood.setString(4, ftag);
            prepareStatementForAddingFood.setString(5, fintro);
            prepareStatementForAddingFood.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static void addTag(String tname) {
        try {
            prepareStatementForAddingTag.setString(1, tname);
            prepareStatementForAddingTag.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static List<String> fetchingTypes() {
        try {
            ResultSet resultSet = preparedStatementForFetchingAllTypes.executeQuery();
            List<String> results = new ArrayList<>();
            while (resultSet.next()){
                results.add(resultSet.getString("ftype"));
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String gettingFoodTags(String fid) {
        try {
            prepareStatementForGettingFoodTags.setString(1, fid);
            ResultSet resultSet = prepareStatementForGettingFoodTags.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("ftag");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    static void updatingFoodTags(String fid, String tname) {
        try {
            String tags = gettingFoodTags(fid);
            if(!tags.contains(tname+"%"))
                tags += tname + "%";
            prepareStatementForUpdatingFoodTags.setString(1, tags);
            prepareStatementForUpdatingFoodTags.setString(2, fid);
            prepareStatementForUpdatingFoodTags.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static int gettingTagId(String tname) {
        try {
            prepareStatementForGettingTagId.setString(1, tname);
            ResultSet resultSet = prepareStatementForGettingTagId.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("tid");
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }


    public static void main(String[] args) {
        try {
            ArrayList<String> types = (ArrayList<String>) fetchingTypes();
            for(String type:types)
            addingAType(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
