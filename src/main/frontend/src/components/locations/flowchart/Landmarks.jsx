import Heading from "./Heading";

export default function Landmarks({ landmarks }) {
  return (
    <>
      {landmarks.length > 0 && (
        <>
          {landmarks.map((landmark) => (
            <li key={landmark.id}>
              <Heading
                data={landmark}
                subheading="Landmark"
                classType="landmark"
              />
            </li>
          ))}
        </>
      )}
    </>
  );
}
