package com.day.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.day.dto.Customer;
import com.day.dto.OrderInfo;
import com.day.dto.OrderLine;
import com.day.dto.Product;
import com.day.exception.AddException;
import com.day.exception.FindException;
import com.day.sql.Myconnection;

public class OrderDAOOracle implements OrderDAO {

	@Override
	public void insert(OrderInfo info) throws AddException {
		Connection con = null;
		try {
			con=Myconnection.getConnection();
			con.setAutoCommit(false);//자동커밋해제
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}
		
		try {
			insertInfo(con, info); //기본정보
			insertLines(con, info.getLines()); //상세정보
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception e2) {
				
			}
			throw new AddException(e.getMessage());
		}finally {
			Myconnection.close(con, null, null);
		}
		
	}
	/**
	 * 주문기본정보 추가한다
	 * @param con	DB연결객체
	 * @param info	주문기본정보
	 * @throws AddException
	 */
	private void insertInfo(Connection con, OrderInfo info) throws AddException{
		//SQL송신
		PreparedStatement pstmt=null;
		String insertInfoSQL="INSERT INTO order_info(order_no, order_id) "
				+ "VALUES (ORDER_SEQ.NEXTVAL, ?)";
		try {
			pstmt=con.prepareStatement(insertInfoSQL);
			pstmt.setString(1, info.getOrder_c().getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException("주문기본추가실패"+e.getMessage());
		}finally {
			Myconnection.close(null, pstmt, null);
		}
	}
	/**
	 * 주문상세정보 추가한다
	 * @param con
	 * @param lines
	 * @throws AddException
	 */
	private void insertLines(Connection con, List<OrderLine> lines) throws AddException{
		PreparedStatement pstmt=null;
		String insertLineSQL = "INSERT INTO order_line(order_no, order_prod_no, order_quantity) "
				+ "VALUES (ORDER_SEQ.CURRVAL, ?, ?)";
		
		try {
			pstmt=con.prepareStatement(insertLineSQL);
			for(OrderLine line : lines) {
				pstmt.setString(1, line.getOrder_p().getProd_no());
				pstmt.setInt(2, line.getOrder_quantity());
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException("주문상세 추가실패"+e.getMessage());
		}finally {
			Myconnection.close(null, pstmt, null);
		}
		
	}
	
	@Override
	public List<OrderInfo> selectById(String id) throws FindException {
		Connection con = null;
		try {
			con=Myconnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectById="SELECT oi.order_no, order_dt, order_prod_no,  prod_name, prod_price, order_quantity \n"
				+ "FROM order_info oi JOIN order_line ol ON(oi.order_no = ol.order_no)\n"
				+ "JOIN product p  ON (ol.order_prod_no = p.prod_no)\n"
				+ "WHERE order_id = ? "
				+ "ORDER BY oi.order_no DESC, order_prod_no";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrderInfo> list = new ArrayList<>();
		try {
			pstmt=con.prepareStatement(selectById);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				OrderInfo info = new OrderInfo();
				int no=rs.getInt("order_no");
				info.setOrder_no(no);
				info.setOrder_dt(rs.getDate("order_dt"));
				OrderLine line = new OrderLine();
				List<OrderLine> linelist = new ArrayList<>();
				line.setOrder_no(no);
				Product p = new Product();
				p.setProd_no(rs.getString("order_prod_no"));
				p.setProd_name(rs.getString("prod_name"));
				p.setProd_price(rs.getInt("prod_price"));
				line.setOrder_p(p);
				line.setOrder_quantity(rs.getInt("order_quantity"));
				linelist.add(line);
				info.setLines(linelist);
				list.add(info);
				System.out.println(info);
			}
			if(list.size()==0) {
				throw new FindException("주문내역이 없습니다");
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			Myconnection.close(con, pstmt, rs);
		}
	}
	
	public static void main(String [] args) {
		OrderDAOOracle dao = new OrderDAOOracle();
		try {
			dao.selectById("id3");
		} catch (FindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//		OrderDAOOracle dao = new OrderDAOOracle();
//		dao.selectById("id1");
//		OrderInfo info = new OrderInfo();
//		Customer c = new Customer();
//		c.setId("id1");
//		info.setOrder_c(c);
//		
//		List<OrderLine> lines = new ArrayList<>();
//		for(int i=1;i<=3;i++) {
//			OrderLine line = new OrderLine();
//			Product p = new Product();
//			p.setProd_no("C000"+i);
//			line.setOrder_p(p);
//			line.setOrder_quantity(i);
//			lines.add(line);
//		}
//		info.setLines(lines);
//		
//		try {
//			dao.insert(info);
//		} catch (AddException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
