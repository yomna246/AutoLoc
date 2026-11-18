package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicAgenceServiceImpTest {

    private static final Long ID = 1L;

    @Mock
    private IAgenceRepository repository;

    @InjectMocks
    private BasicAgenceServiceImp service;

    private Agence valid() {
        return Agence.builder()
                .nom("AutoLoc Tunis")
                .ville("Tunis")
                .adresse("12 Avenue Habib Bourguiba")
                .telephone("71 000 000")
                .build();
    }

    @Test
    void checkValidAgenceReturnsTrue() {
        assertTrue(service.check(valid(), true));
    }

    @Test
    void checkAgenceWithoutTelephoneReturnsFalse() {
        Agence agence = valid();
        agence.setTelephone(null);

        assertFalse(service.check(agence, true));
    }

    @Test
    void createSavesValidAgence() {
        Agence agence = valid();
        when(repository.save(agence)).thenReturn(agence);

        assertSame(agence, service.create(agence));
    }

    @Test
    void createWithIdThrowsException() {
        Agence agence = valid();
        agence.setIdAgence(ID);

        assertThrows(IllegalArgumentException.class, () -> service.create(agence));
        verify(repository, never()).save(any());
    }

    @Test
    void createInvalidAgenceThrowsException() {
        Agence agence = Agence.builder().nom("AutoLoc Tunis").build();

        assertThrows(IllegalArgumentException.class, () -> service.create(agence));
        verify(repository, never()).save(any());
    }

    @Test
    void findByIdUnknownThrowsNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(ID));
    }

    @Test
    void findAllReturnsAllAgences() {
        when(repository.findAll()).thenReturn(List.of(valid(), valid()));

        assertEquals(2, service.findAll().size());
    }

    @Test
    void updateCopiesFieldsAndSaves() {
        Agence existing = valid();
        existing.setIdAgence(ID);
        Agence source = valid();
        source.setNom("AutoLoc Sfax");
        when(repository.findById(ID)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Agence result = service.update(ID, source);

        assertEquals("AutoLoc Sfax", result.getNom());
    }

    @Test
    void updateInvalidAgenceThrowsException() {
        when(repository.findById(ID)).thenReturn(Optional.of(valid()));
        Agence source = Agence.builder().nom("AutoLoc Sfax").build();

        assertThrows(IllegalArgumentException.class, () -> service.update(ID, source));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteByIdDeletesExistingAgence() {
        when(repository.existsById(ID)).thenReturn(true);

        service.deleteById(ID);

        verify(repository).deleteById(ID);
    }

    @Test
    void deleteByIdUnknownThrowsNotFound() {
        when(repository.existsById(ID)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(ID));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void hasVehiculesReturnsFalseWhenEmpty() {
        when(repository.findById(ID)).thenReturn(Optional.of(valid()));

        assertFalse(service.hasVehicules(ID));
    }

    @Test
    void hasVehiculesReturnsTrueWhenNotEmpty() {
        Agence agence = valid();
        agence.addVehicule(new Vehicule());
        when(repository.findById(ID)).thenReturn(Optional.of(agence));

        assertTrue(service.hasVehicules(ID));
    }
}