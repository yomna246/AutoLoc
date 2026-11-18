package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaiementServiceImplTest {

    private static final Long ID = 1L;

    @Mock
    private IPaiementRepository repository;

    @InjectMocks
    private PaiementServiceImpl service;

    @Test
    void findByIdReturnsPaiement() {
        Paiement paiement = new Paiement();
        when(repository.findById(ID)).thenReturn(Optional.of(paiement));

        assertSame(paiement, service.findById(ID));
    }

    @Test
    void findByIdUnknownThrowsNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(ID));
    }

    @Test
    void findAllReturnsAllPaiements() {
        when(repository.findAll()).thenReturn(List.of(new Paiement(), new Paiement()));

        assertEquals(2, service.findAll().size());
    }
}