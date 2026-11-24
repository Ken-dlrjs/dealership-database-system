package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB 접속 정보를 한 곳에서 관리한다.
 * (기존에는 화면 클래스마다 URL/계정/비밀번호가 하드코딩되어 있었음)
 */
public class DBConnection {

    public static final String HOST_URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME  = "DBTEST";

    /** 관리자용 MySQL 계정 (DBTEST 전체 권한) */
    public static final String ADMIN_USER = "root";
    public static final String ADMIN_PW   = "1234";

    /** 세일즈맨용 MySQL 계정 (GRANT로 제한된 권한) */
    public static final String SALES_USER = "user1";
    public static final String SALES_PW   = "1234";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "MySQL JDBC 드라이버가 없습니다. lib/ 폴더의 mysql-connector-j jar를 확인하세요.", e);
        }
    }

    /** root 계정으로 DBTEST 접속 */
    public static Connection admin() throws SQLException {
        return DriverManager.getConnection(HOST_URL + DB_NAME, ADMIN_USER, ADMIN_PW);
    }

    /** user1 계정으로 DBTEST 접속 */
    public static Connection salesman() throws SQLException {
        return DriverManager.getConnection(HOST_URL + DB_NAME, SALES_USER, SALES_PW);
    }

    /** 데이터베이스를 지정하지 않고 서버에 root로 접속 (DBTEST가 없을 때 초기화용) */
    public static Connection server() throws SQLException {
        return DriverManager.getConnection(HOST_URL, ADMIN_USER, ADMIN_PW);
    }
}
