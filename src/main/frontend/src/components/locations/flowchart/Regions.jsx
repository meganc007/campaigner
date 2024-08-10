import Cities from "./Cities";

export default function Regions({ regions }) {
  return (
    <>
      {regions.length > 0 && (
        <ul>
          {regions.map((region) => (
            <li key={region.id}>
              <a href="">
                {region.name}
                <br />
                <small>Region</small>
              </a>
              <Cities cities={region.cities} landmarks={region.landmarks} />
            </li>
          ))}
        </ul>
      )}
    </>
  );
}
