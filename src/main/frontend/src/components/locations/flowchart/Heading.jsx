import { Col, Container, Row } from "react-bootstrap";

export default function Heading({ data, subheading, extraInfo, classType }) {
  function handleClick(event) {
    event.preventDefault();
    alert(data.description);
    extraInfo !== undefined && alert(extraInfo.extraInfo);
  }
  return (
    <div className={classType}>
      <a href="" onClick={handleClick}>
        <h5>{data.name}</h5>
        <h6>{subheading}</h6>
        <Container fluid>
          <Row>
            <Col>
              <span className="description">{data.description}</span>
            </Col>
          </Row>
        </Container>
        {extraInfo !== null && (
          <>
            <span>{extraInfo}</span>
          </>
        )}
      </a>
    </div>
  );
}
//TODO: change extrainfo to accept and map through object of arrays
