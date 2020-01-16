# revolut-billing-system

Uses double-entry bookkeeping. Allows to deposit money for accounts and transfer money between them.
Each account is identified by its `type`, identifier of its owner (or `subjectId`) and `currency`.

Each `operation` creates one or more `transaction`s representing a single money transfer between two accounts 
(e.g. `TransferOperation` creates two transactions - to transfer money and to collect commission for operation).

`api/` module is designed to be pluggable in client applications and implemented as Feign interfaces.

Hsqldb, Jooq and Flyway are used for persistence, Java-Spark for http controllers, Guice for DI.

Testing is implemented as integration tests with JUnit and application's Feign API. 
