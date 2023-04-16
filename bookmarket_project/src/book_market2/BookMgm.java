package book_market2;
import java.util.ArrayList;

public class BookMgm {
	ArrayList<BookVo> bookList;
	
	public BookMgm() {
		bookList = new ArrayList<BookVo>();
		createList();
	}
	public void showList() {
		System.out.println("-----------------------------------------------");
		System.out.println("\t구매 가능 도서 리스트");
		System.out.println("-----------------------------------------------");
		for(BookVo book : bookList) {
			System.out.print(book.getIsbn()+ " | ");
			System.out.print(book.getTitle() + "\t| ");
			System.out.print(book.getPrice() + " | ");
			System.out.print(book.getAuthor()+ "\n");
		}
		System.out.println("-----------------------------------------------");
	}
	
	public BookVo search(String isbn) {
		BookVo book = null;
		for(BookVo sbook : bookList) {
			if(sbook.getIsbn().equals(isbn)) {
				book = sbook;
			}
		}
		return book;
	}
	
	
	
	public void createList() { // 도서 추가
		
		String[] titleList = {"JUST JAVA", "오라클 SQL 기술", "JSP 프로그래밍", "스프링 퀵 스타트"};
		String[] authorList = {"황희정", "홍형경", "최범균", "홍길동"};
		String[] isbnList = {"ISBN1234", "ISBN7854", "ISBN4521", "ISBN6932"};
		int[] priceList = {27000, 25000, 30000, 28000};
		//booklist에 도서 추가
		for(int i = 0; i < titleList.length; i++) {
			BookVo bvo = new BookVo();
			bvo.setTitle(titleList[i]);
			bvo.setAuthor(authorList[i]);
			bvo.setIsbn(isbnList[i]);
			bvo.setPrice(priceList[i]);
			bookList.add(bvo);
		}
		


	}
}
