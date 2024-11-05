import Heading from "./Heading";
import Countries from "./Countries";

export default function Continents({ continents }) {
  return (
    <>
      {continents &&
        continents.map((continent) => (
          <li key={continent.id}>
            <Heading
              data={continent}
              subheading="Continent"
              classType="continent"
            />
            {continent.countries.length > 0 && (
              <Countries countries={continent.countries} />
            )}
          </li>
        ))}
    </>
  );
}
