<h1 align="center">Library Software · IUT de Paris - Rives de Seine</h1>

This project aims to allow the management of reservations, borrows and returns for a library. Its primary goal is to handle concurrency using the Java programming language.

> [!IMPORTANT]  
> The project has been developed exclusively in a university context, at the IUT de Paris - Rives de Seine

## Usage

Each type of operation is handled via a specific port on the server.

1. **Reservations**
   - **Port**: 3000
   - **Usage**: Document reservation requests are made remotely by subscribers. The reservation client sends a request containing the subscriber’s number and the desired document number. If the document is available, it is reserved for a defined duration.

2. **Borrowings**
   - **Port**: 4000
   - **Usage**: Borrowings are done on-site at the media library. The borrowing client sends a request with the subscriber’s number and the document number. The borrowing is validated if the document is available or reserved for the specific subscriber.

3. **Returns**
   - **Port**: 5000
   - **Usage**: Returns are also done on-site. The return client sends a request with the document number. The system verifies that the document was indeed borrowed and records the return.

Clients for each operation connect to the corresponding port and exchange the necessary data with the server service.
