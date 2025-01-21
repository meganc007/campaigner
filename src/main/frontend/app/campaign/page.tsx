import Link from "next/link";

type Campaign = {
  name: string;
  description: string;
  uuid: string;
};

async function fetchCampaignData(): Promise<Campaign[]> {
  const response = await fetch("http://localhost:8080/api/campaigns");
  if (!response.ok) {
    throw new Error("Failed to fetch campaign data");
  }
  return response.json();
}

export default async function AllCampaigns() {
  const campaigns = await fetchCampaignData();
  return (
    <>
      <div className="flex flex-col md:flex-row md:text-lg min-h-screen">
        <div className="px-2 w-full space-y-2 flex-grow">
          <h1 className="py-4">Choose a campaign to view/edit</h1>
          <div className="flex flex-col items-center space-y-4 w-full md:flex-row md:justify-self-center md:justify-center md:flex-wrap md:items-start md:gap-y-6 md:py-8 md:space-y-0 md:gap-x-4 lg:gap-x-8 lg:w-8/12">
            {campaigns.map((campaign) => (
              <div
                className="flex flex-col w-full md:w-4/12 lg:w-3/12"
                key={campaign.uuid}
              >
                <div className="bg-celestial_blue rounded-tl-md rounded-tr-md border-2 border-gunmetal p-4 hover:bg-icterine">
                  <h2>
                    <Link
                      href={`/campaign/overview/${campaign.uuid}/`}
                      className="no-underline bg-transparent hover:bg-transparent"
                    >
                      {campaign.name}
                    </Link>
                  </h2>
                </div>
                <div className="rounded-bl-md rounded-br-md border-2 border-t-0 border-gunmetal p-4 md:min-h-24">
                  <p>{campaign.description}</p>
                </div>
              </div>
            ))}
          </div>
          <hr className="border-gunmetal border-1" />
          <Link href={"/"} className="my-4">
            Return to Home
          </Link>
        </div>
      </div>
    </>
  );
}
