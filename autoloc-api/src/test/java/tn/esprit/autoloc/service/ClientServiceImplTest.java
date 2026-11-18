package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

class ClientServiceImplTest extends AbstractGenericServiceTest<Client> {

    @Mock
    private IClientRepository repository;

    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClientServiceImpl(repository);
    }

    @Override
    protected GenericServiceImpl<Client, Long> service() {
        return service;
    }

    @Override
    protected JpaRepository<Client, Long> repository() {
        return repository;
    }

    @Override
    protected Client valid() {
        return Client.builder()
                .nom("Ben Ali")
                .prenom("Sami")
                .email("sami@mail.tn")
                .numPermis("P12345")
                .build();
    }

    @Override
    protected Client invalid() {
        return Client.builder().nom("Ben Ali").build();
    }

    @Override
    protected Client validWithId() {
        Client client = valid();
        client.setIdClient(ID);
        return client;
    }
}