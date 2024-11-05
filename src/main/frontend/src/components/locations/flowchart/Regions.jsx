import Cities from "./Cities";
import Heading from "./Heading";

export default function Regions({ regions }) {
  return (
    <>
      {regions.length > 0 && (
        <ul>
          {regions.map((region) => (
            <li key={region.id}>
              <Heading
                classType="region"
                data={region}
                subheading="Region"
                extraInfo={`climate: ${region.climate.name}`}
              />
              <Cities
                cities={region.cities}
                landmarks={region.landmarks}
                places={region.places}
              />
            </li>
          ))}
        </ul>
      )}
    </>
  );
}
