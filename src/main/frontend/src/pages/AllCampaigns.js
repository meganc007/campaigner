import { useState, useEffect } from "react";
import Campaign from "../components/Campaign.js";

function AllCampaigns() {
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
    <div className="container">
      <div className="row">
        <div className="col-12">
          <h1>Campaigner app</h1>
          <div className="row">
            <div className="col-12">
              <Campaign campaigns={campaigns} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
export default AllCampaigns;
