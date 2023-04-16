package com.market.dao;

import com.market.vo.OrderVo;

public class OrderDao extends DBConn{
	
	/* 데이터 추가 - PreparedStatement */
	public int insertPre(OrderVo orderVo) {
		int result = 0;
		StringBuffer sb = new StringBuffer(100);
		sb.append("INSERT INTO BOOKMARKET_ORDER(oid,odate,qty,isbn,mid,name,phone,addr) ");
		sb.append("VALUES(?,?,?,?,?,?,?,?)");
		try {
			getPreparedStatement(sb.toString());
			for(int i = 0; i < orderVo.getQtyList().length; i++) {
				pstmt.setString(1, orderVo.getOid());
				pstmt.setString(2, orderVo.getOdate());
				pstmt.setInt(3, orderVo.getQtyList()[i]);
				pstmt.setString(4, orderVo.getIsbnList()[i]);
				pstmt.setString(5, orderVo.getMid());
				pstmt.setString(6, orderVo.getName());
				pstmt.setString(7, orderVo.getPhone());
				pstmt.setString(8, orderVo.getAddr());
				pstmt.addBatch(); //한번실행할때마다 다른공간에 쌓아둠
				pstmt.clearParameters(); //파라미터 다시 세팅
			}
			result = pstmt.executeBatch().length;
			pstmt.clearParameters();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/* 데이터 추가 - Statement */
	public int insert(OrderVo orderVo) {
		int result = 0;
//		StringBuffer sb = new StringBuffer(100);
		getStatement();
		try {
			for(int i = 0; i < orderVo.getQtyList().length; i++) {
				String sql = "INSERT INTO BOOKMARKET_ORDER VALUES(" 
				+ "'" + orderVo.getOid() + "', "  
				+ "'" + orderVo.getOdate() + "', " 
				+ orderVo.getQtyList()[i] + ", " 
				+ "'" + orderVo.getIsbnList()[i] + "', "  
				+ "'" + orderVo.getMid() + "', " 
				+ "'" + orderVo.getName() + "', " 
				+ "'" + orderVo.getPhone() + "', " 
				+ "'" + orderVo.getAddr() + "')";
				result = stmt.executeUpdate(sql);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
