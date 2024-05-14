import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import Continent from "../components/Continent.jsx";

export default function CampaignOverview(props) {
  const location = useLocation();
  const campaign = location.state.campaign;

  const [continents, setContinents] = useState([]);
  useEffect(() => {
    fetch(
      `http://localhost:8080/api/locations/continents/campaign/${campaign.uuid}`
    )
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        console.log(data);
        setContinents(data);
      });
  }, []);

  return (
    <div className="container">
      <div className="row">
        <div className="col-12">
          <h1>Campaign Overview</h1>
          <h4>{campaign.name}</h4>
          <p>{campaign.description}</p>
          <div className="container">
            <div className="row">
              <div className="col-6">
                <div className="card">
                  <h2>Locations</h2>
                  <Continent continents={continents} />
                </div>
              </div>
              <div className="col-6">
                <div className="card">
                  <h2>People</h2>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}