package com.market.dao;

import java.util.ArrayList;

import com.market.vo.CartVo;
import com.market.vo.OrderVo;

public class CartDao extends DBConn{
	/*  로그인한 회원의 장바구니 리스트 */
	public ArrayList<CartVo> select(String mid){
		ArrayList<CartVo> list = new ArrayList<CartVo>();
		StringBuffer sb = new StringBuffer(150);
		sb.append("select isbn,title, price, qty, total_price, sprice, stotal_price, rownum rno ");
		sb.append(" , cid ");
		sb.append(" from (SELECT B.ISBN, B.TITLE, B.PRICE, C.QTY, B.PRICE*C.QTY TOTAL_PRICE,");
		sb.append(" TO_CHAR(B.PRICE,'L999,999') SPRICE,");
		sb.append(" TO_CHAR(B.PRICE*C.QTY, 'L999,999') STOTAL_PRICE, cid");
		sb.append(" FROM BOOKMARKET_BOOK B, BOOKMARKET_CART C, BOOKMARKET_MEMBER M");
		sb.append(" WHERE B.ISBN = C.ISBN AND C.MID = M.MID");
		sb.append(" AND M.MID=?)");
	
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, mid);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				CartVo cartVo = new CartVo();
				cartVo.setIsbn(rs.getString(1));
				cartVo.setTitle(rs.getString(2));
				cartVo.setPrice(rs.getInt(3));
				cartVo.setQty(rs.getInt(4));
				cartVo.setTotal_price(rs.getInt(5));
				cartVo.setSprice(rs.getString(6));
				cartVo.setStotal_price(rs.getString(7));
				cartVo.setRno(rs.getInt(8));
				cartVo.setCid(rs.getString(9));
				list.add(cartVo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	/* 로그인한 회원의 장바구니 카운트 */
	public int getSize(String mid) {
		int result = 0;
		StringBuffer sb = new StringBuffer(50);
		sb.append("select count(*) from bookmarket_cart where mid=?");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, mid);
			rs = pstmt.executeQuery();
			while(rs.next()) result = rs.getInt(1);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/* 장바구니 중복 도서 체크 */
	public int insertCheck(CartVo cartVo) {
		int result = 0;
		StringBuffer sb = new StringBuffer(50);
		sb.append(" select count(*) from bookmarket_cart ");
		sb.append(" where isbn =? and mid=? ");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, cartVo.getIsbn().toUpperCase());
			pstmt.setString(2, cartVo.getMid().toUpperCase());
			rs = pstmt.executeQuery();
			while(rs.next()) result = rs.getInt(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	/* 장바구니 추가 */
	public int insert(CartVo cartVo) {
//System.out.println("mid--->>" + cartVo.getMid());		
		int result = 0;
		StringBuffer sb = new StringBuffer(100);
		sb.append(" insert into bookmarket_cart ");
		sb.append(" values('C_'||LTRIM(TO_CHAR(SEQU_BOOKMARKET_CART_CID.NEXTVAL,'0000')),");
		sb.append(" sysdate, 1, ?,? )");
		
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, cartVo.getIsbn().toUpperCase());
			pstmt.setString(2, cartVo.getMid().toUpperCase());
			
			result = pstmt.executeUpdate();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int delete(String cid) {
		int result = 0;
		StringBuffer sb = new StringBuffer(100);
		sb.append("DELETE FROM BOOKMARKET_CART WHERE CID = ?");
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, cid);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int updateQty(String cid, String status) {
		int result = 0;
		StringBuffer sb = new StringBuffer(100);
		if(status.equals("plus")) {
			sb.append("UPDATE BOOKMARKET_CART SET QTY = QTY+1 WHERE CID = ?");
		} else {
			sb.append("UPDATE BOOKMARKET_CART SET QTY = QTY-1 WHERE CID = ?");
		}
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, cid);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int deleteAll(String mid) {
		int result = 0;
		StringBuffer sb = new StringBuffer(100);
		sb.append("DELETE FROM BOOKMARKET_CART WHERE MID = ?");
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, mid);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	//주문테이블 데이터 생성 - 회원의 qty, isbn 리스트
	public OrderVo getOrderVo(String mid) {
		OrderVo orderVo = new OrderVo();
		StringBuffer sb = new StringBuffer(50);
		sb.append("SELECT QTY, ISBN FROM BOOKMARKET_CART WHERE MID = ? ORDER BY CDATE DESC");
		try {
			getPreparedStatement(sb.toString());
			pstmt.setString(1, mid);
			rs = pstmt.executeQuery();
			rs.last();
			int[] qtyList = new int[rs.getRow()];
			String[] isbnList = new String[rs.getRow()];
			rs.beforeFirst();
			int idx = 0;
			while(rs.next()) {
				//orderVo의 qtyList[], isbnList[] 데이터 저장
				qtyList[idx] = rs.getInt(1);
				isbnList[idx] = rs.getString(2);
				idx++;
			}
			orderVo.setQtyList(qtyList);
			orderVo.setIsbnList(isbnList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return orderVo;
	}
	
	
	
	
	
	
	
}
