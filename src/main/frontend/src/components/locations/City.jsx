import { Col, Container, Row } from "react-bootstrap";

export default function City({ cities }) {
  return (
    <Container>
      <Row>
        <Col xs={12}>
          <h3>Cities</h3>
          {cities.map((city) => (
            <div key={city.id}>
              <p>
                <strong>{city.name}</strong>
              </p>
              <p>Description: {city.description}</p>
              <p>Wealth: {city.wealth.name}</p>
              <p>Country: {city.country.name}</p>
              <p>
                Region: {city.region.name} - {city.region.description}
              </p>

              <p>
                Government: {city.government.name} -{" "}
                {city.government.description}
              </p>
              <p>
                Settlement Type: {city.settlementType.name} -{" "}
                {city.settlementType.description}
              </p>
            </div>
          ))}
        </Col>
      </Row>
    </Container>
  );
}
