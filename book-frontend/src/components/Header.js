import React from 'react';
import { Container, Nav, Navbar } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <>
      <Navbar bg="dark" variant="dark">
        <Container>
          <Link to={'/'} className="navbar-brand">
            Home
          </Link>
          <Nav className="me-auto">
            <Link to={'/joinForm'} className="nav-link">
              Join
            </Link>
            <Link to={'/loginForm'} className="nav-link">
              Login
            </Link>
            <Link to={'/saveForm'} className="nav-link">
              Write
            </Link>
          </Nav>
        </Container>
      </Navbar>
      <br />
    </>
  );
};

export default Header;
