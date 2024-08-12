export default function Heading({ data, subheading }) {
  return (
    <a href="">
      {data.name}
      <br />
      <small>{subheading}</small>
    </a>
  );
}
