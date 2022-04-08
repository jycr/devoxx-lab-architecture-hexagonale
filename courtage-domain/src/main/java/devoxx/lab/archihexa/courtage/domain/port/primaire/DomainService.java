package devoxx.lab.archihexa.courtage.domain.port.primaire;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DomainService {
}
