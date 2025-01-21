"use client";
import Loading from "@/app/components/loading";
import { useParams } from "next/navigation";
import { useState, useEffect } from "react";

type Campaign = {
  name: string;
  description: string;
  uuid: string;
};

export default function Overview() {
  const { uuid } = useParams<{ uuid: string }>();
  const [campaign, setCampaign] = useState<Campaign>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function fetchCampaignData(): Promise<Campaign> {
    const response = await fetch(`http://localhost:8080/api/campaigns/${uuid}`);
    if (!response.ok) {
      throw new Error("Failed to fetch campaign data");
    }
    return response.json();
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
        const fetchedCampaign = await fetchCampaignData();
        setCampaign(fetchedCampaign);
      } catch (err) {
        setError("Failed to fetch campaign data");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return <Loading />;
  }

  if (error) {
    return (
      <div className="flex flex-col p-2 md:flex-row md:text-lg">{error}</div>
    );
  }

  return (
    <div className="flex flex-col md:flex-row md:text-lg">
      <div className="px-2 w-full">
        <h1 className="py-4">Campaign Overview</h1>
        <h2>{campaign?.name}</h2>
        <p>{campaign?.description}</p>
      </div>
    </div>
  );
}
