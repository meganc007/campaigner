import Landmarks from "./Landmarks";
import Heading from "./Heading";

export default function Cities({ cities, landmarks }) {
  return (
    <>
      {cities.length > 0 && (
        <ul>
          {cities.map((city) => (
            <li key={city.id}>
              <Heading data={city} subheading="City" />
            </li>
          ))}
          <Landmarks landmarks={landmarks} />
        </ul>
      )}
    </>
  );
}
