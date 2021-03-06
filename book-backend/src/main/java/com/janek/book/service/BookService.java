package com.janek.book.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.janek.book.domain.BookInfo;
import com.janek.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

// 기능을 정의, 트랜잭션을 관리할 수 있다.

@RequiredArgsConstructor	// final이 붙은 생성자를 자동으로 만들어준다.(자동 DI)
@Service
public class BookService {

	private final BookRepository bookRepository;
	
	@Transactional	// 서비스 함수가 종료될 때 commit할지 rollback 할지 트랜잭션 관리
	public BookInfo saveBookInfo(BookInfo book) {
		return bookRepository.save(book);
	}
	
	@Transactional(readOnly = true)	// JPA의 변경감지 기능 비활성화, update시의 정합성을 유지(해당 select의 원본을 유지) -> insert 시에는 못막는다.
	public BookInfo getBookInfo(Long id) throws IllegalAccessException {
		return bookRepository.findById(id)
				.orElseThrow(() -> new IllegalAccessException("Id를 확인하세요."));
	}
	
	public List<BookInfo> getAllBookInfo() {
		return bookRepository.findAll();
	}
	
	@Transactional
	public BookInfo updateBookInfo(Long id, BookInfo book) {
		// 더티체킹 후 update
		BookInfo bookEntiy = bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인하세요"));	// 영속화 (book 오브젝트) -> 영속성 컨텍스트 보관
		bookEntiy.setTitle(book.getTitle());
		bookEntiy.setAuthor(book.getAuthor());
		return bookEntiy;
	}	// 함수 종료 = 트랜잭션 종료 => 영속화 되어있는 데이터를 갱신(flush) => commit : 더티체킹
	
	@Transactional
	public String deleteBookInfo(Long id) {
		bookRepository.deleteById(id);
		return "ok";
	}
	
}
