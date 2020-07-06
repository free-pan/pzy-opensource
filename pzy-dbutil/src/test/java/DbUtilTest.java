import org.apache.commons.lang3.builder.ToStringExclude;
import org.pzy.opensource.dbutil.domain.bo.DbConnectionInfoBO;
import org.pzy.opensource.dbutil.support.util.ConnectionUtil;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DbUtilTest {

    @Test
    public void test() throws SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://10.100.96.55:3306/idr_attendance?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";
        String user = "idr";
        String password = "idr@135";
        DbConnectionInfoBO dbConnectionInfo = new DbConnectionInfoBO(driver, url, user, password);
        Connection connection = ConnectionUtil.getConnection(dbConnectionInfo, true);
        PreparedStatement preparedStatement = connection.prepareStatement("select user_id,name from idr_user.idr_recog_user where user_id not in (SELECT user_id FROM attendance_area_device) and delete_flag=0");
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String,String> userMap = new HashMap<>();
        while (resultSet.next()) {
            String userId = resultSet.getString("user_id");
            String name = resultSet.getString("name");
            userMap.put(userId,name);
        }
        resultSet.close();
        preparedStatement.close();
        ConnectionUtil.closeConnection(connection);
        connection = ConnectionUtil.getConnection(dbConnectionInfo, true);
        preparedStatement = connection.prepareStatement("insert into attendance_area_device(id,area_id,device_id,user_id,device_status,device_name,area_name,user_name,status,error_code,error_msg) values(?,?,?,?,?,?,?,?,?,?,?)");
        int total = userMap.size();
        System.out.println("总数:" + total);
        for (String userId : userMap.keySet()) {
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setString(2, "10000294");
            preparedStatement.setString(3, "");
            preparedStatement.setString(4,userId);
            preparedStatement.setInt(5,0);
            preparedStatement.setString(6,"");
            preparedStatement.setString(7, "航天科技大厦15层");
            preparedStatement.setString(8, userMap.get(userId));
            preparedStatement.setInt(9,1);
            preparedStatement.setString(10,"808003");
            preparedStatement.setString(11,"人员照片为空");
            preparedStatement.executeUpdate();
            System.out.println("剩余:" + (--total));
        }
        System.out.println("批处理执行完毕!");
        preparedStatement.close();
        ConnectionUtil.closeConnection(connection);
        System.out.println("连接关闭!");
    }
}
