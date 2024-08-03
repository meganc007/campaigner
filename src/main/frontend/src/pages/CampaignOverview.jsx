import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import Continent from "../components/Continent.jsx";
import Country from "../components/Country.jsx";
import City from "../components/City.jsx";

export default function CampaignOverview(props) {
  const reactLocation = useLocation();
  const campaign = reactLocation.state.campaign;

  const [location, setLocation] = useState({});
  useEffect(() => {
    fetch(
      `http://localhost:8080/api/locations/${campaign.uuid}`
    )
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        console.log(data);
        setLocation(data);
      });
  }, [campaign.uuid]);

  return (
    <div className="container">
      <div className="row">
        <div className="col-12">
          <h1>Campaign Overview</h1>
          <h4>{campaign.name}</h4>
          <p>{campaign.description}</p>
          <div className="container">
            <div className="row">
              <div className="col-12">
                <div className="card">
                  <h2>Locations</h2>
                  <City cities={location.cities || []} />
                </div>
              </div>
              <div className="col-12">
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