export default function Country({ countries }) {
    return (
        <div className="container">
            <div className="row">
                <div className="col-12">
                    <h3>Countries</h3>
                    {countries.map((country) => (
                        <div key={country.id}>
                            <h4>{country.name}</h4>
                            <p>{country.description}</p>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}