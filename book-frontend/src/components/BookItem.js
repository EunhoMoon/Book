import React from 'react';
import { Card } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const BookItem = () => {
  return (
    <Card>
      <Card.Body>
        <Card.Title>제목1</Card.Title>
        <Link to={'/post/1'} className="btn btn-primary">
          상세보기
        </Link>
      </Card.Body>
    </Card>
  );
};

export default BookItem;
