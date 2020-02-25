DROP TABLE account IF EXISTS;
DROP TABLE role IF EXISTS;
DROP TABLE account_permissions IF EXISTS;
DROP TABLE account_roles IF EXISTS;


CREATE TABLE accounts (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(55),
	age character varying(155),
	location character varying(155),
	image_uri character varying(75),
	username character varying(55) NOT NULL,
	password character varying(155) NOT NULL,
	uuid character varying(55)
);

CREATE TABLE roles (
	id bigint PRIMARY KEY AUTO_INCREMENT,
	name character varying(55) NOT NULL UNIQUE
);

CREATE TABLE account_permissions (
	account_id bigint REFERENCES accounts(id),
	permission character varying(55)
);

CREATE TABLE account_roles (
	role_id bigint NOT NULL REFERENCES roles(id),
	account_id bigint NOT NULL REFERENCES accounts(id)
);