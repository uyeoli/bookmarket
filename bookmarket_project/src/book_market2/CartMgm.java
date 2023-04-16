package book_market2;
import java.util.ArrayList;
import java.util.Scanner;

public class CartMgm {
	ArrayList<BookVo> cartList;
	private ArrayList<CartItemVo> cartItemList;
	Scanner sc;
	
	public CartMgm(Scanner sc) {
		this.sc = sc;
		cartList = new ArrayList<BookVo>();
		cartItemList = new ArrayList<CartItemVo>();
		
	}
	
	public CartMgm() {
		cartList = new ArrayList<BookVo>();
		cartItemList = new ArrayList<CartItemVo>();
		
	}
	
	public ArrayList<CartItemVo> getCartItemList() {
		return cartItemList;
	}
	
	public int getSize() {
		return cartItemList.size();
	}
	
	public void add(boolean check, CartItemVo item, BookVo book) {
		if(check) {
			item.setQty(item.getQty()+1);
		} else {
			CartItemVo item2 = new CartItemVo();
			item2.setIsbn(book.getIsbn());
			item2.setTitle(book.getTitle());
			item2.setQty(1);
			item2.setTotalPrice(book.getPrice());
			cartItemList.add(item2);
		}
	}
	//장바구니에 book 추가
	public boolean insert(BookVo book) {
		cartList.add(book);
		boolean result = false;
		if(cartItemList.size() != 0) {
			boolean check = false;
			//�⺻for���� ����Ͽ� book�� isbn�� cartItemList�� isbn��
			CartItemVo currItem = null;
			for(int i = 0; i < cartItemList.size(); i++) {
				CartItemVo item = cartItemList.get(i);
				if(item.getIsbn().equals(book.getIsbn())) {
					currItem = item;
					check = true;
					i = cartItemList.size();
				}
			}
			add(check, currItem, book); // cartItemList�� �߰�
			result = true;
		}else {
			CartItemVo item = new CartItemVo();
			item.setIsbn(book.getIsbn());
			item.setTitle(book.getTitle());
			item.setQty(1);
			item.setTotalPrice(book.getPrice());
			cartItemList.add(item);
			result = true;
		}
		return result;
	}
	public boolean remove(int idx) {
		return cartItemList.remove(cartItemList.get(idx));
	}
	
	
	
	public boolean remove(String isbn) {
		boolean result = false;
		int idx = -1;
		for(int i = 0; i < cartItemList.size(); i++) {
			CartItemVo item = cartItemList.get(i);
			if(isbn.equals(item.getIsbn())) {
				idx = i;
			}
		}
		if(idx != -1) {
			System.out.println("��ٱ����� �׸��� �����Ͻðڽ��ϱ�? Y | N");
			String cartRemove = sc.next().toUpperCase();
			if(cartRemove.equals("Y")) {
				cartItemList.remove(idx);
				result = true;
			} 
		} 
		return result;
	}
	
	public void remove() {
		cartItemList.clear();
	}
	
	public void updateQty(String isbn, int qty) {
		int idx = -1;
		for(int i = 0; i < cartItemList.size(); i++) {
			CartItemVo item = cartItemList.get(i);
			if(isbn.equals(item.getIsbn())) {
				idx = i;
			}
		}
		if(idx != -1) {
			CartItemVo item = cartItemList.get(idx);
			if(item.getQty() >= qty) {
				item.setQty(item.getQty() - qty);
				if(item.getQty() == 0) {
					cartItemList.remove(idx);
				}
				System.out.println("--���� ������Ʈ �Ϸ�--");
			} else {
				System.out.println("--������ ������ �����մϴ�.--");
			}
		} else {
			System.out.println("������ ������ ������ �������� �ʽ��ϴ�.");
		}
	}
	
	public void updateQty(String isbn) {
		int idx = -1;
		for(int i = 0; i < cartItemList.size(); i++) {
			CartItemVo item = cartItemList.get(i);
			if(isbn.equals(item.getIsbn())) {
				idx = i;
			}
		}
		if(idx != -1) {
			CartItemVo item = cartItemList.get(idx);
				item.setQty(item.getQty() - 1);
		}
	}
	
	public void showCartList() {
		System.out.println("-----------------------------------------------");
		System.out.println("����ID\t\t����\t�հ�");
		System.out.println("-----------------------------------------------");
		for(CartItemVo item : cartItemList) {
			System.out.print(item.getIsbn()+ "\t");
			System.out.print(item.getQty() + "\t");
			System.out.print(item.getQty()*item.getTotalPrice()+ "\n");
		}
		System.out.println("-----------------------------------------------\n");
	}
	
	public void showCartList(String order) {
//		System.out.println("-----------------------------------------------");
//		System.out.println("����ID\t\t����\t�հ�");
		System.out.println("-----------------------------------------------");
		System.out.println("����ID\t\t����\t�հ�");
		int orderTotalPrice = 0;
		for(CartItemVo item : cartItemList) {
			System.out.print(item.getIsbn()+ "\t");
			System.out.print(item.getQty() + "\t");
			System.out.print(item.getQty()*item.getTotalPrice()+ "\n");
			orderTotalPrice += item.getQty()*item.getTotalPrice();
		}
		System.out.println("-----------------------------------------------");
		System.out.println("\t\t�ֹ� �ѱݾ� : " + orderTotalPrice);
	}
	
}
