CREATE TABLE IF NOT EXISTS Breweries
(
    brewery_id BIGSERIAL PRIMARY KEY,
    name       VARCHAR(60) NOT NULL,
    city       VARCHAR(60) NOT NULL,
    state      VARCHAR(20),
    country    VARCHAR(30) NOT NULL
);

COPY Breweries (brewery_id, name, city, state, country)
    FROM '/docker-entrypoint-initdb.d/breweries.csv'
    WITH (FORMAT CSV, HEADER TRUE);

--- Synchronize the sequence after inserts
SELECT setval(
               pg_get_serial_sequence('breweries', 'brewery_id'),
               (SELECT MAX(brewery_id) FROM Breweries)
       );