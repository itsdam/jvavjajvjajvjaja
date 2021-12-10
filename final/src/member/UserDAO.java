package member;


import java.io.IOException;
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
public class UserDAO {
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;
    
    public UserDAO() {
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
    
    //로그인 기능구현
    public int login(String userID, String userPassword) {
        String sql = "SELECT userPassword FROM USER WHERE userID = ?";
        try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userID);
                rs = pstmt.executeQuery();
                if(rs.next()) {
                    if(rs.getString(1).equals(userPassword)) {
                        return 1; //로그인 성공
                    }else
                        return 0; //비밀번호 틀림
                }
                return -1; //아이디 없음
        }catch (Exception e) {
                e.printStackTrace();
        }
        return -2; //데이터베이스오류
    }

    //회원가입 기능 구현
    public int join(User user) {
        String sql = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
        try {
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, user.getUserID());
          pstmt.setString(2, user.getUserName());
          pstmt.setString(3, user.getUserPassword());
          pstmt.setString(4, user.getUserBirth());
          pstmt.setString(5, user.getUserEmail());
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

    //아이디 중복 확인 메소드
    public int checkUserID(String userID) {
        String sql = "SELECT userID FROM USER WHERE userID = ?";
        try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userID);
                rs = pstmt.executeQuery();
                if(rs.next()) {
                    if(rs.getString(1).equals(userID)) {
                            return 1; //아이디 중복
                    }
                }
        }catch(Exception e){
                e.printStackTrace();
        }
        return 0; //아이디 중복 아님
    }
    
    //이메일 중복 확인 메소드
    public int checkUserEmail(String userEmail) {
        String sql = "SELECT userEmail FROM USER WHERE userEmail = ?";
        try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userEmail);
                rs = pstmt.executeQuery();
                if(rs.next()) {
                    if(rs.getString(1).equals(userEmail)) {
                            return 1; //이메일 중복
                    }
                }
        }catch(Exception e){
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
        return 0; //이메일 중복 아님
    }
    
    //아이디 변경
    public String changeID(String curID, String newID, String passwd){
        int userCk = login(curID, passwd); //1:성공 0:비밀번호틀림 -1:아이디없음
        if(userCk != 1) return "아이디와 비밀번호를 다시 확인해주세요.";
        
        int idCk = checkUserID(newID); //1:중복 0:중복아님
        if(idCk != 0) return "이미 존재하는 아이디입니다.";
        
        String sql = "UPDATE USER SET userID = ? WHERE userID = ?";
        try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, newID);
                pstmt.setString(2, curID);
                if(pstmt.executeUpdate()>0) return "아이디 변경에 성공했습니다.";
        }catch(Exception e){
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
        return "오류가 발생했습니다.";
    }
    
    //비밀번호 변경
    public String changePassword(String curID, String curPasswd, String NewPasswd){
        int userCk = login(curID, curPasswd); //1:성공 0:비밀번호틀림 -1:아이디없음
        if(userCk != 1) return "아이디와 비밀번호를 다시 확인해주세요.";
        
        String sql = "UPDATE USER SET userPassword = ? WHERE userID = ?";
        try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, NewPasswd);
                pstmt.setString(2, curID);
                if(pstmt.executeUpdate()>0) return "비밀번호 변경에 성공했습니다.";
        }catch(Exception e){
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
        return "오류가 발생했습니다.";
    }
    
    //아이디 찾기
    public String srchUserID(String userEmail) {
        String sql = "SELECT userID FROM USER WHERE userEmail = ?";
            try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userEmail);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getString(1); // 아이디 찾기 성공
            }else {
                return null; //아이디 찾기 실패
            }
        }catch(Exception e){
                e.printStackTrace();
        }
          return ""; //데이터베이스 오류      
    }
    //비밀번호 찾기
    public String srchUserPasswd(String userID, String userEmail) {
        String sql = "SELECT userPassword FROM USER WHERE userID = ? AND userEmail = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            pstmt.setString(2, userEmail);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getString(1); // 비밀번호 찾기 성공
            }else {
                return null; //비밀번호 찾기 실패
            }
        }catch(Exception e){
                e.printStackTrace();
        }
      return ""; //데이터베이스 오류
    }
    
    public int delete(String userID, String userPassword) {
        int result = this.login(userID, userPassword); //1:일치 0:불일치 -1:아이디없음 -2:데이터베이스오류
        if(result == 1) {
            String sql = "DELETE FROM USER WHERE userID = ?";
            try {
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, userID);
                    return pstmt.executeUpdate();
            }catch(Exception e){
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
        }else if(result != -2) {
                return 0; //아이디, 비밀번호 불일치
        }
        return -1; //디비오류
    }
}
