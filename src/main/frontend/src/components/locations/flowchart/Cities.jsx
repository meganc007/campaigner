import Landmarks from "./Landmarks";
import Heading from "./Heading";
import Places from "./Places";

export default function Cities({ cities, landmarks, places }) {
  return (
    <>
      {cities.length > 0 && (
        <ul>
          {cities.map((city) => (
            <li key={city.id}>
              <Heading data={city} subheading="City" />
              <ul>
                <Places places={places} comparison={city.id} />
              </ul>
            </li>
          ))}
          <Landmarks landmarks={landmarks} />
          <Places places={places} comparison={null} />
        </ul>
      )}
    </>
  );
}
