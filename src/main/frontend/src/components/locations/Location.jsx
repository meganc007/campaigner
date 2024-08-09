import { Col, Container, Row, Nav } from "react-bootstrap";
import "./location.css";

export default function Location({ continents }) {
  return (
    <Container>
      <Row>
        <Col>
          <Row>
            <Nav>
              <ul>
                {continents &&
                  continents.map((continent) => (
                    <li key={continent.id}>
                      <a href="">{continent.name}</a>
                      {continent.countries.length > 0 && (
                        <ul>
                          {continent.countries.map((country) => (
                            <li key={country.id}>
                              <a href="">{country.name}</a>
                              {country.regions.length > 0 && (
                                <ul>
                                  {country.regions.map((region) => (
                                    <li key={region.id}>
                                      <a href="">{region.name}</a>
                                    </li>
                                  ))}
                                </ul>
                              )}
                            </li>
                          ))}
                        </ul>
                      )}
                    </li>

                    // {country.regions.map((region) => (
                    //   <div key={region.id}>
                    //     <p>
                    //       <strong>Regions</strong>
                    //     </p>
                    //     <p>{region.name}</p>
                    //     <p>{region.description}</p>
                    //     <p>
                    //       Climate: <br />
                    //       {region.climate.name} -{" "}
                    //       {region.climate.description}
                    //     </p>
                    //     {region.landmarks.map((landmark) => (
                    //       <div key={landmark.id}>
                    //         <p>
                    //           <strong>Landmarks</strong>
                    //         </p>
                    //         <p>{landmark.name}</p>
                    //         <p>{landmark.description}</p>
                    //       </div>
                    //     ))}
                    //   </div>
                    // ))}
                  ))}
              </ul>
            </Nav>
          </Row>
        </Col>
      </Row>
    </Container>
  );
}
