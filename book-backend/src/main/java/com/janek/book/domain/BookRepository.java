package com.janek.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository를 적어야 스프링 IoC에 Bean으로 등록이 되지만, JpaRepository를 extends하면 생략 가능(CRUD를 들고 있다.)
public interface BookRepository extends JpaRepository<BookInfo, Long> {

}
