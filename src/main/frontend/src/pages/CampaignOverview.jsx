import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import Location from "../components/locations/Location.jsx";
import Continent from "../components/locations/Continent.jsx";
import { Col, Container, Row } from "react-bootstrap";

export default function CampaignOverview(props) {
  const [isLoading, setLoading] = useState(true);
  const reactLocation = useLocation();
  const campaign = reactLocation.state.campaign;

  const [continents, setContinent] = useState({});
  useEffect(() => {
    fetch(
      `http://localhost:8080/api/locations/continents/campaign/${campaign.uuid}`
    )
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        console.log(data);
        setContinent(data);
        setLoading(false);
      });
  }, [campaign.uuid]);

  if (isLoading) {
    return (
      <div className="d-flex justify-content-center">
        <div className="spinner-border" role="status"></div>
      </div>
    );
  }

  return (
    <Container>
      <Row>
        <Col>
          <h1>Campaign Overview</h1>
          <h4>{campaign.name}</h4>
          <p>{campaign.description}</p>
          <Container fluid>
            <Row>
              <Col xs={12}>
                <div>
                  <h2>Locations</h2>
                  <Location continents={continents || []} />
                </div>
              </Col>
              <Col xs={12}>
                <div>
                  <h2>People</h2>
                </div>
              </Col>
            </Row>
          </Container>
        </Col>
      </Row>
    </Container>
  );
}
