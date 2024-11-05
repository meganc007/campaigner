import Regions from "./Regions";
import Heading from "./Heading";

export default function Countries({ countries }) {
  return (
    <ul>
      {countries.map((country) => (
        <li key={country.id}>
          <Heading data={country} subheading="Country" classType="country" />
          <Regions regions={country.regions} />
        </li>
      ))}
    </ul>
  );
}
