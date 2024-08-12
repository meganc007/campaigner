export default function Heading({ data, subheading, extraInfo }) {
  return (
    <a href="">
      {data.name}

      <h6>{subheading}</h6>

      {extraInfo !== null && (
        <>
          <span>{extraInfo}</span>
        </>
      )}
    </a>
  );
}
