package com.market.page;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.market.commons.MarketFont;
import com.market.dao.BookDao;
import com.market.dao.CartDao;
import com.market.dao.DBConn;
import com.market.dao.MemberDao;
import com.market.main.MainWindow;
import com.market.vo.CartVo;

import book_market2.CartMgm;

public class CartItemListPage extends JPanel {

	JTable cartTable;
	Object[] tableHeader = { "번호","도서ID", "도서명", "단가", "수량", "총가격" };
	CartMgm cm;
	MemberDao memberDao;
	BookDao bookDao;
	CartDao cartDao;
	ArrayList<CartVo> cartItemList;
	JLabel totalPricelabel;
	public static int mSelectRow = -1;

	public CartItemListPage(JPanel panel, CartMgm cm, Map<String,DBConn> daoList) {
		this.memberDao = (MemberDao)daoList.get("memberDao");
		this.bookDao = (BookDao)daoList.get("bookDao");
		this.cartDao = (CartDao)daoList.get("cartDao");
		this.cm = cm;
		this.setLayout(null);

		Rectangle rect = panel.getBounds();
		System.out.println(rect);
		this.setPreferredSize(rect.getSize());

		JPanel bookPanel = new JPanel();
		bookPanel.setBounds(0, 0, 1000, 400);
		add(bookPanel);

		//"도서ID", "도서명", "단가", "수량", "총가격" 
		cartItemList = cartDao.select(MainWindow.member.getMid().toUpperCase());
		Object[][] content = new Object[cartItemList.size()][tableHeader.length];
		Integer totalPrice = 0;
		for (int i = 0; i < cartItemList.size(); i++) {
			CartVo item = cartItemList.get(i);
			content[i][0] = item.getRno();
			content[i][1] = item.getIsbn();
			content[i][2] = item.getTitle();
			content[i][3] = item.getSprice();  //단가
			content[i][4] = item.getQty();
			content[i][5] = item.getStotal_price();
			totalPrice += item.getQty() * item.getPrice();
		}

		cartTable = new JTable(content, tableHeader);
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setPreferredSize(new Dimension(600, 300));
		jScrollPane.setViewportView(cartTable);
		bookPanel.add(jScrollPane);

		JPanel totalPricePanel = new JPanel();
		totalPricePanel.setBounds(0, 400, 1000, 50);
		totalPricelabel = new JLabel("총금액: " + priceFormat(totalPrice) + " 원");
		totalPricelabel.setForeground(Color.red);
		MarketFont.getFont(totalPricelabel);
		totalPricePanel.add(totalPricelabel);
		add(totalPricePanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBounds(0, 450, 1000, 50);
		add(buttonPanel);

		JLabel buttonLabel = new JLabel("장바구니 비우기");
		MarketFont.getFont(buttonLabel);
		JButton clearButton = new JButton();
		clearButton.add(buttonLabel);
		buttonPanel.add(clearButton);

		
		/** 장바구니 비우기 버튼 이벤트 처리 **/
		clearButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				//ArrayList<CartItemVo> cartItem = cm.getCartItemList();
					int select = JOptionPane.showConfirmDialog(clearButton, "정말로 삭제하시겠습니까? ");
					if (select == 0) {
//						cart.deleteBook();
						cartDao.deleteAll(MainWindow.member.getMid().toUpperCase()); // 로그인한 mid값의 목록 전체 삭제
						JOptionPane.showMessageDialog(clearButton, "삭제가 완료되었습니다");
						TableModel tableModel = new DefaultTableModel(new Object[0][0], tableHeader);
						cartTable.setModel(tableModel);
						totalPricelabel.setText("총금액: " + 0 + " 원");
					}
			}
		});

		
		JLabel removeLabel = new JLabel("장바구니 항목 삭제하기");
		MarketFont.getFont(removeLabel);
		JButton removeButton = new JButton();
		removeButton.add(removeLabel);
		buttonPanel.add(removeButton);

		/** 장바구니 항목 삭제 버튼 이벤트 처리 **/
		removeButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
//				if (cartDao.getSize(MainWindow.member.getMid().toUpperCase()) == 0)
//					JOptionPane.showMessageDialog(removeButton, "장바구니가 비어있습니다");
//				else 
				if (mSelectRow == -1)
					JOptionPane.showMessageDialog(removeButton, "삭제할 항목을 선택해주세요");
				else {
//					cm.remove(mSelectRow);
					int result = cartDao.delete(cartItemList.get(mSelectRow).getCid());
					if(result == 1) {
						showList();
						mSelectRow = -1;
					}
				}
			}
		});
		/*
		 *  장바구니 항목 수정하기 : 증가(+)
		 */
		JLabel updateQtyLabel = new JLabel("장바구니 항목 수량(+)");
		MarketFont.getFont(updateQtyLabel);
		JButton updateQtyButton = new JButton();
		updateQtyButton.add(updateQtyLabel);
		buttonPanel.add(updateQtyButton);

		updateQtyButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
//				if(cm.getSize() == 0) {
//					JOptionPane.showMessageDialog(null, "장바구니가 비어있습니다");
//				}else 
				if(mSelectRow == -1){
					JOptionPane.showMessageDialog(null, "수정할 항목을 선택해주세요");
				}else {
					int qty = cartItemList.get(mSelectRow).getQty();
					if(qty >= 1) {
						cartDao.updateQty(cartItemList.get(mSelectRow).getCid(), "plus");
						showList();
						mSelectRow = -1;
					}else {
						JOptionPane.showMessageDialog(null, "2개 이상인 경우에만 수정 가능합니다");
					}
				}//if
			}
		});
		
		/*
		 *  장바구니 항목 수정하기 : 감소(-)
		 */
		JLabel updateQtyLabel2 = new JLabel("장바구니 항목 수량(-)");
		MarketFont.getFont(updateQtyLabel2);
		JButton updateQtyButton2 = new JButton();
		updateQtyButton2.add(updateQtyLabel2);
		buttonPanel.add(updateQtyButton2);

		updateQtyButton2.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
//				if(cm.getSize() == 0) {
//					JOptionPane.showMessageDialog(null, "장바구니가 비어있습니다");
//				}else 
				if(mSelectRow == -1){
					JOptionPane.showMessageDialog(null, "수정할 항목을 선택해주세요");
				}else {
					int qty = cartItemList.get(mSelectRow).getQty();
					if(qty > 1) {
						cartDao.updateQty(cartItemList.get(mSelectRow).getCid(), "minus");
						showList();
						mSelectRow = -1;
					}else {
						JOptionPane.showMessageDialog(null, "2개 이상인 경우에만 수정 가능합니다");
					}
				}//if
			}
		});
		// 리스트 선택 이벤트 처리
		cartTable.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				int row = cartTable.getSelectedRow();
				mSelectRow = row;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				}

			});
		
		}//생성자
	/* cartItemList 출력 */
	public void showList() {
		cartItemList = cartDao.select(MainWindow.member.getMid().toUpperCase());
		Object[][] content = new Object[cartItemList.size()][tableHeader.length];
		Integer totalPrice = 0;
		for (int i = 0; i < cartItemList.size(); i++) {
			CartVo item = cartItemList.get(i);
			content[i][0] = item.getRno();
			content[i][1] = item.getIsbn();
			content[i][2] = item.getTitle();
			content[i][3] = item.getSprice();  //단가
			content[i][4] = item.getQty();
			content[i][5] = item.getStotal_price();
			totalPrice += item.getQty() * item.getPrice();
		}
		TableModel tableModel = new DefaultTableModel(content, tableHeader);
		totalPricelabel.setText("총금액: " + priceFormat(totalPrice) + " 원");
		cartTable.setModel(tableModel);
	}
	
	public String priceFormat(long price) {
		DecimalFormat df = new DecimalFormat("###,###");
		String sprice = df.format(price);
		return sprice;
	}
	
	
	
}//class