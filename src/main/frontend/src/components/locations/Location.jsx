import { Col, Container, Row, Nav } from "react-bootstrap";
import Countries from "./flowchart/Countries";
import "./location.css";

export default function Location({ continents }) {
  return (
    <Container>
      <Row>
        <Col>
          <Row>
            <div className="tree">
              <ul>
                {continents &&
                  continents.map((continent) => (
                    <li key={continent.id}>
                      <a href="">
                        {continent.name}
                        <br />
                        <small>Continent</small>
                      </a>
                      {continent.countries.length > 0 && (
                        <Countries countries={continent.countries} />
                      )}
                    </li>
                  ))}
              </ul>
            </div>
          </Row>
        </Col>
      </Row>
    </Container>
  );
}
