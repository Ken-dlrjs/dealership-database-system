import javax.swing.JOptionPane;

import ui.LoginFrame;
import util.DbInitializer;

public class Main {

    public static void main(String[] args) {

        // 처음 실행하는 컴퓨터라면 DBTEST 스키마/테이블/user1 계정/샘플 데이터를 먼저 만든다.
        // (로그인 자체가 DBTEST.employee 테이블을 조회하므로 로그인 전에 반드시 필요)
        try {

            if (DbInitializer.ensureDatabase()) {
                JOptionPane.showMessageDialog(
                        null,
                        "DBTEST 데이터베이스가 없어 샘플 데이터로 새로 생성했습니다.");
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "MySQL 접속 또는 DB 생성 실패\n\n"
                    + "- MySQL 서버(localhost:3306)가 실행 중인지\n"
                    + "- root 비밀번호가 1234 인지\n"
                    + "- mysql-connector-j 가 빌드 경로에 있는지 확인하세요.\n\n"
                    + ex.getMessage(),
                    "DB 오류",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        new LoginFrame();
    }
}
