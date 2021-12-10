package member;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class FavMenuDAO {
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;
    
    public FavMenuDAO() {
        try {
                String strURL = "jdbc:mysql://127.0.0.1:3306/java?characterEncoding=UTF-8&serverTimezone=UTC";
                String strUser = "root";
                //String strPWD = "rabbitsum#9";
                String strPWD = "tooj0521^^";
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(strURL, strUser, strPWD);
        }catch(Exception e){
            System.out.println("SQLException :"+ e.getMessage());
        }
    }
    
    //ID 부여 메소드
    public int getNextID() {
            // 내림차순으로 조회하여 가장 마지막 id를 구한다
            String sql = "SELECT id FROM favmenu ORDER BY id DESC";
            try {
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return rs.getInt(1) + 1; //기존id + 1
                    }
                    return 1; // 첫 번째인 경우
            } catch (Exception e) {e.printStackTrace();}
            return -1; // 데이터베이스 오류
    }
    //선호 결제 방법 추가 메소드
    public int insertMenu(String userID, String kind) {
        String sql = "INSERT INTO FAVMENU VALUES (?, ?, ?)";
        try {
          pstmt = conn.prepareStatement(sql);
          pstmt.setInt(1, getNextID());
          pstmt.setString(2, kind);
          pstmt.setString(3, userID);
          return pstmt.executeUpdate(); //0:실패 1:성공
        }catch (Exception e) {
              e.printStackTrace();
        }finally{
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return -1; //데이터베이스 오류
    }
}
