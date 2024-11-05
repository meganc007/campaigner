import { Col, Container, Row } from "react-bootstrap";
import { useState } from "react";

export default function Heading({ data, subheading, extraInfo, classType }) {
  const [toggle, setToggle] = useState(true);
  function handleClick(event) {
    event.preventDefault();
    setToggle(!toggle);
  }
  return (
    <div className={classType}>
      <a href="" onClick={handleClick}>
        <h5>{data.name}</h5>
        {toggle == true && (
          <>
            <h6>{subheading}</h6>
            <Container fluid>
              <Row>
                <Col>
                  <span className="description">{data.description}</span>
                </Col>
                {extraInfo !== null && (
                  <>
                    <span>{extraInfo}</span>
                  </>
                )}
              </Row>
            </Container>
          </>
        )}
      </a>
    </div>
  );
}
