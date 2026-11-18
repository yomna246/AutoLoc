package tn.esprit.autoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

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
abstract class AbstractGenericServiceTest<T> {

    protected static final Long ID = 1L;

    protected abstract GenericServiceImpl<T, Long> service();

    protected abstract JpaRepository<T, Long> repository();

    /** Entité valide, sans identifiant. */
    protected abstract T valid();

    /** Entité qui ne passe pas la méthode check(). */
    protected abstract T invalid();

    /** Entité valide, avec un identifiant. */
    protected abstract T validWithId();

    @Test
    void checkValidEntityReturnsTrue() {
        assertTrue(service().check(valid(), true));
    }

    @Test
    void checkInvalidEntityReturnsFalse() {
        assertFalse(service().check(invalid(), true));
    }

    @Test
    void createSavesValidEntity() {
        T entity = valid();
        when(repository().save(entity)).thenReturn(entity);

        T result = service().create(entity);

        assertSame(entity, result);
        verify(repository()).save(entity);
    }

    @Test
    void createWithIdThrowsException() {
        T entity = validWithId();

        assertThrows(IllegalArgumentException.class, () -> service().create(entity));
        verify(repository(), never()).save(any());
    }

    @Test
    void createInvalidEntityThrowsException() {
        T entity = invalid();

        assertThrows(IllegalArgumentException.class, () -> service().create(entity));
        verify(repository(), never()).save(any());
    }

    @Test
    void findByIdReturnsEntity() {
        T entity = validWithId();
        when(repository().findById(ID)).thenReturn(Optional.of(entity));

        assertSame(entity, service().findById(ID));
    }

    @Test
    void findByIdUnknownThrowsNotFound() {
        when(repository().findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service().findById(ID));
    }

    @Test
    void findAllReturnsAllEntities() {
        List<T> entities = List.of(valid(), valid());
        when(repository().findAll()).thenReturn(entities);

        assertEquals(2, service().findAll().size());
    }

    @Test
    void updateSavesExistingEntity() {
        T existing = validWithId();
        when(repository().findById(ID)).thenReturn(Optional.of(existing));
        when(repository().save(existing)).thenReturn(existing);

        T result = service().update(ID, valid());

        assertSame(existing, result);
        verify(repository()).save(existing);
    }

    @Test
    void updateInvalidEntityThrowsException() {
        when(repository().findById(ID)).thenReturn(Optional.of(validWithId()));
        T entity = invalid();

        assertThrows(IllegalArgumentException.class, () -> service().update(ID, entity));
        verify(repository(), never()).save(any());
    }

    @Test
    void deleteByIdDeletesExistingEntity() {
        when(repository().existsById(ID)).thenReturn(true);

        service().deleteById(ID);

        verify(repository()).deleteById(ID);
    }

    @Test
    void deleteByIdUnknownThrowsNotFound() {
        when(repository().existsById(ID)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service().deleteById(ID));
        verify(repository(), never()).deleteById(any());
    }
}