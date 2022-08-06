package com.ccookat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

	private Connection conn;
	
	public CartDAO(Connection conn) {
		this.conn = conn;	
	}
	
	public int getMaxNum() {

		int maxNum =0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {

			sql = "select nvl(max(cartNum),0) from cart";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if(rs.next()) {

				maxNum = rs.getInt(1); 								
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return maxNum;
	}

	
	//장바구니에 넣어져있는 데이터를 가져오기
	public List<CartDTO> selectAll(String customerId) {
		
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		String sql;
		
		CartDTO ctdto = null;
		List<CartDTO> lists = new ArrayList<CartDTO>();
		
		try {	
			
			sql = "select a.cartnum,a.customerid,a.itemnum,a.cartitemcount,";
			sql+= "a.carttotprice, b.itemname,b.itemImage1 from cart a join item b ";
			sql+= "on a.itemnum = b.itemnum where customerid=? order by a.cartnum desc";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, customerId);
			
			rs = pstmt.executeQuery();
						
			while(rs.next()) {
				
				ctdto = new CartDTO();
				
				ctdto.setCartNum(rs.getInt(1));
				ctdto.setCustomerId(rs.getString(2));
				ctdto.setItemNum(rs.getInt(3));
				ctdto.setCartItemCount(rs.getInt(4));
				ctdto.setCartTotPrice(rs.getInt(5));
				ctdto.setItemName(rs.getString(6));
				ctdto.setItemImage1(rs.getString(7));				
				
				lists.add(ctdto);
		
			}
			
			pstmt.close();
			rs.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return lists;	
	}
	
	//장바구니에 담기
	//고객이 장바구니에 담으면 아이템넘버랑 수량 고객정보 가져와서 넣어야함
	//totalprice가격이 있는데 이건 주문으로 넘어갈때 담아서 넘겨줌
	//생각해보니까 maxnum있어야함 그걸로 cartnum 순서대로 관리해야됨
	public void insertData(CartDTO ctdto) {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "insert into cart(cartnum,customerid,itemnum,";
			sql+= "cartitemcount,carttotprice) ";
			sql+= "values(?,?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ctdto.getCartNum());
			pstmt.setString(2, ctdto.getCustomerId());
			pstmt.setInt(3, ctdto.getItemNum());
			pstmt.setInt(4, ctdto.getCartItemCount());
			pstmt.setInt(5, ctdto.getCartTotPrice());
					
			pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
			
	}
	
	
}
