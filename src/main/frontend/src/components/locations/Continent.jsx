import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Country from "./Country.jsx";
import { Col, Container, Row } from "react-bootstrap";

export default function Continent({ continents }) {
  return (
    <Container>
      <Row>
        <Col xs={12}>
          <h2>Continents</h2>
          {continents.map((continent) => (
            <div key={continent.id}>
              <h4>{continent.name}</h4>
              <p>{continent.description}</p>
            </div>
          ))}
        </Col>
      </Row>
    </Container>
  );
}
