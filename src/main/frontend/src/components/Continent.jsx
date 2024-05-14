import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Country from "./Country.jsx";

export default function Continent({ continents }) {
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

