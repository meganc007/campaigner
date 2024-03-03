import "./App.css";
import { Routes, Route } from "react-router-dom";
import CampaignOverview from "./pages/CampaignOverview";
import AllCampaigns from "./pages/AllCampaigns";

function App() {
  return (
    <Routes>
      <Route path="/" element={<AllCampaigns />} />
      <Route path="/campaign-overview" element={<CampaignOverview />} />
    </Routes>
  );
}

export default App;
