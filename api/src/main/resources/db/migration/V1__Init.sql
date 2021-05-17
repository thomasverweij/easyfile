CREATE TABLE bucket (
    id uuid NOT NULL,
    password character varying(255)
);

CREATE TABLE file_metadata (
    id uuid NOT NULL,
    created_date timestamp without time zone,
    file_name character varying(255),
    bucket_id uuid
);

ALTER TABLE ONLY bucket
    ADD CONSTRAINT bucket_pkey PRIMARY KEY (id);


ALTER TABLE ONLY file_metadata
    ADD CONSTRAINT file_metadata_pkey PRIMARY KEY (id);


ALTER TABLE ONLY file_metadata
    ADD CONSTRAINT fk3vmew6wv58ndo6pu4ss3lji3l FOREIGN KEY (bucket_id) REFERENCES public.bucket(id);

