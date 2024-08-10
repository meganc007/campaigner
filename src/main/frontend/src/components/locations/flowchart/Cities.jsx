import Landmarks from "./Landmarks";

export default function Cities({ cities, landmarks }) {
  return (
    <>
      {cities.length > 0 && (
        <ul>
          {cities.map((city) => (
            <li key={city.id}>
              <a href="">
                {city.name}
                <br />
                <small>City</small>
              </a>
            </li>
          ))}
          <Landmarks landmarks={landmarks} />
        </ul>
      )}
    </>
  );
}
