import Regions from "./Regions";

export default function Countries({ countries }) {
  return (
    <ul>
      {countries.map((country) => (
        <li key={country.id}>
          <a href="">
            {country.name}
            <br />
            <small>Country</small>
          </a>
          <Regions regions={country.regions} />
        </li>
      ))}
    </ul>
  );
}
