import Cities from "./Cities";
import Heading from "./Heading";

export default function Regions({ regions }) {
  return (
    <>
      {regions.length > 0 && (
        <ul>
          {regions.map((region) => (
            <li key={region.id}>
              <Heading data={region} subheading="Region" />
              <Cities cities={region.cities} landmarks={region.landmarks} />
            </li>
          ))}
        </ul>
      )}
    </>
  );
}
