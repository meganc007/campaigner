export default function Landmarks({ landmarks }) {
  return (
    <>
      {landmarks.length > 0 && (
        <>
          {landmarks.map((landmark) => (
            <li key={landmark.id}>
              <a href="">
                {landmark.name}
                <br />
                <small>Landmark</small>
              </a>
            </li>
          ))}
        </>
      )}
    </>
  );
}
