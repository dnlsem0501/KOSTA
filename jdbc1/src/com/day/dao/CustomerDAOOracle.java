package com.day.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.day.dto.Customer;
import com.day.dto.Product;
import com.day.exception.AddException;
import com.day.exception.FindException;
import com.day.exception.ModifyException;
import com.day.sql.Myconnection;

public class CustomerDAOOracle implements CustomerDAO {

	public CustomerDAOOracle() throws Exception{
		Class.forName("oracle.jdbc.driver.OracleDriver");
//		System.out.println("JDBC드라이버로드 성공");
	}
	@Override
	public void insert(Customer c) throws AddException {
		Connection con = null;
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}

		String insertSQL = "INSERT INTO customer(id,pwd,name,buildingno,enabled) VALUES(?,?,?,?,1)";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, c.getId());
			System.out.println(c.getId());
			pstmt.setString(2, c.getPwd());
			pstmt.setString(3, c.getName());
			pstmt.setString(4, c.getBuildingno());
			int rowcnt = pstmt.executeUpdate();
			if(rowcnt==1) {
				System.out.println("추가성공");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(2);
		}finally {
			Myconnection.close(con, pstmt, null);
		}
		
	}

	@Override
	public Customer selectById(String id) throws FindException {
		Connection con = null;
		ResultSet rs = null;
		Customer c = null;
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement pstmt = null;
		String selectByID = "SELECT * FROM customer WHERE id = ?";
		try {
			pstmt = con.prepareStatement(selectByID);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				String user_pwd = rs.getString("pwd");
				String user_name = rs.getString("name");
	
				c = new Customer(id, user_pwd, user_name);

				}
			return c;
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			Myconnection.close(con, pstmt, rs);
		}
				
	}

	@Override
	public void update(Customer c) throws ModifyException {
		Connection con = null;
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}
		String updateSQL = "UPDATE customer SET ";
		if(c.getEnabled()==1) {
			if(!"".equals(c.getPwd())&&!"".equals(c.getName())&&!"".equals(c.getBuildingno())) {
				//1.다바꾼다
				updateSQL +="pwd='"+c.getPwd()+"', name='"+c.getName()
				+"', buildingno='"+c.getBuildingno()+"' WHERE ID='"+c.getId()+"'";
			}else if(!"".equals(c.getPwd())&&!"".equals(c.getName())){
				//2.비번,이름 바꾼다
				updateSQL +="pwd='"+c.getPwd()+"', name='"+c.getName()+"' WHERE ID='"+c.getId()+"'";
			}else if(!"".equals(c.getPwd())&&!"".equals(c.getBuildingno())) {
				//3.비번, 빌딩넘 바꾼다
				updateSQL +="pwd='"+c.getPwd()+"', buildingno='"+c.getBuildingno()+"' WHERE ID='"+c.getId()+"'";
			}else if(!"".equals(c.getName())&&!"".equals(c.getBuildingno())) {
				//4.이름,빌딩넘 바꾼다
				updateSQL +="name='"+c.getName()+"', buildingno='"+c.getBuildingno()+"' WHERE ID='"+c.getId()+"'";
			}else if(!"".equals(c.getPwd())) {
				//5.비번만바꾼다
				updateSQL +="pwd='"+c.getPwd()+"' WHERE ID='"+c.getId()+"'";
			}else if(!"".equals(c.getName())) {
				//6.이름만바꾼다
				updateSQL +="name='"+c.getName()+"' WHERE ID='"+c.getId()+"'";
			}else if(!"".equals(c.getBuildingno())) {
				//7.빌딩넘만바꾼다
				updateSQL +="buildingno='"+c.getBuildingno()+"' WHERE ID='"+c.getId()+"'";
			}else {
				//안바꾼다;
				updateSQL = "";
			}
		}else if(c.getEnabled()==0) {
			//탈퇴를한다
			updateSQL +="enabled=0 WHERE ID='"+c.getId()+"'";
		}
		Statement stmt = null;
		try {
			if(updateSQL=="") {
				System.out.println("정보를 수정하지 않았습니다");
				return;
			}
			stmt = con.createStatement();
			int rowcnt = stmt.executeUpdate(updateSQL);
			if(rowcnt==1) {
				System.out.println(c.getId()+"의 정보 수정");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Myconnection.close(con,stmt,null);
		}
	}
	public static void main(String[] args) {
		
		//INSER
//		System.out.println("ID 입력하세요");
//		Scanner sc = new Scanner(System.in);
//		String input_id= sc.nextLine();
//		System.out.println("입력ID:"+input_id);
//		System.out.println("PWD 입력하세요");
//		String input_pwd= sc.nextLine();
//		System.out.println("입력PWD:"+input_pwd);
//		System.out.println("NAME 입력하세요");
//		String input_name= sc.nextLine();
//		System.out.println("입력NAME:"+input_name);
//		Customer c = new Customer(input_id, input_pwd, input_name);
//		try {
//			CustomerDAOOracle dao = new CustomerDAOOracle();
//			dao.insert(c);
//		} catch (AddException e) {
//		}catch (Exception e) {
//		}
		
//		selectById(id)검색
//		try {
//			CustomerDAOOracle dao = new CustomerDAOOracle();
//			Customer c = dao.selectById("id8");
//			System.out.println(c); //p.toString() 자동 호출
//		
//		} catch (FindException e){ //
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
		
		//UPDATE
//		Scanner sc = new Scanner(System.in);
//		System.out.println("아이디:");
//		String idValue = sc.nextLine();
//		System.out.print("비밀번호를 변경하려면 값을 입력하세요.[변경을 원하지 않으면 enter를 누르세요.]:");
//		String pwdValue = sc.nextLine(); //enter를 누른경우 pwdValue변수값은 ""가 된
//		System.out.println("입력한 비밀번호값 :[" + pwdValue + "]");
//		System.out.print("이름을 변경하려면 값을 입력하세요.[변경을 원하지 않으면 enter를 누르세요.]:");
//		String nameValue = sc.nextLine(); //enter를 누른경우 pwdValue변수값은 ""가 된
//		System.out.println("입력한 이름값 :[" + nameValue + "]");
//		System.out.print("빌딩넘버를 변경하려면 값을 입력하세요.[변경을 원하지 않으면 enter를 누르세요.]:");
//		String buildingnoValue = sc.nextLine(); //enter를 누른경우 pwdValue변수값은 ""가 된
//		System.out.println("입력한 빌딩넘버값 :[" + buildingnoValue + "]");
//		Customer c = new Customer (idValue, pwdValue, nameValue, buildingnoValue);
//		try {
//			CustomerDAOOracle dao = new CustomerDAOOracle();
//			dao.update(c);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//DELETE
		Scanner sc = new Scanner(System.in);
		System.out.println("아이디:");
		String idValue = sc.nextLine();
		System.out.println("계정 삭제를 원하시면 0,원하지 않으면 enter를 누르세요");
		int askDelete = sc.nextInt();
		if(askDelete==0) {
			Customer c=new Customer(idValue,askDelete);
			try {
				CustomerDAOOracle dao = new CustomerDAOOracle();
				dao.update(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
