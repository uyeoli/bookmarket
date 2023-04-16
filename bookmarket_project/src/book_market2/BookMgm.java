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
		System.out.println("\t���� ���� ���� ����Ʈ");
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
	
	
	
	public void createList() { // ���� �߰�
		
		String[] titleList = {"JUST JAVA", "����Ŭ SQL ���", "JSP ���α׷���", "������ �� ��ŸƮ"};
		String[] authorList = {"Ȳ����", "ȫ����", "�ֹ���", "ȫ�浿"};
		String[] isbnList = {"ISBN1234", "ISBN7854", "ISBN4521", "ISBN6932"};
		int[] priceList = {27000, 25000, 30000, 28000};
		//booklist�� ���� �߰�
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