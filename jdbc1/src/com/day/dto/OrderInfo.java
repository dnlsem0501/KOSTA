package com.day.dto;

import java.util.Date;
import java.util.List;

public class OrderInfo {
	private int order_no;
//	private String order_id;
	private Date order_dt;
	private Customer order_c;
	private List<OrderLine> lines; 
	
	public int getOrder_no() {
		return order_no;
	}
	@Override
	public String toString() {
		return "OrderInfo [order_no=" + order_no + ", order_dt=" + order_dt + ", order_c=" + order_c + ", lines="
				+ lines + "]";
	}
	public Customer getOrder_c() {
		return order_c;
	}
	public void setOrder_c(Customer order_c) {
		this.order_c = order_c;
	}
	public List<OrderLine> getLines() {
		return lines;
	}

	public void setLines(List<OrderLine> lines) {
		this.lines = lines;
	}
	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}

	public Date getOrder_dt() {
		return order_dt;
	}
	public void setOrder_dt(java.util.Date order_dt) {
		this.order_dt = order_dt;
	}
	public OrderInfo(Customer order_c) {
		super();
		this.order_c=order_c;
	}
	public OrderInfo(Customer order_c, List<OrderLine> lines) {
		super();
		this.order_c=order_c;
		this.lines=lines;
	}
	public OrderInfo() {
		super();
	}

	public OrderInfo(int order_no, Date order_dt, Customer order_c, List<OrderLine> lines) {
		super();
		this.order_no=order_no;
		this.order_dt=order_dt;
		this.order_c=order_c;
		this.lines=lines;
	}
}
