import { Link } from "react-router-dom";

function Continent(props) {
  const continents = props.continents;
  return (
    <div className="container">
      <div className="row">
        <div className="col-12">
          <h2>Continents</h2>
          {continents.map((continent) => (
            <div key={continent.id}>
              <h4>{continent.name}</h4>
              <p>{continent.description}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Continent;
