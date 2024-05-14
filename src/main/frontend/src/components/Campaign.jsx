import { Link } from "react-router-dom";

export default function Campaign({campaigns}) {
  return (
    <div className="container">
      <div className="row">
        {campaigns.map((campaign) => (
          <div className="col-12" key={campaign.uuid}>
            <Link to="/campaign-overview" state={{ campaign }}>
              <h4>{campaign.name}</h4>
            </Link>
            <p>{campaign.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
