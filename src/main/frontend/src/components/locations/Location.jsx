import { Col, Container, Row, Nav } from "react-bootstrap";
import "./location.css";
import Continents from "./flowchart/Continents";

export default function Location({ continents }) {
  return (
    <Container fluid>
      <Row>
        <Col>
          <Row>
            <div className="tree">
              <ul>
                <Continents continents={continents} />
              </ul>
            </div>
          </Row>
        </Col>
      </Row>
    </Container>
  );
}
