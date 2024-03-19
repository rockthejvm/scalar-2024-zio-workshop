CREATE TABLE IF NOT EXISTS companies (
    id BIGSERIAL PRIMARY KEY,
    slug TEXT UNIQUE NOT NULL,
    name TEXT UNIQUE NOT NULL,
    url TEXT UNIQUE NOT NULL,
    location TEXT,
    country TEXT,
    industry TEXT,
    image TEXT,
    tags TEXT[],
);

CREATE TABLE IF NOT EXISTS reviews
(
	id BIGSERIAL PRIMARY KEY,
	company_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	management INT NOT NULL,
	culture INT NOT NULL,
	salary INT NOT NULL,
	benefits INT NOT NULL,
	would_recommend INT NOT NULL,
	review TEXT NOT NULL,
	created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
	updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
