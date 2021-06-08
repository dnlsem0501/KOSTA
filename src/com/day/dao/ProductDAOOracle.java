package com.day.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.day.dto.Product;
import com.day.exception.FindException;
import com.day.sql.Myconnection;

public class ProductDAOOracle implements ProductDAO {
	
	public ProductDAOOracle() throws Exception{
		//JDBC드라이버 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("JDBC드라이버로드 성공");

	}
	@Override
	public List<Product> selectAll() throws FindException {
		Connection con = null;
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectALLSQL = "SELECT * FROM product ORDER BY prod_no ASC";  //ASC 는 오름차순
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(selectALLSQL);
			rs = pstmt.executeQuery(); 
			//rs.next(); 를 한번 실행해야 테이블의 첫번째 행을 읽는다. 코드를 읽을때 다음 행으로 커서가 이동하며 
											//이동한 행에 값이 있으면 true, 없으면 false
			while(rs.next()){
				String prod_no = rs.getString("prod_no");
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				java.sql.Date prod_mf_dt = rs.getDate("prod_mf_dt");
				
				Product p = new Product(prod_no, prod_name, prod_price, prod_mf_dt, null);
				list.add(p);
			}
			if(list.size()==0) {
				throw new FindException("상품이 없습니다.");
			}
			return list;
			
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			Myconnection.close(con, pstmt, rs);
		}
		
	}

	@Override
	public List<Product> selectAll(int currentPage) throws FindException {
		int cnt_per_page = 4; //페이지별 보여줄 목록수
								//전체건수 7건, 총 페이지수 2페이지
		Connection con = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		
		PreparedStatement pstmt = null;
		String selectALLPageSQL ="SELECT *\n"
				+ "FROM (SELECT rownum rn, a.*\n"
				+ "           FROM   (SELECT *\n"
				+ "                        FROM order_view \n"
				+ "                        --WHERE order_dt BETWEEN '21/01/01' AND '21/03/31' \n"
				+ "                        ORDER BY order_no DESC\n"
				+ "                       ) a\n"
				+ "          )\n"
				+ "WHERE rn BETWEEN START_ROW(?, ?) AND  END_ROW(?, ?)";
		try {
			pstmt = con.prepareStatement(selectALLPageSQL);
			pstmt.setInt(1, currentPage);
			pstmt.setInt(2, cnt_per_page);
			pstmt.setInt(3, currentPage);
			pstmt.setInt(4, cnt_per_page);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				String prod_no = rs.getString("prod_no");
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				java.sql.Date prod_mf_dt = rs.getDate("prod_mf_dt");
				
				Product p = new Product(prod_no, prod_name, prod_price, prod_mf_dt, null);
				list.add(p);
			}
			if(list.size()==0) {
				throw new FindException("상품이 없습니다.111");
			}
			return list;
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			Myconnection.close(con,pstmt,rs);
		}
	}

	@Override
	public Product selectByNo(String prod_no) throws FindException {
		Connection con = null;
		ResultSet rs = null;
		Product p = null;
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement pstmt = null;
		String selectByNoSQL = "SELECT prod_name, prod_price, prod_mf_dt FROM product "
				+ "WHERE product.prod_no = ?";
		try {
			pstmt = con.prepareStatement(selectByNoSQL);
			pstmt.setString(1, prod_no);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				java.sql.Date prod_mf_dt = rs.getDate("prod_mf_dt");
				
				p = new Product(prod_no, prod_name, prod_price, prod_mf_dt, null);

				}
			return p;
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			Myconnection.close(con,pstmt,rs);
		}
	}

	@Override
	public List<Product> selectByName(String word) throws FindException {
		Connection con = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();
		try {
			con = Myconnection.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PreparedStatement pstmt = null;
		String selectByNameSQL = "SELECT * FROM product WHERE prod_name LIKE ? "
				+ "ORDER BY prod_no";
		try {
			pstmt = con.prepareStatement(selectByNameSQL);
			pstmt.setString(1, "%"+word+"%");   //앞뒤로 %% 포인트
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String prod_no = rs.getString("prod_no");
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				java.sql.Date prod_mf_dt = rs.getDate("prod_mf_dt");
				
				Product p = new Product(prod_no, prod_name, prod_price, prod_mf_dt, null);
				list.add(p);
				}
			
			if(list.size()==0) {
				throw new FindException("상품이 없습니다.333");
			}
			return list;
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			Myconnection.close(con,pstmt,rs);
		}
	}

	public static void main(String [] args) {
		//selectAll();
		//product테이블의 모든행 출력
//		try {
//			ProductDAOOracle dao = new ProductDAOOracle();
//			List<Product> all = dao.selectAll();
//			for(Product p:all) {
//				System.out.println(p); //p.toString() 자동 호출
//			}
//		} catch (FindException e){ //
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
		
		//selectAll(2);
		//한페이지 최대 4행, 2번째 페이지 출력(5~8)
//		try {
//			ProductDAOOracle dao = new ProductDAOOracle();
//			List<Product> all = dao.selectAll(2);
//			for(Product p:all) {
//				System.out.println(p); //p.toString() 자동 호출
//			}
//		} catch (FindException e){ //
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
		//selectByNo("C0001");
		//prod_no가 'C0001'인 행 출력
//		try {
//			ProductDAOOracle dao = new ProductDAOOracle();
//			Product p = dao.selectByNo("C0001");
//			System.out.println(p); //p.toString() 자동 호출
//		
//		} catch (FindException e){ //
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
		//selectByName(word);
		//prod_name에 'word'가 포함된 목록 출력
		try {
			ProductDAOOracle dao = new ProductDAOOracle();
			List<Product> all = dao.selectByName("아메");
			for(Product p:all) {
				System.out.println(p); //p.toString() 자동 호출
			}
		} catch (FindException e){ //
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
