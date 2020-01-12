SET DATABASE SQL SYNTAX PGS TRUE;

CREATE SEQUENCE account_seq;

CREATE TABLE account (
  id             BIGINT             NOT NULL DEFAULT NEXTVAL('account_seq'),
  subject_id     VARCHAR(100)       NOT NULL,
  account_type   VARCHAR(30)        NOT NULL,
  amount         NUMERIC(15, 2)     NOT NULL,
  currency       VARCHAR(3)         NOT NULL,

  CONSTRAINT account_pkey PRIMARY KEY (id)
);

INSERT INTO account (subject_id, account_type, amount, currency) VALUES ('1111', 'MAIN_USER_ACCOUNT', 0, 'USD');