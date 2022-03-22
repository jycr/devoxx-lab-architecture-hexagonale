package devoxx.lab.hexagonalarchitecture.courtage.application.springboot.adapters.persistence;

import devoxx.lab.hexagonalarchitecture.courtage.domain.model.Portefeuille;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface PortefeuilleSpringDataCrudRepository extends CrudRepository<Portefeuille, String> {
}
