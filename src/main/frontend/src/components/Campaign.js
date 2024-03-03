function Campaign(props) {
  const campaigns = props.campaigns;
  return (
    <div className="container">
      <div className="row">
        {campaigns.map((campaign) => (
          <div className="col-12" key={campaign.uuid}>
            <h4>{campaign.name}</h4>
            <p>{campaign.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Campaign;
