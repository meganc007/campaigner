import {
  Accordion,
  Card,
  CardBody,
  CardTitle,
  CardSubtitle,
  CardText,
  Col,
  Container,
  Row,
} from "react-bootstrap";

export default function Location({ location }) {
  const continents = location.continents;
  const countries = location.countries;
  return (
    <Container>
      <Row>
        <Col>
          <Row>
            {continents.map((continent) => (
              <Card key={continent.id} className="col">
                <CardBody>
                  <CardTitle>{continent.name}</CardTitle>
                  <CardSubtitle>Continent</CardSubtitle>
                  <CardText>{continent.description}</CardText>

                  {countries.map((country) => (
                    <Accordion key={country.id} defaultActiveKey={country.id}>
                      <Accordion.Item eventKey={country.id}>
                        <Accordion.Header>{country.name}</Accordion.Header>
                        <Accordion.Body>{country.description}</Accordion.Body>
                      </Accordion.Item>
                    </Accordion>
                  ))}
                </CardBody>
              </Card>
            ))}
          </Row>
        </Col>
      </Row>
    </Container>
  );
}
