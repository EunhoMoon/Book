package com.janek.book.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity	// 서버 실행시에 테이블이 DB에 생성(Object Relation Mapping) 된다.
public class BookInfo {
	@Id	// 해당 변수를 PK로 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// 해당 데이터베이스 번호증가 전략을 따라간다.
	private Long id;
	
	private String title;
	private String author;
}
