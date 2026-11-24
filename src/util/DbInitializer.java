package util;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * DBTEST 데이터베이스 초기화 담당.
 *
 * 새 컴퓨터에서는 DBTEST 스키마와 user1 계정이 없기 때문에
 * "jdbc:mysql://localhost:3306/DBTEST" 로 접속하는 순간 Unknown database 오류가 난다.
 * 그래서 초기화는 항상 데이터베이스를 지정하지 않은 URL(서버 자체)로 root 접속해서 수행한다.
 */
public class DbInitializer {

    private static final String SQL_FILE = "db_init.sql";

    /**
     * 프로그램 시작 시 호출.
     * DBTEST.employee 테이블이 없으면(=처음 실행하는 컴퓨터) db_init.sql 을 자동 실행한다.
     *
     * @return true: DB 사용 가능 / false: 초기화 실패
     */
    public static boolean ensureDatabase() throws Exception {

        try (Connection conn = DBConnection.server()) {

            if (isInitialized(conn)) {
                return false; // 이미 준비됨, 초기화 안 함
            }

            runScript(conn);
            return true;      // 새로 초기화함
        }
    }

    /**
     * 관리자 화면의 "DB 초기화" 버튼에서 호출. 무조건 전체 재생성.
     *
     * @return 실행한 SQL 문 개수
     */
    public static int resetDatabase() throws Exception {

        try (Connection conn = DBConnection.server()) {
            return runScript(conn);
        }
    }

    private static boolean isInitialized(Connection conn) throws Exception {

        String sql =
                "SELECT COUNT(*) FROM information_schema.tables " +
                "WHERE table_schema = 'DBTEST' AND table_name = 'employee'";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    private static int runScript(Connection conn) throws Exception {

        Path path = findSqlFile();

        String sqlScript = Files.readString(path, StandardCharsets.UTF_8);

        sqlScript = sqlScript.replace("﻿", "");
        sqlScript = sqlScript.replaceAll("(?m)^\\s*--.*$", "");
        sqlScript = sqlScript.replaceAll("(?s)/\\*.*?\\*/", "");

        int executedCount = 0;

        try (Statement stmt = conn.createStatement()) {

            for (String sql : sqlScript.split(";")) {

                sql = sql.trim();

                if (sql.isEmpty()) {
                    continue;
                }

                stmt.execute(sql);
                executedCount++;
            }
        }

        return executedCount;
    }

    /**
     * 실행 위치(작업 디렉토리)에 따라 db_init.sql 을 못 찾는 문제 방지.
     * 현재 디렉토리 → 상위 디렉토리(bin/ 에서 실행한 경우) 순으로 찾는다.
     */
    private static Path findSqlFile() {

        Path[] candidates = {
                Path.of(SQL_FILE),
                Path.of("..", SQL_FILE)
        };

        for (Path p : candidates) {
            if (Files.exists(p)) {
                return p;
            }
        }

        throw new IllegalStateException(
                SQL_FILE + " 파일을 찾을 수 없습니다. 프로젝트 루트 폴더에서 실행하세요.\n"
                + "현재 위치: " + Path.of("").toAbsolutePath());
    }
}
