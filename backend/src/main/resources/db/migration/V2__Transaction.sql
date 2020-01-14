CREATE SEQUENCE transaction_seq;

CREATE TABLE transaction (
  id                    BIGINT            NOT NULL DEFAULT NEXTVAL('transaction_seq'),
  operation_id          VARCHAR(40)       NOT NULL,
  operation_type        VARCHAR(30)       NOT NULL,

  from_subject_id       VARCHAR(100)      NOT NULL,
  from_account_type     VARCHAR(30)       NOT NULL,

  to_subject_id         VARCHAR(100)      NOT NULL,
  to_account_type       VARCHAR(30)       NOT NULL,

  amount                NUMERIC(15, 2)    NOT NULL,
  currency              VARCHAR(3)        NOT NULL,

  CONSTRAINT transaction_pkey PRIMARY KEY (id)
);

CREATE INDEX transaction_operation_id_idx ON transaction(operation_id);