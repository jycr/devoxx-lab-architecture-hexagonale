package devoxx.lab.archihexa.courtage.application.springboot.adapters;

import devoxx.lab.archihexa.courtage.domain.model.Portefeuille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPortefeuilleRepository extends JpaRepository<Portefeuille, String> {
}
