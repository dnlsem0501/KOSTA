package com.day.dao;

import com.day.dto.Customer;
import com.day.exception.AddException;
import com.day.exception.FindException;
import com.day.exception.ModifyException;

public interface CustomerDAO {
	/**
	 * 고객은 가입한다
	 * @param c
	 * @throws AddException 추가실패시 발생
	 */
	public void insert(Customer c) throws AddException;
	/**
	 * 고객은 자기정보를 조회한다
	 * @param id
	 * @return
	 * @throws FindException 못찾을경우 발생
	 */
	public Customer selectById(String id) throws FindException;
	/**
	 *고객은 자기정보를 수정한다
	 * @param c
	 * @throws ModifyException
	 */
	public void update(Customer c) throws ModifyException;
	
}
