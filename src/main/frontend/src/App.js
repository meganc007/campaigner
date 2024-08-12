import { Routes, Route } from "react-router-dom";
import CampaignOverview from "./pages/CampaignOverview.jsx";
import AllCampaigns from "./pages/AllCampaigns.jsx";

function App() {
  return (
    <Routes>
      <Route path="/" element={<AllCampaigns />} />
      <Route path="/campaign-overview" element={<CampaignOverview />} />
    </Routes>
  );
}

export default App;
