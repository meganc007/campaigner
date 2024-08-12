import { Col, Container, Row } from "react-bootstrap";

export default function Country({ countries }) {
  return (
    <Container>
      <Row>
        <Col xs={12}>
          <h3>Countries</h3>
          {countries.map((country) => (
            <div key={country.id}>
              <h4>{country.name}</h4>
              <p>{country.description}</p>
            </div>
          ))}
        </Col>
      </Row>
    </Container>
  );
}
