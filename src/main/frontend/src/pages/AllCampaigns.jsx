import { useState, useEffect } from "react";
import Campaign from "../components/Campaign.jsx";
import { Col, Container, Row } from "react-bootstrap";

export default function AllCampaigns() {
  const [campaigns, setCampaigns] = useState([]);
  useEffect(() => {
    fetch("http://localhost:8080/api/campaigns")
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        console.log(data);
        setCampaigns(data);
      });
  }, []);
  return (
    <Container>
      <Row>
        <Col xs={12}>
          <h1>Campaigner app</h1>
          <Row>
            <Col xs={12}>
              <Campaign campaigns={campaigns} />
            </Col>
          </Row>
        </Col>
      </Row>
    </Container>
  );
}
