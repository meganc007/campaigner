import scroll from './scroll.svg';
import './App.css';
import { useState, useEffect } from 'react';

function App() {
const [campaigns, setCampaigns] = useState([]);
  useEffect(() => {
    fetch('http://localhost:8080/api/campaigns')
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        console.log(data);
        setCampaigns(data);
      });
  }, []);
  return (
    <div className="App">
      <div>
        <h1>Campaigner app</h1>
              {campaigns.map((campaign) => (
              <>
              <p>Campaign uuid: {campaign.uuid}</p>
              <p>Campaign name: {campaign.name}</p>
              <p>Campaign description: {campaign.description}</p>
              </>
              ))}
        </div>
    </div>
  );
}

export default App;
