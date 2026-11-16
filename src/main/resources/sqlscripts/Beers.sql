CREATE TABLE IF NOT EXISTS Beer
(
    beer_id    BIGSERIAL PRIMARY KEY,
    abv        NUMERIC(4,3),
    ibu_number INTEGER,
    name       VARCHAR(60) NOT NULL,
    style      VARCHAR(40) NOT NULL,
    brewery_id BIGINT NOT NULL,
    ounces     NUMERIC(5,2)
);

COPY Beer (beer_id, abv, ibu_number, name, style, brewery_id, ounces)
    FROM '/docker-entrypoint-initdb.d/beers.csv'
    WITH (FORMAT CSV, HEADER TRUE);

--- Synchronize sequence after explicit ID inserts
SELECT setval(
               pg_get_serial_sequence('beer', 'beer_id'),
               (SELECT MAX(beer_id) FROM Beer)
       );
