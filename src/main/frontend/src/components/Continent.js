import { Link } from "react-router-dom";

function Continent(props) {
  const continents = props.continents;
  return (
    <div className="container">
      <div className="row">
        {continents.map((continent) => (
          <div className="col-12" key={continent.id}>
            <h4>{continent.name}</h4>
            <p>{continent.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Continent;
