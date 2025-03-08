CREATE TABLE IF NOT EXISTS admin (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(255) NOT NULL,
    statusactive BOOLEAN,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(255) NOT NULL,
    statusactive BOOLEAN,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS gateway (
    id SERIAL PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    serialnumber VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS homebase_gateway (
    id SERIAL PRIMARY KEY,
    homebaseid INTEGER NOT NULL,
    gatewayid INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS homebase_installer (
    id SERIAL PRIMARY KEY,
    homebaseid INTEGER NOT NULL,
    gatewayid INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS homebase_inverter (
    id SERIAL PRIMARY KEY,
    homebaseid INTEGER NOT NULL,
    inverterid INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS homebaseid (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(255) NOT NULL,
    statusactive BOOLEAN,
    homebaseid VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    gateway_id INTEGER,
    user_id INTEGER,
    installer_id INTEGER,
    token_influxdb VARCHAR(255),
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS homebase_user (
    id SERIAL PRIMARY KEY,
    homebaseid VARCHAR(255) NOT NULL,
    customeremail VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS installer (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(255) NOT NULL,
    statusactive BOOLEAN,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS inverter (
    id SERIAL PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    serialnumber VARCHAR(255) NOT NULL
);
