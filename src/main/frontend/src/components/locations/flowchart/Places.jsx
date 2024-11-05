import Heading from "./Heading";

export default function Places({ places, comparison }) {
  return (
    <>
      {places.length > 0 && (
        <>
          {places.map((place) =>
            place.fk_city === comparison ? (
              <li key={place.id}>
                <Heading
                  classType="place"
                  data={place}
                  subheading="Place"
                  extraInfo={
                    <>
                      Type: {place.placeType.name}
                      <br />
                      Terrain: {place.terrain.name}
                    </>
                  }
                />
              </li>
            ) : null
          )}
        </>
      )}
    </>
  );
}
